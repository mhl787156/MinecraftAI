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
 * Bidirectional Associative Memory
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class BAM extends NeuralNetwork {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates an instance of BAM network with specified number of neurons
         * in input and output layers.
	 * 
	 * @param inputNeuronsCount
	 *            number of neurons in input layer
	 * @param outputNeuronsCount
	 *            number of neurons in output layer
	 */
	public BAM(int inputNeuronsCount, int outputNeuronsCount) {

		// init neuron settings for BAM network
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("neuronType", "InputOutputNeuron");
		neuronProperties.setProperty("bias", new Double(0));
		neuronProperties.setProperty("transferFunction",
				TransferFunctionType.STEP);
		neuronProperties.setProperty("transferFunction.yHigh", new Double(1));
		neuronProperties.setProperty("transferFunction.yLow", new Double(0));

		this.createNetwork(inputNeuronsCount, outputNeuronsCount, neuronProperties);
	}	
	
	/**
	 * Creates BAM network architecture
	 * 
	 * @param inputNeuronsCount
	 *            number of neurons in input layer
	 * @param outputNeuronsCount
	 *            number of neurons in output layer
	 * @param neuronProperties
	 *            neuron properties
	 */
	private void createNetwork(int inputNeuronsCount, int outputNeuronsCount,  NeuronProperties neuronProperties) {

                // set network type
		this.setNetworkType(NeuralNetworkType.BAM);

		// create input layer
		Layer inputLayer = LayerFactory.createLayer(inputNeuronsCount, neuronProperties);
		// add input layer to network
		this.addLayer(inputLayer);

		// create output layer
		Layer outputLayer = LayerFactory.createLayer(outputNeuronsCount, neuronProperties);	
		// add output layer to network
		this.addLayer(outputLayer);
		
		// create full connectivity from in to out layer	
		ConnectionFactory.fullConnect(inputLayer, outputLayer);		
		// create full connectivity from out to in layer
		ConnectionFactory.fullConnect(outputLayer, inputLayer);
				
		// set input and output cells for this network
		NeuralNetworkFactory.setDefaultIO(this);

		// set Hebbian learning rule for this network
		this.setLearningRule(new BinaryHebbianLearning(this));			
	}		
}
