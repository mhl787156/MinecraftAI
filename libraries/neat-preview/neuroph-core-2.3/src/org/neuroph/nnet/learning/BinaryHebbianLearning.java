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

import org.neuroph.core.Connection;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;

/**
 * Hebbian-like learning algorithm used for Hopfield network. Works with [0, 1] values
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class BinaryHebbianLearning extends UnsupervisedHebbianLearning {
		
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new instance of BinaryHebbianLearning
	 */
	public BinaryHebbianLearning() {
		super();
	}

	/**
	 * Creates new instance of BinaryHebbianLearning for the specified neural network
	 * 
	 * @param neuralNetwork
	 */
	public BinaryHebbianLearning(NeuralNetwork neuralNetwork) {
		super(neuralNetwork);
	}
	

	/**
	 * This method implements weights update procedure for the single neuron
	 * 
	 * @param neuron
	 *            neuron to update weights
	 */
	@Override
	protected void updateNeuronWeights(Neuron neuron) {
		double output = neuron.getOutput();
		for (Connection connection : neuron.getInputConnections()) {
			double input = connection.getInput();

			if (((input>0) && (output>0)) || ((input<=0) && (output<=0))) {
				connection.getWeight().inc(this.learningRate);
			} else {
				connection.getWeight().dec(this.learningRate);
			}
		}
	}	
	
}
