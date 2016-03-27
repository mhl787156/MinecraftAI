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

import java.io.Serializable;
import java.util.Vector;

import org.neuroph.core.Connection;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.learning.SupervisedLearning;

/**
 * LMS learning rule for neural networks.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class LMS extends SupervisedLearning implements Serializable {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new LMS learning rule
	 */
	public LMS() {
		super();
	}

	/**
	 * Creates new LMS learning rule for specified neural network
	 * 
	 * @param neuralNetwork neural network to train
	 */
	public LMS(NeuralNetwork neuralNetwork) {
		super(neuralNetwork);
	}

	/**
	 * Updates total network error with specified pattern error vector
	 * 
	 * @param patternError
	 *            single pattern error vector
         */
        @Override
	protected void updateTotalNetworkError(Vector<Double> patternError) {
		for(Double error : patternError) {
			this.totalNetworkError += (error * error) / patternError.size();
		}
	}

	/**
	 * This method implements weight update procedure for the whole network for
	 * this learning rule
	 * 
	 * @param patternError
	 *            single pattern error vector
	 */
        @Override
	protected void updateNetworkWeights(Vector<Double> patternError) {
		int i = 0;
		for(Neuron neuron : neuralNetwork.getOutputNeurons()) {
			Double outputError = patternError.elementAt(i);
			neuron.setError(outputError.doubleValue());
			this.updateNeuronWeights(neuron);
			i++;
		}
	}

	/**
	 * This method implements weights update procedure for the single neuron
	 * 
	 * @param neuron
	 *            neuron to update weights
	 */
	protected void updateNeuronWeights(Neuron neuron) {
		for(Connection connection : neuron.getInputConnections() ) {
			double input = connection.getInput();
			if (input == 0) {
				continue;
			}
			double neuronError = neuron.getError();
			double deltaWeight = this.learningRate * neuronError * input;
			connection.getWeight().inc(deltaWeight);			
		}
	}

}
