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
import org.neuroph.nnet.comp.ThresholdNeuron;

/**
 * Delta rule learning algorithm for perceptrons with step functions.
 *
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class StepDeltaRule extends LMS {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The e parametar of this learning algorithm
	 */
	private double e = 0.1;

	/**
	 * Creates new StepDeltaRule learning
	 */
	public StepDeltaRule() {
		super();
	}

	/**
	 * Creates new StepDeltaRule learning for the specified neural network
	 *
	 * @param neuralNetwork
	 */
	public StepDeltaRule(NeuralNetwork neuralNetwork) {
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
		int i = 0;
		for(Neuron outputNeuron : neuralNetwork.getOutputNeurons()) {
			ThresholdNeuron neuron = (ThresholdNeuron)outputNeuron;
			double outputError = patternError.elementAt(i);
			if (outputError == 0) {
				neuron.setError(0);
				continue;
			}

			double thresh = neuron.getThresh();
			double netInput = neuron.getNetInput();
			double threshErr = thresh - netInput;
			double neuronError = outputError * (Math.abs(threshErr) + e);
			// if we put neuronError = outputError here, we get perceptron
			// learning algorithm

			neuron.setError(neuronError);
			this.updateNeuronWeights(neuron);
			thresh = thresh - this.learningRate * neuronError;
			neuron.setThresh(thresh);

			i++;
		} // for

	}

	/**
	 * Gets the e parametar
	 *
	 * @return e parametar
	 */
	public Double getE() {
		return new Double(e);
	}

	/**
	 * Sets the e parametar
	 *
	 * @param e
	 *            the value for e parametar
	 */
	public void setE(Double e) {
		this.e = e.doubleValue();
	}

}
