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

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.transfer.TransferFunction;

/**
 * Delta rule learning algorithm for perceptrons with sigmoid functions.
 * 
 * @see LMS
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class SigmoidDeltaRule extends LMS {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new SigmoidDeltaRule
	 */
	public SigmoidDeltaRule() {
		super();
	}

	/**
	 * Creates new SigmoidDeltaRule for the specified neural network
	 * 
	 * @param neuralNetwork neural network to train
	 */
	public SigmoidDeltaRule(NeuralNetwork neuralNetwork) {
		super(neuralNetwork);
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
		this.adjustOutputNeurons(patternError);
	}

	/**
	 * This method implements weights update procedure for the output neurons
	 * 
	 * @param patternError
	 *            single pattern error vector
	 */
	protected void adjustOutputNeurons(Vector<Double> patternError) {
		int i = 0;
		for(Neuron neuron : neuralNetwork.getOutputNeurons()) {
			double outputError = patternError.elementAt(i);
			if (outputError == 0) {
				neuron.setError(0);
				continue;
			}
			
			TransferFunction transferFunction = neuron.getTransferFunction();
			double neuronInput = neuron.getNetInput();
			double delta = outputError
					* transferFunction.getDerivative(neuronInput);
			neuron.setError(delta);
			this.updateNeuronWeights(neuron);				
			i++;
		} // for				
	}

}
