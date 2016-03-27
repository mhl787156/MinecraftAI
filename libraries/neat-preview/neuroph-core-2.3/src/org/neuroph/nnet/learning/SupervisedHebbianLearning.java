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
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.learning.SupervisedTrainingElement;

/**
 * Supervised hebbian learning rule.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class SupervisedHebbianLearning extends LMS {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new instance of SupervisedHebbianLearning algorithm
	 */
	public SupervisedHebbianLearning() {
		super();
	}

	/**
	 * Creates new instance of SupervisedHebbianLearning algorithm  for the specified
	 * neural network.
	 * 
	 * @param neuralNetwork
     *                  neural network to train
	 */
	public SupervisedHebbianLearning(NeuralNetwork neuralNetwork) {
		super(neuralNetwork);
	}

	/**
	 * Learn method override without network error and iteration limit
	 * Implements just one pass through the training set Used for testing -
	 * debuging algorithm
	 * 
	 * public void learn(TrainingSet trainingSet) { Iterator
	 * iterator=trainingSet.iterator(); while(iterator.hasNext()) {
	 * SupervisedTrainingElement trainingElement =
	 * (SupervisedTrainingElement)iterator.next();
	 * this.learnPattern(trainingElement); } }
	 */
	
	/**
	 * Trains network with the pattern from the specified training element
	 * 
	 * @param trainingElement
	 *            supervised training element which contains input and desired
	 *            output
	 */
	@Override
	protected void learnPattern(SupervisedTrainingElement trainingElement) {
		Vector<Double> input = trainingElement.getInput();
		this.neuralNetwork.setInput(input);
		this.neuralNetwork.calculate();
		Vector<Double> output = this.neuralNetwork.getOutput();
		Vector<Double> desiredOutput = trainingElement.getDesiredOutput();
		Vector<Double> patternError = this.getPatternError(output, desiredOutput);
		this.updateTotalNetworkError(patternError);
		this.updateNetworkWeights(desiredOutput);
	}

	/**
	 * This method implements weight update procedure for the whole network for
	 * this learning rule
	 * 
	 * @param desiredOutput
	 *            desired network output
	 */
	@Override
	protected void updateNetworkWeights(Vector<Double> desiredOutput) {
		int i = 0;
		for (Neuron neuron : neuralNetwork.getOutputNeurons()) {
			Double desiredNeuronOutput = desiredOutput.elementAt(i);
			this.updateNeuronWeights(neuron, desiredNeuronOutput);
			i++;
		}

	}

	/**
	 * This method implements weights update procedure for the single neuron
	 * 
	 * @param neuron
	 *            neuron to update weights
	 *        desiredOutput
	 *	      desired output of the neuron
	 */
	protected void updateNeuronWeights(Neuron neuron, double desiredOutput) {
		for (Connection connection : neuron.getInputConnections()) {
			double input = connection.getInput();
			double deltaWeight = input * desiredOutput * this.learningRate;
			connection.getWeight().inc(deltaWeight);
		}
	}
}
