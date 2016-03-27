package org.neuroph.contrib.neat.gen.impl;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.NeuralNetworkBuilder;
import org.neuroph.contrib.neat.gen.TerminationCondition;
import org.neuroph.contrib.neat.gen.operations.FitnessFunction;
import org.neuroph.contrib.neat.gen.operations.MutationOperation;
import org.neuroph.contrib.neat.gen.operations.OrganismSelector;
import org.neuroph.contrib.neat.gen.operations.ReproductionOperation;
import org.neuroph.contrib.neat.gen.operations.Speciator;
import org.neuroph.contrib.neat.gen.operations.mutation.ActivationResponseMutationOperation;
import org.neuroph.contrib.neat.gen.operations.mutation.AddConnectionMutationOperation;
import org.neuroph.contrib.neat.gen.operations.mutation.AddNeuronMutationOperation;
import org.neuroph.contrib.neat.gen.operations.mutation.WeightMutationOperation;
import org.neuroph.contrib.neat.gen.operations.reproduction.CloneReproductionOperator;
import org.neuroph.contrib.neat.gen.operations.reproduction.CrossoverReproductionOperator;
import org.neuroph.contrib.neat.gen.operations.selector.NaturalSelectionOrganismSelector;
import org.neuroph.contrib.neat.gen.persistence.Persistence;
import org.neuroph.contrib.neat.gen.persistence.impl.NoOpPersistence;

/**
 * Provides an implementation of the <code>NeatParameters</code> interface
 * implementing all of the methods with their NEAT C++ defaults.
 * 
 * The only operation that must be provided is the
 * <code>getFitnessFunction</code> as this is implementation specific, all other
 * values are pre-populated with default values.
 * 
 * @author Aidan Morgan
 */
public class SimpleNeatParameters implements NeatParameters {
	private static Logger s_log = Logger.getLogger(SimpleNeatParameters.class.getName());
	
	/**
	 * The current innovation id. Using an <code>AtomicLong</code> in an attempt
	 * to prevent any possible threading issues.
	 */
	private AtomicLong innovationId;

	/**
	 * The <code>MutationOperation</code>s to use for this network evolution.
	 */
	private List<MutationOperation> mutationOperators;

	/**
	 * The <code>ReproductionOperation</code>s to use for this network
	 * evolution.
	 */
	private List<ReproductionOperation> reproductionOperators;

	/**
	 * the <code>OrganismSelector</code> to use for this network evolution.
	 */
	private OrganismSelector organismSelector;

	/**
	 * The total size of the population.
	 */
	private int populationSize;

	/**
	 * The <code>Random</code> instance used for generating random numbers.
	 */
	private Random randomGenerator;

	/**
	 * The <code>FitnessFunction</code> to use for evaluating each
	 * <code>Chromosome</code>s fitness.
	 */
	private FitnessFunction fitnessFunction;

	private NeuralNetworkBuilder neuralNetworkBuilder;

	private Speciator speciator;

	private Persistence persistence;

	private SimpleTerminationCondition terminationCondition;

	/**
	 * Constructor;
	 */
	public SimpleNeatParameters() {
		innovationId = new AtomicLong();
		randomGenerator = new SecureRandom();

		mutationOperators = new ArrayList<MutationOperation>();
		mutationOperators.add(new AddConnectionMutationOperation());
		mutationOperators.add(new AddNeuronMutationOperation());
		mutationOperators.add(new WeightMutationOperation());
		mutationOperators.add(new ActivationResponseMutationOperation());

		reproductionOperators = new ArrayList<ReproductionOperation>();
		reproductionOperators.add(new CrossoverReproductionOperator());
		reproductionOperators.add(new CloneReproductionOperator());

		organismSelector = new NaturalSelectionOrganismSelector();

		neuralNetworkBuilder = new SimpleNeuralNetworkBuilder();
		speciator = new SimpleSpeciator();
		persistence = new NoOpPersistence();

		terminationCondition = new SimpleTerminationCondition();
	}

	public List<MutationOperation> getMutationOperators() {
		return mutationOperators;
	}

	public OrganismSelector getOrganismSelector() {
		return organismSelector;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public double getRandomClamped() {
		return (getRandomGenerator().nextDouble() - getRandomGenerator()
				.nextDouble());
	}

	public Random getRandomGenerator() {
		return randomGenerator;
	}

	public List<ReproductionOperation> getReproductionOperators() {
		return reproductionOperators;
	}

	public long nextInnovationId() {
		long innoId = innovationId.incrementAndGet();
		return innoId;
	}

	public void setMutationOperators(List<MutationOperation> mutationOperators) {
		this.mutationOperators = mutationOperators;
	}

	public void setReproductionOperators(
			List<ReproductionOperation> reproductionOperators) {
		this.reproductionOperators = reproductionOperators;
	}

	public void setOrganismSelector(OrganismSelector organismSelector) {
		this.organismSelector = organismSelector;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public void setRandomGenerator(Random randomGenerator) {
		this.randomGenerator = randomGenerator;
	}

	public FitnessFunction getFitnessFunction() {
		return fitnessFunction;
	}

	public void setFitnessFunction(FitnessFunction f) {
		this.fitnessFunction = f;
	}

	public NeuralNetworkBuilder getNeuralNetworkBuilder() {
		return neuralNetworkBuilder;
	}

	public Speciator getSpeciator() {
		return speciator;
	}

	public long getMaximumGenerations() {
		return terminationCondition.getMaximumGenerations();
	}

	public void setMaximumGenerations(long maxGeneration) {
		terminationCondition.setMaximumGenerations(maxGeneration);
	}

	public double getMaximumFitness() {
		return terminationCondition.getMaximumOrganismFitness();
	}

	public void setMaximumFitness(double maximumFitness) {
		terminationCondition.setMaximumOrganismFitness(maximumFitness);
	}

	public void setNeuralNetworkBuilder(
			NeuralNetworkBuilder neuralNetworkBuilder) {
		this.neuralNetworkBuilder = neuralNetworkBuilder;
	}

	public void setSpeciator(Speciator speciator) {
		this.speciator = speciator;
	}

	public Persistence getPersistence() {
		return persistence;
	}

	public void setPersistence(Persistence p) {
		this.persistence = p;
	}

	public TerminationCondition getTerminationCondition() {
		return terminationCondition;
	}
	
	public void setInnovationIdsStart(long id) {
		if(s_log.isLoggable(Level.INFO)) {
			s_log.info("Setting innovation id start value to: " + id + ".");
		}
		
		innovationId.set(id + 1);
	}
}
