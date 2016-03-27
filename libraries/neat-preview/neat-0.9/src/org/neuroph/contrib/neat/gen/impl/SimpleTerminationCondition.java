package org.neuroph.contrib.neat.gen.impl;

import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.Generation;
import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.TerminationCondition;

/**
 * An implementation of <code>TerminationCondition</code> that will stop the
 * evolutionary process when the maximum number of generations has been reached,
 * or the maximum fitness score has been reached.
 * 
 * @author Aidan Morgan
 */
public class SimpleTerminationCondition implements TerminationCondition {
	/**
	 * The maximum generations to allow the evolution to continue. Defaults to the
	 * value of <code>Long.MAX_VALUE</code> making the evolution process effectively
	 * infinite by default.
	 */
	private long maxGeneration = Long.MAX_VALUE;
	
	/**
	 * The maximum fitness score an <code>Organism</code> can have before the evolution 
	 * is considered complete. Defaults to <code>Double.MAX_VALUE</code> making the
	 * evolution process effectively infinite by default. 
	 */
	private double maxFitness = Double.MAX_VALUE;

	/**
	 * @inheritDoc
	 */
	public boolean exitCriteriaMet(NeatParameters neatParameters,
			int generationNumber, Generation currentGeneration,
			FitnessScores fitness) {
		if (generationNumber > maxGeneration) {
			return true;
		}

		if (fitness.getBestFitness() >= maxFitness) {
			return true;
		}

		return false;
	}

	/**
	 * Returns the maximum generations to allow the evolution to continue before halting.
	 * @return the maximum generations to allow the evolution to continue before halting.
	 */
	public long getMaximumGenerations() {
		return maxGeneration;
	}

	public void setMaximumGenerations(long maxGeneration) {
		this.maxGeneration = maxGeneration;
	}

	/**
	 * Returns the maximum fitness score an <code>Organism</code> can have before the evolution 
	 * is considered complete. 
	 * 
	 * @return the maximum fitness score an <code>Organism</code> can have before the evolution 
	 * is considered complete.
	 */
	public double getMaximumOrganismFitness() {
		return maxFitness;
	}

	/**
	 * Sets the maximum fitness score an <code>Organism</code> can have before the evolution 
	 * is considered complete.
	 * 
	 * @param maxFitness the maximum fitness score expected.
	 */
	public void setMaximumOrganismFitness(double maxFitness) {
		this.maxFitness = maxFitness;
	}

}
