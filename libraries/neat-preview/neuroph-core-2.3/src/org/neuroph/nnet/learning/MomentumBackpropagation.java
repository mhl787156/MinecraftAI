/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.nnet.learning;

import org.neuroph.core.Connection;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;


/**
 * Backpropagation learning rule with momentum.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class MomentumBackpropagation extends BackPropagation {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Momentum factor
	 */
	private double momentum = 0.25;


	/**
	 * Creates new instance of MomentumBackpropagation learning
     */
	public MomentumBackpropagation() {
		super();
	}

	/**
	 * Creates new instance of MomentumBackpropagation learning for the specified neural network
	 * 
	 * @param neuralNetwork
     *                  neural network to train
	 */
	public MomentumBackpropagation(NeuralNetwork neuralNetwork) {
		super(neuralNetwork);
	}
	
	/**
	 * This method implements weights update procedure for the single neuron
	 * for the backpropagation with momentum factor
	 * @param neuron
	 *            neuron to update weights
	 */
	@Override
	protected void updateNeuronWeights(Neuron neuron) {
		for(Connection connection : neuron.getInputConnections() ) {
			double input = connection.getInput();
			if (input == 0) {
				continue;
			}
			double neuronError = neuron.getError();
			
			Weight weight = connection.getWeight();
			
			double currentWeighValue = weight.getValue();
			double previousWeightValue = weight.getPreviousValue();
			double deltaWeight = this.learningRate * neuronError * input +
				momentum * (currentWeighValue - previousWeightValue);
			
			weight.setPreviousValue(currentWeighValue);
			weight.inc(deltaWeight);			
		}
	}

	/**
	 * Returns the momentum factor 
	 * 
	 * @return momentum factor 
	 */
	public double getMomentum() {
		return momentum;
	}

	/**
	 * Sets the momentum factor 
	 * 
	 * @param momentum
	 *            momentum factor
	 */	
	public void setMomentum(double momentum) {
		this.momentum = momentum;
	}

}
