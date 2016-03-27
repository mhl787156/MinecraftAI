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


import java.util.Vector;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 *  Multi Layer Perceptron neural network with Back propagation learning algorithm.
 *
 *  @see org.neuroph.nnet.learning.BackPropagation
 *  @see org.neuroph.nnet.learning.MomentumBackpropagation
 *  @author Zoran Sevarac <sevarac@gmail.com>
 */
public class MultiLayerPerceptron extends NeuralNetwork {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 2L;

	/**
	 * Creates new MultiLayerPerceptron with specified number neurons in
	 * getLayersIterator
	 * 
	 * @param neuronsInLayers
	 *            collection of neuron number in getLayersIterator
	 */
	public MultiLayerPerceptron(Vector<Integer> neuronsInLayers) {
		// init neuron settings
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("transferFunction",
				TransferFunctionType.SIGMOID);

		this.createNetwork(neuronsInLayers, neuronProperties);
	}
	
	public MultiLayerPerceptron(int ... neuronsInLayers) {
		// init neuron settings
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("transferFunction",
				TransferFunctionType.SIGMOID);

		Vector<Integer> neuronsInLayersVector = new Vector<Integer>();
		for(int i=0; i<neuronsInLayers.length; i++)
			neuronsInLayersVector.add(new Integer(neuronsInLayers[i]));
		
		this.createNetwork(neuronsInLayersVector, neuronProperties);
	}

	public MultiLayerPerceptron(TransferFunctionType transferFunctionType, int ... neuronsInLayers) {
		// init neuron settings
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("transferFunction", transferFunctionType);

		Vector<Integer> neuronsInLayersVector = new Vector<Integer>();
		for(int i=0; i<neuronsInLayers.length; i++)
			neuronsInLayersVector.add(new Integer(neuronsInLayers[i]));

		this.createNetwork(neuronsInLayersVector, neuronProperties);
	}

	public MultiLayerPerceptron(Vector<Integer> neuronsInLayers, TransferFunctionType transferFunctionType) {
		// init neuron settings
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("transferFunction", transferFunctionType);

		this.createNetwork(neuronsInLayers, neuronProperties);
	}

	/**
	 * Creates new MultiLayerPerceptron net with specified number neurons in
	 * getLayersIterator
	 * 
	 * @param neuronsInLayers
	 *            collection of neuron numbers in layers
	 * @param neuronProperties
	 *            neuron propreties
	 */
	public MultiLayerPerceptron(Vector<Integer> neuronsInLayers,NeuronProperties neuronProperties) {
		this.createNetwork(neuronsInLayers, neuronProperties);
	}

	/**
	 * Creates MultiLayerPerceptron Network architecture - fully connected
	 * feedforward with specified number of neurons in each layer
	 * 
	 * @param neuronsInLayers
	 *            collection of neuron numbers in getLayersIterator
	 * @param neuronProperties
	 *            neuron propreties
	 */
	private void createNetwork(Vector<Integer> neuronsInLayers, NeuronProperties neuronProperties) {

		// set network type
		this.setNetworkType(NeuralNetworkType.MULTI_LAYER_PERCEPTRON);

		// create layers
		Layer prevLayer = null;
		for(Integer neuronsNum : neuronsInLayers) {	
			// createLayer layer
			Layer layer = LayerFactory
					.createLayer(neuronsNum, neuronProperties);
			// add created layer to network
			this.addLayer(layer);
			// createLayer full connectivity between previous and this layer
			if (prevLayer != null)
				ConnectionFactory.fullConnect(prevLayer, layer);
			prevLayer = layer;
		} // while

		// set input and output cells for network
		NeuralNetworkFactory.setDefaultIO(this);

		// set learnng rule
		//this.setLearningRule(new BackPropagation(this));
		this.setLearningRule(new MomentumBackpropagation());
		
	}

}