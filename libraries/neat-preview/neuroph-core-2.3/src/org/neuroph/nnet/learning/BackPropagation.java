/***
 * Neuroph  http://neuroph.sourceforge.net
 * Copyright by Neuroph Project (C) 2008 
 *
 * This file is part of Neuroph framework.
 *
 * Neuroph is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Neuroph is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Neuroph. If not, see <http://www.gnu.org/licenses/>.
 */

package org.neuroph.nnet.learning;

import java.util.Vector;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.transfer.TransferFunction;

/**
 * Back Propagation learning rule for Multi Layer Perceptron neural networks.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com> 
 * 
 */
public class BackPropagation extends SigmoidDeltaRule {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new instance of BackPropagation learning
	 */
	public BackPropagation() {
		super();
	}

	/**
	 * Creates new instance of BackPropagation learning for the specified neural network
	 * 
	 * @param neuralNetwork
	 */
	public BackPropagation(NeuralNetwork neuralNetwork) {
		super(neuralNetwork);
	}

	/**
	 * This method implements weight update procedure for the whole network
	 * for the specified  error vector
	 * 
	 * @param patternError
	 *            single pattern error vector
	 */
	@Override
	protected void updateNetworkWeights(Vector<Double> patternError) {
		this.adjustOutputNeurons(patternError);
		this.adjustHiddenLayers();
	}

	/**
	 * This method implements weights adjustment for the hidden layers
	 */
	private void adjustHiddenLayers() {
		int layerNum = this.neuralNetwork.getLayersCount();

		for (int i = layerNum - 2; i > 0; i--) {
			Layer layer = neuralNetwork.getLayerAt(i);
			
			for(Neuron neuron : layer.getNeurons()) {	
				double delta = this.calculateDelta(neuron);
				neuron.setError(delta);
				this.updateNeuronWeights(neuron);
			} // for
		} // for
	}

	/**
	 * Calculates and returns delta parameter (neuron error) for the specified
	 * neuron
	 * 
	 * @param neuron
	 *            neuron to calculate error for
	 * @return delta (neuron error) for the specified neuron
	 */
	private double calculateDelta(Neuron neuron) {
		Vector<Connection> connectedTo = ((Neuron) neuron).getOutConnections();

		double delta_sum = 0;
		for(Connection connection : connectedTo) {	
			double d = connection.getConnectedNeuron().getError()
					* connection.getWeight().getValue();
			delta_sum += d; // weighted sum from the next layer
		} // for

		TransferFunction transferFunction = neuron.getTransferFunction();
		double netInput = neuron.getNetInput();
		double f1 = transferFunction.getDerivative(netInput);
		double delta = f1 * delta_sum;
		return delta;
	}

}
