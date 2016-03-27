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
import org.neuroph.nnet.learning.SupervisedHebbianLearning;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 * Hebbian neural network with supervised Hebbian learning algorithm.
 * In order to work this network needs aditional bias neuron in input layer which is allways 1 in training set!
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class SupervisedHebbianNetwork extends NeuralNetwork {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */
	private static final long serialVersionUID = 2L;

	/**
	 * Creates an instance of Supervised Hebbian Network net with specified 
	 * number neurons in input and output layer
	 * 
	 * @param inputNeuronsNum
	 *            number of neurons in input layer
	 * @param outputNeuronsNum
	 *            number of neurons in output layer
	 */
	public SupervisedHebbianNetwork(int inputNeuronsNum, int outputNeuronsNum) {
		this.createNetwork(inputNeuronsNum, outputNeuronsNum,
			TransferFunctionType.RAMP);
	}

	/**
	 * Creates an instance of Supervised Hebbian Network  with specified number
	 * of neurons in input layer and output layer, and transfer function
	 * 
	 * @param inputNeuronsNum
	 *            number of neurons in input layer
	 * @param outputNeuronsNum
	 *            number of neurons in output layer
	 * @param transferFunctionType
	 *            transfer function type id
	 */
	public SupervisedHebbianNetwork(int inputNeuronsNum, int outputNeuronsNum,
		TransferFunctionType transferFunctionType) {
		this.createNetwork(inputNeuronsNum, outputNeuronsNum,
			transferFunctionType);
	}

	/**
	 *Creates an instance of Supervised Hebbian Network with specified number
	 * of neurons in input layer, output layer and transfer function
	 * 
	 * @param inputNeuronsNum
	 *            number of neurons in input layer
	 * @param outputNeuronsNum
	 *            number of neurons in output layer
	 * @param transferFunctionType
	 *            transfer function type
	 */
	private void createNetwork(int inputNeuronsNum, int outputNeuronsNum,
		TransferFunctionType transferFunctionType) {

		// init neuron properties
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("transferFunction", transferFunctionType);
		neuronProperties.setProperty("transferFunction.slope", new Double(1));
		neuronProperties.setProperty("transferFunction.yHigh", new Double(1));
		neuronProperties.setProperty("transferFunction.xHigh", new Double(1));		
		neuronProperties.setProperty("transferFunction.yLow", new Double(-1));
		neuronProperties.setProperty("transferFunction.xLow", new Double(-1));
		
		// set network type code
		this.setNetworkType(NeuralNetworkType.SUPERVISED_HEBBIAN_NET);

		// createLayer input layer
		Layer inputLayer = LayerFactory.createLayer(inputNeuronsNum,
			neuronProperties);
		this.addLayer(inputLayer);

		// createLayer output layer
		Layer outputLayer = LayerFactory.createLayer(outputNeuronsNum,
			neuronProperties);
		this.addLayer(outputLayer);

		// createLayer full conectivity between input and output layer
		ConnectionFactory.fullConnect(inputLayer, outputLayer);

		// set input and output cells for this network
		NeuralNetworkFactory.setDefaultIO(this);

		// set appropriate learning rule for this network
		this.setLearningRule(new SupervisedHebbianLearning(this));
	}
}
