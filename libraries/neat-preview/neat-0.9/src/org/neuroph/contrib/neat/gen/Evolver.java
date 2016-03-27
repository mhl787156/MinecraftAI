package org.neuroph.contrib.neat.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.neuroph.contrib.neat.gen.operations.MutationOperation;
import org.neuroph.contrib.neat.gen.operations.OrganismFitnessScore;
import org.neuroph.contrib.neat.gen.operations.ReproductionOperation;
import org.neuroph.contrib.neat.gen.persistence.Persistence;
import org.neuroph.contrib.neat.gen.persistence.PersistenceException;
import org.neuroph.contrib.neat.gen.util.SpecieHelper;

/**
 * Evolver is the main class that performs the NEAT evolutionary process.
 * 
 * It is responsible for creating or loading a completely configured
 * evolutionary setup and then running it until the exit criteria is met.
 * 
 * @author Aidan Morgan
 */
public class Evolver {
	private static Logger s_log = Logger.getLogger(Evolver.class
			.getSimpleName());
	private Generation currentGeneration;
	private Innovations innovations;
	private NeatParameters neatParameters;
	private FitnessScores fitness;

	public static Evolver createNew(NeatParameters params,
			List<NeuronGene> inputLayer, List<NeuronGene> outputLayer) {
		List<Specie> species = new ArrayList<Specie>();
		List<Organism> organisms = new ArrayList<Organism>();
		Innovations innovations = new Innovations();

		initialise(params, inputLayer, outputLayer, species, organisms,
				innovations);

		Generation gen = new Generation(0, species, params.nextInnovationId());
		innovations.updateFromGeneration(gen);

		FitnessScores scores = new FitnessScores(params);
		// we always start the evolution loop with the fitness scores set, as
		// this is a new population make sure we do the initial calculation.
		calculateFitness(params, scores, organisms, 0);

		
		Evolver e = new Evolver(params, gen, innovations, scores);
		return e;
	}

	/**
	 * Configures the initial state of the <code>Evolver</code> for a new run.
	 * @param params the <code>NeatParameters</code> describing the evolution environment.
	 * @param inputLayer the input <code>NeuronGene</code>s.
	 * @param outputLayer the output <code>NeuronGene</code>s.
	 * @param species the <code>List</code> of initial <code>Specie</code>s.
	 * @param organisms the <code>List</code> of initial <code>Organism</code>s.
	 * @param innovations the <code>Innovations</code>.
	 */
	private static void initialise(NeatParameters params,
			List<NeuronGene> inputLayer, List<NeuronGene> outputLayer,
			List<Specie> species, List<Organism> organisms,
			Innovations innovations) {
		Specie originOfSpecies = new Specie(params);
		Organism ref = new Organism(params, inputLayer, outputLayer, true);
		organisms.add(ref);
		
		// we need to put these into the innovations map so we can reload them
		// later on. This should probably be done by the constructors for 
		// ConnectionGene and NeuronGene, however then we pollute the interface
		// really bad.
		// TODO : AM : Come back and look at this once Persistence loading is working properly.
		innovations.registerInnovations(ref.getConnections());
		innovations.registerInnovations(ref.getNeurons());

		// seed the species list with the correct number of Organisms.
		for (int i = 0; i < params.getPopulationSize() - 1; i++) {
			Organism clone = ref.copy(params);

			originOfSpecies.addOrganism(clone);
			organisms.add(clone);
		}

		species.add(originOfSpecies);
	}

	public static Evolver loadFromPersistence(NeatParameters params)
			throws PersistenceException {
		Persistence p = params.getPersistence();

		Innovations innovations = p.loadInnovations();
		Generation generation = p.loadGeneration(innovations);
		FitnessScores fitnessScores = p.loadFitnessScores();
		
		if(innovations == null) {
			throw new IllegalStateException("Could not load Innovations from Persistence " + p.getClass().getName() + ".");
		}
		
		if(generation == null) {
			throw new IllegalStateException("Could not load Generation from Persistence " + p.getClass().getName() + ".");
		}
		
		if(fitnessScores == null) {
			throw new IllegalStateException("Could not load FitnessScores from Persistence " + p.getClass().getName() + ".");
		}
		
		// reset the innovation id counter to what it was at last checkpoint.
		// this will prevent us from overwriting / duplicating id's
		params.setInnovationIdsStart(generation.getLastInnovationId() + 1);
		
		Evolver e = new Evolver(params, generation,
				innovations, fitnessScores);
		
		return e;
	}

	private Evolver(NeatParameters params, Generation gen,
			Innovations innovations, FitnessScores fitness) {
		this.neatParameters = params;
		this.currentGeneration = gen;
		this.innovations = innovations;
		this.fitness = fitness;
	}

	/**
	 * Peforms the NEAT evolution process. Will return once
	 * <code>NeatParameters.exitCriteriaMet()</code> returns true.
	 * 
	 * @return the fittest <code>Organism</code> from the evolution process.
	 * @throws PersistenceException
	 */
	public Organism evolve() throws PersistenceException {
		int generationNumber = currentGeneration.getGenerationNumber();
		List<Organism> organisms = currentGeneration.getOrganisms();
		List<Specie> species = currentGeneration.getSpecies();

		// keep evolving until the exit criteria has been met.
		while (!neatParameters.getTerminationCondition().exitCriteriaMet(
				neatParameters, generationNumber, currentGeneration, fitness)) {
			List<Organism> organismCopy = new ArrayList<Organism>(organisms);
			List<Organism> newGeneration = runGeneration(neatParameters,
					innovations, species, organismCopy, fitness,
					generationNumber);
			generationNumber++;

			organisms = new ArrayList<Organism>(newGeneration);

			List<Specie> speciesClone = SpecieHelper.copy(species);

			currentGeneration = new Generation(generationNumber, speciesClone, neatParameters.nextInnovationId());

			// update the innovation id maps before persisting.
			innovations.updateFromGeneration(currentGeneration);
			
			neatParameters.getPersistence().addGeneration(innovations,
					currentGeneration, fitness);
		}

		return fitness.getFittestOrganism(organisms);
	}

	/**
	 * Runs one generation of the evolution process.
	 * 
	 * @param innovations
	 * @param species
	 * @param organisms
	 * @param fitnessScores
	 *            TODO
	 * @param generationNumber
	 * @return
	 */
	private static List<Organism> runGeneration(NeatParameters params,
			Innovations innovations, List<Specie> species,
			List<Organism> organisms, FitnessScores fitnessScores,
			int generationNumber) {
		notify(species, EvolutionEventType.START_GENERATION);

		List<Organism> newOrganisms = new ArrayList<Organism>();
		List<Organism> survivingOrganisms = selectOrganisms(params,
				fitnessScores, species, organisms, generationNumber);

		reproduce(params, species, innovations, fitnessScores,
				generationNumber, newOrganisms, survivingOrganisms);
		mutate(params, species, innovations, fitnessScores, generationNumber,
				newOrganisms);

		List<Organism> generation = new ArrayList<Organism>();
		generation.addAll(survivingOrganisms);
		generation.addAll(newOrganisms);

		calculateFitness(params, fitnessScores, generation, generationNumber);
		speciate(params, innovations, species, generation, fitnessScores, generationNumber);

		if (s_log.isLoggable(Level.INFO)) {
			s_log.info("Num species    : " + species.size()
					+ " Num Organisms: " + generation.size());
			s_log.info("End generation : " + generationNumber);
			s_log.info("Best fitness   : " + fitnessScores.getBestFitness());
			s_log.info("Best Organism  : "
					+ fitnessScores.getFittestOrganism(generation)
							.getInnovationId());
		}

		notify(species, EvolutionEventType.END_GENERATION);

		return generation;
	}

	private static void calculateFitness(NeatParameters params,
			FitnessScores scores, List<Organism> organisms, int generationNumber) {
		calculateFitnessScores(params, scores, organisms, generationNumber);
	}

	private static void speciate(NeatParameters params, Innovations innovations, List<Specie> species,
			List<Organism> generation, FitnessScores scores,
			int generationNumber) {
		notify(species, EvolutionEventType.START_SPECIATION);

		List<Specie> survivingSpecies = params.getOrganismSelector()
				.selectSpecies(params, species, scores, generationNumber);
		species.retainAll(survivingSpecies);
		params.getSpeciator().speciate(params, species, scores, generation);

		notify(species, EvolutionEventType.END_SPECIATION);
	}

	private static void mutate(NeatParameters params, List<Specie> species,
			Innovations innovations, FitnessScores fitnessScores,
			int generationNumber, List<Organism> newOrganisms) {
		int totalMutationOperationsPerformed = 0;

		notify(species, EvolutionEventType.START_MUTATION);
		for (MutationOperation mo : params.getMutationOperators()) {
			totalMutationOperationsPerformed += mo.mutate(params, innovations,
					fitnessScores, newOrganisms, generationNumber);
		}
		notify(species, EvolutionEventType.END_MUTATION);
	}

	private static void reproduce(NeatParameters params, List<Specie> species,
			Innovations innovations, FitnessScores fitnessScores,
			int generationNumber, List<Organism> newOrganisms,
			List<Organism> survivingOrganisms) {
		notify(species, EvolutionEventType.START_REPRODUCTION);
		for (ReproductionOperation r : params.getReproductionOperators()) {
			r.reproduce(params, innovations, fitnessScores, (params
					.getPopulationSize()
					- newOrganisms.size() - survivingOrganisms.size()),
					species, newOrganisms, generationNumber);
		}
		notify(species, EvolutionEventType.END_REPRODUCTION);
	}

	private static List<Organism> selectOrganisms(NeatParameters params,
			FitnessScores fitnessScores, List<Specie> species,
			List<Organism> organisms, int generationNumber) {
		notify(species, EvolutionEventType.START_SELECTION);
		List<Organism> survivingOrganisms = params.getOrganismSelector()
				.selectOrganismsForNextGeneration(params, fitnessScores,
						species, organisms, generationNumber);

		List<Specie> deadSpecies = new ArrayList<Specie>();

		for (Specie s : species) {
			s.cull(survivingOrganisms);

			// if there are no organisms left in the species then kill it off.
			if (s.getOrganisms().isEmpty()) {
				deadSpecies.add(s);
			}
		}

		species.removeAll(deadSpecies);
		// remove any of the organisms not selected from the list.
		organisms.retainAll(survivingOrganisms);

		notify(species, EvolutionEventType.END_SELECTION);
		return survivingOrganisms;
	}

	/**
	 * Notify all <code>Species</code> and their <code>Organism</code> of the
	 * current state of the evolutionary process.
	 * 
	 * @param species
	 *            the <code>Specie</code>s to notify.
	 * 
	 * @param type
	 *            a <code>EvolutionEventType</code> representing the type of
	 *            event.
	 */
	private static void notify(List<Specie> species, EvolutionEventType type) {
		for (Specie s : species) {
			s.update(type);
		}
	}

	private static void calculateFitnessScores(NeatParameters params,
			FitnessScores scores, List<Organism> organisms, long generation) {
		List<OrganismFitnessScore> fitnessScores = new ArrayList<OrganismFitnessScore>();

		for (Organism o : organisms) {
			fitnessScores.add(new OrganismFitnessScore(o, params
					.getNeuralNetworkBuilder().createNeuralNetwork(o)));
		}

		params.getFitnessFunction().evaluate(fitnessScores);

		for (OrganismFitnessScore fs : fitnessScores) {
			Organism o = fs.getOrganism();
			double fitness = fs.getFitness();

			scores.setFitness(o, fitness);
		}

		fitnessScores.clear();
	}

	public Innovations getInnovations() {
		return innovations;
	}

	public FitnessScores getFitness() {
		return fitness;
	}
}
