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
import org.neuroph.nnet.comp.CompetitiveLayer;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 * Max Net neural network with competitive learning rule.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class MaxNet extends NeuralNetwork {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new Maxnet network with specified neuron number
	 * 
	 * @param neuronsCount
	 *            number of neurons in MaxNet network (same number in input and output layer)
	 */
	public MaxNet(int neuronsCount) {
		this.createNetwork(neuronsCount);
	}

	/**
	 * Creates MaxNet network architecture
	 * 
	 * @param neuronNum
	 *            neuron number in network
	 * @param neuronProperties
	 *            neuron properties
	 */
	private void createNetwork(int neuronsNum) {

		// set network type
		this.setNetworkType(NeuralNetworkType.MAXNET);

		// createLayer input layer in layer
		Layer inputLayer = LayerFactory.createLayer(neuronsNum,
				new NeuronProperties());
		this.addLayer(inputLayer);

		// createLayer properties for neurons in output layer
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("neuronType", "CompetitiveNeuron");
		neuronProperties.setProperty("transferFunction",
				TransferFunctionType.RAMP);

		// createLayer full connectivity in competitive layer
		CompetitiveLayer competitiveLayer = new CompetitiveLayer(neuronsNum,
				neuronProperties);

		// add competitive layer to network
		this.addLayer(competitiveLayer);

		double competitiveWeight = -(1 / (double) neuronsNum);
		// createLayer full connectivity within competitive layer
		ConnectionFactory.fullConnect(competitiveLayer, competitiveWeight, 1);

		// createLayer forward connectivity from input to competitive layer
		ConnectionFactory.forwardConnect(inputLayer, competitiveLayer, 1);

		// set input and output cells for this network
		NeuralNetworkFactory.setDefaultIO(this);
	}

}
