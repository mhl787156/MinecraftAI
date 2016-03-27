package org.neuroph.contrib.neat.gen.operations.reproduction;

import java.util.ArrayList;
import java.util.List;

import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.Innovations;
import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.Specie;
import org.neuroph.contrib.neat.gen.operations.ReproductionOperation;

/**
 * A <code>ReproductionOperation</code> is responsible for performing all of the
 * <code>Organism</code> reproduction required each generation.
 * 
 * @author Aidan Morgan
 */
public abstract class AbstractReproductionOperation implements
		ReproductionOperation {
	public static final boolean DEFAULT_USE_FITNESS_BIAS = true;

	/**
	 * If set to <code>true</code> then the reproduction is biased in favour of the
	 * fitter <code>Organism</code>s using a simple ratio of each <code>Organism</code>s
	 * fitness versus the total fitness of all <code>Organism</code>s.
	 * 
	 * This behaviour may want to be turned off to allow a "fairer" reproduction
	 * of the <code>Organisms</code>.
	 * 
	 * <b>Default Value: </b> <code>true</code>.
	 */
	private boolean useFitnessBias = DEFAULT_USE_FITNESS_BIAS;
	
	/**
	 * The percentage chance that this operation should be performed.
	 */
	private double operationPercentage;

	/**
	 * Constructor
	 * 
	 * @param percentage
	 *            the percentage change that this operation should be performed.
	 */
	public AbstractReproductionOperation(double percentage) {
		operationPercentage = percentage;
	}

	/**
	 * @inheritDoc
	 */
	public void reproduce(NeatParameters params, Innovations innovations,
			FitnessScores fitnessScores, int requiredOrganisms,
			List<Specie> species, List<Organism> organisms,
			long generationNumber) {

		if (requiredOrganisms <= 0) {
			return;
		}

		List<Organism> newOffspring = new ArrayList<Organism>();

		double allSpeciesFitness = 0.0;

		for (Specie s : species) {
			allSpeciesFitness += fitnessScores.getFitness(s.getOrganisms());
		}

		int sizeBeforeReproduction = organisms.size();

		while (newOffspring.size() < sizeBeforeReproduction + requiredOrganisms) {
			int newSize = requiredOrganisms
					- (newOffspring.size() - sizeBeforeReproduction);

			if (useFitnessBias) {
				reproduceUsingFitnessBias(params, innovations, fitnessScores,
						species, generationNumber, newOffspring,
						allSpeciesFitness, newSize);
			} else {
				reproduceWithoutFitnessBias(params, innovations, fitnessScores,
						species, generationNumber, newOffspring, newSize);
			}
		}

		// prune this back to the required size.
		while (newOffspring.size() > requiredOrganisms) {
			// make sure we only trim back newly created Organisms.
			newOffspring.remove(params.getRandomGenerator().nextInt(
					newOffspring.size()));
		}

		organisms.addAll(newOffspring);
	}

	/**
	 * Performs the <code>reproduce</code> operation without applying the fitness bias. 
	 * 
	 * @see AbstractReproductionOperation#reproduce(NeatParameters, Innovations, FitnessScores, int, List, List, long)
	 */
	private void reproduceWithoutFitnessBias(NeatParameters params,
			Innovations innovations, FitnessScores fitnessScores,
			List<Specie> species, long generationNumber,
			List<Organism> newOffspring, int newSize) {
		int size = (int) Math.ceil(((double) newSize)
				/ ((double) species.size()));

		for (Specie s : species) {
			reproduce(params, innovations, fitnessScores, s.getOrganisms(),
					size, newOffspring, generationNumber);
		}
	}

	/**
	 * Performs the <code>reproduce</code> operation applying the fitness bias. 
	 * 
	 * Determines the number of <code>Organism</code>s from each species by weighting them by 
	 * their relative strength compared to all other <code>Specie</code>s. 
	 * 
	 * I.e. if a particular species is very strong in comparison to all of the others 
	 * then more offspring should be created from it.
	 * 
	 * @see AbstractReproductionOperation#reproduce(NeatParameters, Innovations, FitnessScores, int, List, List, long)
	 */
	private void reproduceUsingFitnessBias(NeatParameters params,
			Innovations innovations, FitnessScores fitnessScores,
			List<Specie> species, long generationNumber,
			List<Organism> newOffspring, double allSpeciesFitness, int newSize) {
		// determine the number of <code>Organism</code>s from each species by
		// weighting them by their relative strength
		// compared to all other <code>Specie</code>s. I.e. if a particular
		// species is very strong in comparison to all of
		// the others then more offspring should be created from it.
		for (Specie s : species) {
			double percentFitness = fitnessScores.getFitness(s.getOrganisms())
					/ allSpeciesFitness;

			// round up because we will just remove random incorrect organisms
			// later.
			int numSpecieOffspring = (int) Math.ceil(percentFitness * newSize);
			reproduce(params, innovations, fitnessScores, s.getOrganisms(),
					numSpecieOffspring, newOffspring, generationNumber);
		}
	}

	/**
	 * Performs the reproduction operation. The <code>Organism</code>s to be
	 * added to the next generation should be addded to the
	 * <code>newOffspring</code> <code>List</code>.
	 * 
	 * @param params
	 *            the <code>NeatParameters</code> that describe the environment
	 *            for the evolution.
	 * @param organisms
	 *            the <code>List</code> of <code>Organisms</code> to reproduce
	 *            from.
	 * @param numSpecieOffspring
	 *            the number of new <code>Organism</code>s to create.
	 * @param newOffspring
	 *            the <code>List</code> containing the new
	 *            <code>Offspring</code>. This <code>List</code> should have all
	 *            new <code>Offspring</code> for the next generation added to
	 *            it.
	 */
	protected abstract void reproduce(NeatParameters params,
			Innovations innovations, FitnessScores fitnessScores,
			List<Organism> organisms, int numSpecieOffspring,
			List<Organism> newOffspring, long generationNumber);

	/**
	 * Retutrns the percentage probability that this
	 * <code>ReproductionOperation</code> should be performed.
	 * 
	 * @return the percentage probability that this
	 *         <code>ReproductionOperation</code> should be performed.
	 */
	public double getOperationPercentage() {
		return operationPercentage;
	}

	/**
	 * Used to enable / disable the fitness bias for this <code>ReproductionOperation</code>.
	 * @param useFitnessBias <code>true</code> to enable fitness biasing, <code>false</code>
	 * otherwise.
	 */
	public void setUseFitnessBias(boolean useFitnessBias) {
		this.useFitnessBias = useFitnessBias;
	}

}
