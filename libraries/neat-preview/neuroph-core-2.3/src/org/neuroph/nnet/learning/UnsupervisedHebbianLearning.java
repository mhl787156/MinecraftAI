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
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.core.learning.UnsupervisedLearning;

/**
 * Unsupervised hebbian learning rule.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class UnsupervisedHebbianLearning extends UnsupervisedLearning {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new instance of UnsupervisedHebbianLearning algorithm
	 */
	public UnsupervisedHebbianLearning() {
		super();
		this.setLearningRate(0.1);
	}

	/**
	 * Creates an instance of UnsupervisedHebbianLearning algorithm  for the specified 
	 * neural network
	 * 
	 * @param neuralNetwork
     *                  neural network to train
	 */
	public UnsupervisedHebbianLearning(NeuralNetwork neuralNetwork) {
		super(neuralNetwork);
		this.setLearningRate(0.1);
	}

	/**
	 * This method does one learning epoch for the unsupervised learning rules.
	 * It iterates through the training set and trains network weights for each
	 * element. Stops learning after one epoch.
	 * 
	 * @param trainingSet
	 *            training set for training network
	 */
	@Override
	public void doLearningEpoch(TrainingSet trainingSet) {
		super.doLearningEpoch(trainingSet);
		stopLearning(); // stop learning ahter one learning epoch
	}

	/**
	 * Adjusts weights for the output neurons
	 */
	protected void adjustWeights() {
		for (Neuron neuron : neuralNetwork.getOutputNeurons()) {
			this.updateNeuronWeights(neuron);
		}
	}

	/**
	 * This method implements weights update procedure for the single neuron
	 * 
	 * @param neuron
	 *            neuron to update weights
	 */
	protected void updateNeuronWeights(Neuron neuron) {
		double output = neuron.getOutput();

		for (Connection connection : neuron.getInputConnections()) {
			double input = connection.getInput();
			double deltaWeight = input * output * this.learningRate;
			connection.getWeight().inc(deltaWeight);
		}
	}
}
