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
import org.neuroph.nnet.learning.CompetitiveLearning;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.SummingFunctionType;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.WeightsFunctionType;

/**
 * Two layer neural network with competitive learning rule.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class CompetitiveNetwork extends NeuralNetwork {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new competitive network with specified neuron number
	 * 
	 * @param inputNeuronsCount
	 *            number of input neurons
         * @param outputNeuronsCount
         *            number of output neurons
	 */
	public CompetitiveNetwork(int inputNeuronsCount, int outputNeuronsCount) {
		this.createNetwork(inputNeuronsCount, outputNeuronsCount);
	}

	/**
	 * Creates Competitive network architecture
	 * 
	 * @param neuronNum
	 *            neuron number in network
	 * @param neuronProperties
	 *            neuron properties
	 */
	private void createNetwork(int inputNeuronsNum, int outputNeuronsNum) {

		// set network type
		this.setNetworkType(NeuralNetworkType.COMPETITIVE);

		// createLayer input layer
		Layer inputLayer = LayerFactory.createLayer(inputNeuronsNum,
				new NeuronProperties());
		this.addLayer(inputLayer);

		// createLayer properties for neurons in output layer
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("neuronType", "CompetitiveNeuron");
		neuronProperties.setProperty("weightsFunction",
				WeightsFunctionType.WEIGHTED_INPUT);
		neuronProperties
				.setProperty("summingFunction", SummingFunctionType.SUM);
		neuronProperties.setProperty("transferFunction",
				TransferFunctionType.RAMP);

		// createLayer full connectivity in competitive layer
		CompetitiveLayer competitiveLayer = new CompetitiveLayer(
				outputNeuronsNum, neuronProperties);

		// add competitive layer to network
		this.addLayer(competitiveLayer);

		double competitiveWeight = -(1 / (double) outputNeuronsNum);
		// createLayer full connectivity within competitive layer
		ConnectionFactory.fullConnect(competitiveLayer, competitiveWeight, 1);

		// createLayer full connectivity from input to competitive layer
		ConnectionFactory.fullConnect(inputLayer, competitiveLayer);

		// set input and output cells for this network
		NeuralNetworkFactory.setDefaultIO(this);

		this.setLearningRule(new CompetitiveLearning(this));
	}

}
