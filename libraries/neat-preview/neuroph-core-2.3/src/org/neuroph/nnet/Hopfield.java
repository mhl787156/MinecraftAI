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
import org.neuroph.nnet.learning.BinaryHebbianLearning;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 * Hopfield neural network.
 * Notes: try to use [1, -1] activation levels, sgn as transfer function, or real numbers for activation
 * @author Zoran Sevarac <sevarac@gmail.com>
 */

public class Hopfield extends NeuralNetwork {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 2L;

	/**
	 * Creates new Hopfield network with specified neuron number
	 * 
	 * @param neuronsNum
	 *            neurons number in Hopfied network
	 */
	public Hopfield(int neuronsNum) {

		// init neuron settings for hopfield network
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("neuronType", "InputOutputNeuron");
		neuronProperties.setProperty("bias", new Double(0));
		neuronProperties.setProperty("transferFunction",
				TransferFunctionType.STEP);
		neuronProperties.setProperty("transferFunction.yHigh", new Double(1));
		neuronProperties.setProperty("transferFunction.yLow", new Double(0));

		this.createNetwork(neuronsNum, neuronProperties);
	}

	/**
	 * Creates new Hopfield network with specified neuron number and neuron
	 * properties
	 * 
	 * @param neuronsNum
	 *            neurons number in Hopfied network
	 * @param neuronProperties
	 *            neuron properties
	 */
	public Hopfield(int neuronsNum, NeuronProperties neuronProperties) {
		this.createNetwork(neuronsNum, neuronProperties);
	}

	/**
	 * Creates Hopfield network architecture
	 * 
	 * @param neuronsNum
	 *            neurons number in Hopfied network
	 * @param neuronProperties
	 *            neuron properties
	 */
	private void createNetwork(int neuronsNum, NeuronProperties neuronProperties) {

		// set network type
		this.setNetworkType(NeuralNetworkType.HOPFIELD);

		// createLayer neurons in layer
		Layer layer = LayerFactory.createLayer(neuronsNum, neuronProperties);

		// createLayer full connectivity in layer
		ConnectionFactory.fullConnect(layer, 0.1);

		// add layer to network
		this.addLayer(layer);

		// set input and output cells for this network
		NeuralNetworkFactory.setDefaultIO(this);

		// set Hopfield learning rule for this network
		//this.setLearningRule(new HopfieldLearning(this));	
		this.setLearningRule(new BinaryHebbianLearning(this));			
	}

}
