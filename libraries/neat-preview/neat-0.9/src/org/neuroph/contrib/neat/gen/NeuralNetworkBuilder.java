package org.neuroph.contrib.neat.gen;

import org.neuroph.core.NeuralNetwork;

/**
 * Simple builder interface for creating an instance of
 * <code>NeuralNetwork</code> from an <code>Organism</code>. This allows
 * different implementations of <code>NeuralNetwork</code> to be used in the
 * NEAT process (i.e. doesn't force a specific <code>NeuralNetwork</code> type
 * onto the NEAT algorithm.
 * 
 * @author Aidan Morgan
 */
public interface NeuralNetworkBuilder {
	/**
	 * Returns an instance of <code>NeuralNetwork</code> which is the phenotype
	 * (i.e. physical manifestation) of the provided <code>Organism</code>.
	 * 
	 * @param o
	 *            the <code>Organism</code> to create the
	 *            <code>NeuralNetwork</code> for.
	 * @return the <code>NeuralNetwork</code> representation of the provided
	 *         <code>Organism</code>.
	 */
	public NeuralNetwork createNeuralNetwork(Organism o);
}
