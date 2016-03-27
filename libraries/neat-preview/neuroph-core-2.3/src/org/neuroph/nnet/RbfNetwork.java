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
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.SummingFunctionType;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.WeightsFunctionType;

/**
 * Radial basis function neural network.
 * 
 * TODO: learning for rbf layer: k-means clustering
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class RbfNetwork extends NeuralNetwork {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */		
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new RbfNetwork with specified number of neurons in input, rbf and output layer
	 * 
	 * @param inputNeuronsNum
	 *		number of neurons in input layer
	 * @param rbfNeuronsNum
	 *		number of neurons in rbf layer
	 * @param outputNeuronsNum
	 *		number of neurons in output layer
	 */
	public RbfNetwork(int inputNeuronsNum, int rbfNeuronsNum, int outputNeuronsNum) {
		this.createNetwork(inputNeuronsNum, rbfNeuronsNum, outputNeuronsNum);
	}

	/**
	 * Creates RbfNetwork architecture with specified number of neurons in input
	 * layer, output layer and transfer function
	 * 
	 * @param inputNeuronsNum
	 *		number of neurons in input layer
	 * @param rbfNeuronsNum
	 *		number of neurons in rbf layer
	 * @param outputNeuronsNum
	 *		number of neurons in output layer
	 */
	private void createNetwork(int inputNeuronsNum, int rbfNeuronsNum,
			int outputNeuronsNum) {
		// init neuron settings for this network
		NeuronProperties rbfNeuronProperties = new NeuronProperties();
		rbfNeuronProperties.setProperty("weightsFunction",
				WeightsFunctionType.DIFERENCE);
		rbfNeuronProperties.setProperty("summingFunction",
				SummingFunctionType.INTENSITY);
		rbfNeuronProperties.setProperty("transferFunction",
				TransferFunctionType.GAUSSIAN);

		// set network type code
		this.setNetworkType(NeuralNetworkType.RBF_NETWORK);

		// create input layer
		Layer inputLayer = LayerFactory.createLayer(inputNeuronsNum,
				TransferFunctionType.LINEAR);
		this.addLayer(inputLayer);

		// create rbf layer
		Layer rbfLayer = LayerFactory.createLayer(rbfNeuronsNum,
				rbfNeuronProperties);
		this.addLayer(rbfLayer);

		// create output layer
		Layer outputLayer = LayerFactory.createLayer(outputNeuronsNum,
				TransferFunctionType.LINEAR);
		this.addLayer(outputLayer);

		// create full conectivity between input and rbf layer
		ConnectionFactory.fullConnect(inputLayer, rbfLayer);
		// create full conectivity between rbf and output layer
		ConnectionFactory.fullConnect(rbfLayer, outputLayer);

		// set input and output cells for this network
		NeuralNetworkFactory.setDefaultIO(this);

		// set appropriate learning rule for this network
		this.setLearningRule(new LMS(this));
	}

}
