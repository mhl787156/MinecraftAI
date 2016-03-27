package org.neuroph.contrib.neat.gen.operations;

import java.util.List;

/**
 * A <code>FitnessFunction</code> is used to evaluate how well a
 * <code>NeuralNetwork</code> performs the task we are attempting to evolve a
 * <code>NeuralNetwork</code> for.
 * 
 * This is the only interface a developer using NEAT must implement.
 * 
 * For a simpler-to use abstract class see: @link AbstractFitnessFunction
 * 
 * @author Aidan Morgan
 */
public interface FitnessFunction {
	/**
	 * Evaluates the provided <code>List</code> of
	 * <code>OrganismFitnessScore</code>s for how well they perform the task we
	 * are attempting to evolve a <code>NeuralNetwork</code> for.
	 * 
	 * After this callback has been invoked it is expected that the
	 * <code>setFitness()</code> for each of the <code>Organism</code>s is set
	 * on it's corresponding <code>OrganismFitnessScore</code>.
	 * 
	 * The <code>NeuralNetwork</code> to be evaluated can also be retrieved from
	 * the <code>OrganismFitnessScore</code> by using the
	 * <code>getNetwork()</code> method.
	 * 
	 * @param fitnesses
	 *            the <code>List</code> of <code>OrganismFitnessScore</code>s to
	 *            evaluate.
	 */
	public void evaluate(List<OrganismFitnessScore> fitnesses);
}
