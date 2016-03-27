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

package org.neuroph.nnet;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.nnet.learning.SigmoidDeltaRule;
import org.neuroph.nnet.learning.StepDeltaRule;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 * Perceptron neural network with some LMS based learning algorithm.
 * 
 * @see org.neuroph.nnet.learning.LMS
 * @see org.neuroph.nnet.learning.StepDeltaRule
 * @see org.neuroph.nnet.learning.SigmoidDeltaRule
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Perceptron extends NeuralNetwork {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new Perceptron with specified number of neurons in input and
	 * output layer, with Step trqansfer function
	 * 
	 * @param inputNeuronsNum
	 *            number of neurons in input layer
	 * @param outputNeuronsNum
	 *            number of neurons in output layer
	 */
	public Perceptron(int inputNeuronsNum, int outputNeuronsNum) {
		this.createNetwork(inputNeuronsNum, outputNeuronsNum,
				TransferFunctionType.STEP);
	}

	/**
	 * Creates new Perceptron with specified number of neurons in input and
	 * output layer, and specified transfer function
	 * 
	 * @param inputNeuronsNum
	 *            number of neurons in input layer
	 * @param outputNeuronsNum
	 *            number of neurons in output layer
	 * @param transferFunctionType
	 *            neuron transfer function type
	 */
	public Perceptron(int inputNeuronsNum, int outputNeuronsNum,
			TransferFunctionType transferFunctionType) {
		this.createNetwork(inputNeuronsNum, outputNeuronsNum,
				transferFunctionType);
	}

	/**
	 * Creates perceptron architecture with specified number of neurons in input
	 * and output layer, specified transfer function
	 * 
	 * @param inputNeuronsNum
	 *            number of neurons in input layer
	 * @param outputNeuronsNum
	 *            number of neurons in output layer
	 * @param transferFunctionType
	 *            neuron transfer function type
	 */
	private void createNetwork(int inputNeuronsNum, int outputNeuronsNum,
			TransferFunctionType transferFunctionType) {

		// set network type
		this.setNetworkType(NeuralNetworkType.PERCEPTRON);

		// init neuron settings for this type of network
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("neuronType", "ThresholdNeuron");
		neuronProperties.setProperty("thresh", new Double(-Math.abs(Math
				.random() - 0.5)).toString());
		neuronProperties.setProperty("transferFunction", transferFunctionType);
		// for sigmoid and tanh transfer functions
		neuronProperties.setProperty("transferFunction.slope", new Double(1)); 				

		// create input layer
		Layer inputLayer = LayerFactory.createLayer(inputNeuronsNum,
				neuronProperties);
		this.addLayer(inputLayer);

		// createLayer output layer
		Layer outputLayer = LayerFactory.createLayer(outputNeuronsNum,
				neuronProperties);
		this.addLayer(outputLayer);

		// create full conectivity between input and output layer
		ConnectionFactory.fullConnect(inputLayer, outputLayer);

		// set input and output cells for this network
		NeuralNetworkFactory.setDefaultIO(this);

		// set appropriate learning rule for this network
		if (transferFunctionType == TransferFunctionType.STEP) {
			this.setLearningRule(new StepDeltaRule(this));
		} else if (transferFunctionType == TransferFunctionType.SIGMOID) {
			this.setLearningRule(new SigmoidDeltaRule(this));
		} else if (transferFunctionType == TransferFunctionType.TANH) {
			this.setLearningRule(new SigmoidDeltaRule(this));
		} else {
			this.setLearningRule(new LMS(this));
		}
	}

}
