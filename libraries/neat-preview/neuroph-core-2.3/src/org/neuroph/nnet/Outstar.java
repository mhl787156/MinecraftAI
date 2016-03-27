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
import org.neuroph.nnet.learning.OutstarLearning;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 * Outstar neural network with Outstar learning rule.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Outstar extends NeuralNetwork {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates an instance of Outstar network with specified number of neurons
     * in output layer.
	 * 
	 * @param outputNeuronsCount
	 *            number of neurons in output layer
	 */
	public Outstar(int outputNeuronsCount) {
		this.createNetwork(outputNeuronsCount);
	}	
	
	/**
	 * Creates Outstar architecture with specified number of neurons in 
	 * output layer
	 * 
	 * @param outputNeuronsCount
	 *            number of neurons in output layer
	 */
	private void createNetwork(int outputNeuronsCount ) {

		// set network type
		this.setNetworkType(NeuralNetworkType.OUTSTAR);

		// init neuron settings for this type of network
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("transferFunction", TransferFunctionType.STEP);
		
		// create input layer
		Layer inputLayer = LayerFactory.createLayer(1, neuronProperties);
		this.addLayer(inputLayer);

		// createLayer output layer
		neuronProperties.setProperty("transferFunction", TransferFunctionType.RAMP);
		Layer outputLayer = LayerFactory.createLayer(outputNeuronsCount,	neuronProperties);
		this.addLayer(outputLayer);

		// create full conectivity between input and output layer
		ConnectionFactory.fullConnect(inputLayer, outputLayer);

		// set input and output cells for this network
		NeuralNetworkFactory.setDefaultIO(this);

		// set outstar learning rule for this network
		this.setLearningRule(new OutstarLearning(this));
	}		
	
}
