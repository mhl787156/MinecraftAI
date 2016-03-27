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
import org.neuroph.nnet.learning.KohonenLearning;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.SummingFunctionType;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.WeightsFunctionType;

/**
 * Kohonen neural network.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Kohonen extends NeuralNetwork {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new Kohonen network with specified number of neurons in input and
	 * map layer
	 * 
	 * @param inputNeuronsNum
	 *            number of neurons in input layer
	 * @param outputNeuronsNum
	 *            number of neurons in output layer
	 */
	public Kohonen(int inputNeuronsNum, int outputNeuronsNum) {
		this.createNetwork(inputNeuronsNum, outputNeuronsNum);
	}

	/**
	 * Creates Kohonen network architecture with specified number of neurons in
	 * input and map layer
	 * 
	 * @param inputNeurons
	 *            number of neurons in input layer
	 * @param outputNeurons
	 *            number of neurons in output layer
	 */
	private void createNetwork(int inputNeuronsNum, int outputNeuronsNum) {

		// specify input neuron properties (use default: weighted sum input with
		// linear transfer)
		NeuronProperties inputNeuronProperties = new NeuronProperties();

		// specify map neuron properties
		NeuronProperties outputNeuronProperties = new NeuronProperties();
		outputNeuronProperties.setProperty("weightsFunction",
				WeightsFunctionType.DIFERENCE);
		outputNeuronProperties.setProperty("summingFunction",
				SummingFunctionType.INTENSITY);
		outputNeuronProperties.setProperty("transferFunction",
				TransferFunctionType.LINEAR);

		// set network type
		this.setNetworkType(NeuralNetworkType.KOHONEN);

		// createLayer input layer
		Layer inLayer = LayerFactory.createLayer(inputNeuronsNum,
				inputNeuronProperties);
		this.addLayer(inLayer);

		// createLayer map layer
		Layer mapLayer = LayerFactory.createLayer(outputNeuronsNum,
				outputNeuronProperties);
		this.addLayer(mapLayer);

		// createLayer full connectivity between input and output layer
		ConnectionFactory.fullConnect(inLayer, mapLayer);

		// set network input and output cells
		NeuralNetworkFactory.setDefaultIO(this);

		this.setLearningRule(new KohonenLearning(this));
	}

}
