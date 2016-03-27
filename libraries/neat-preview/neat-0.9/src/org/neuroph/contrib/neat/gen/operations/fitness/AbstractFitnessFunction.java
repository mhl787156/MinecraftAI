package org.neuroph.contrib.neat.gen.operations.fitness;

import java.util.List;

import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.operations.FitnessFunction;
import org.neuroph.contrib.neat.gen.operations.OrganismFitnessScore;
import org.neuroph.core.NeuralNetwork;

/**
 * Simple adapter abstract base class that removes the <code>List</code> method and
 * instead sequentially calls the <code>evaluate</code> method for an individual 
 * <code>Organism</code>.
 * 
 * This is the typical usage scenario so it is provided by default in this class. The 
 * interface requires the <code>List</code> to allow fitness calculations to be performed
 * in parallel (for longer running tasks).
 * 
 * @author Aidan Morgan
 */
public abstract class AbstractFitnessFunction implements FitnessFunction {

	/**
	 * @inheritDoc
	 */
	public void evaluate(List<OrganismFitnessScore> fitnesses) {
		for (OrganismFitnessScore ofs : fitnesses) {
			double score = evaluate(ofs.getOrganism(), ofs.getNeuralNetwork());
			ofs.setFitness(score);
		}
	}

	/**
	 * Evaluates the provided <code>Organism</code> using the provided <code>NeuralNetwork</code> 
	 * and return it's fitness.
	 * 
	 * @param o the <code>Organism</code> under evaluation.
	 * @param nn the <code>NeuralNetwork</code> to be evaluated.
	 * @return the fitness of the provided <code>NeuralNetwork</code>.
	 */
	protected abstract double evaluate(Organism o, NeuralNetwork nn);
}
