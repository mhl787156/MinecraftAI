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

package org.neuroph.util;

import java.util.Vector;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.Adaline;
import org.neuroph.nnet.BAM;
import org.neuroph.nnet.CompetitiveNetwork;
import org.neuroph.nnet.Hopfield;
import org.neuroph.nnet.Instar;
import org.neuroph.nnet.Kohonen;
import org.neuroph.nnet.MaxNet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Outstar;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.RbfNetwork;
import org.neuroph.nnet.SupervisedHebbianNetwork;
import org.neuroph.nnet.UnsupervisedHebbianNetwork;

/**
 * Provides methods to create various neural networks.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class NeuralNetworkFactory {

	/**
	 * Creates and returns a new instance of Adaline network
	 * @param inputsCount number of inputs of Adaline network
	 * @return instance of Adaline network
	 */
	public static Adaline createAdaline(int inputsCount) {
		Adaline nnet = new Adaline(new Integer(inputsCount));
		return nnet;
	}


	/**
	 * Creates  and returns a new instance of Perceptron network
	 * @param inputNeuronsCount number of neurons in input layer
         * @param outputNeuronsCount number of neurons in output layer
	 * @return instance of Perceptron network
	 */	
	public static Perceptron createPerceptron(int inputNeuronsCount, int outputNeuronsCount, TransferFunctionType transferFunctionType) {
		Perceptron nnet = new Perceptron(inputNeuronsCount, outputNeuronsCount, transferFunctionType);
		return nnet;
	}

	/**
	 * Creates and returns a new instance of Multi Layer Perceptron
	 * @param layersStr space separated number of neurons in layers
	 * @param transferFunctionType transfer function type for neurons
	 * @return instance of Multi Layer Perceptron
	 */
	public static MultiLayerPerceptron createMLPerceptron(String layersStr, TransferFunctionType transferFunctionType) {
		Vector<Integer> layerSizes = VectorParser.parseInteger(layersStr);
		MultiLayerPerceptron nnet = new MultiLayerPerceptron(layerSizes,
				transferFunctionType);
		return nnet;
	}
	
	/**
	 * Creates and returns a new instance of Hopfield network
	 * @param neuronsCount number of neurons in Hopfield network
	 * @return instance of Hopfield network
	 */
	public static Hopfield createHopfield(int neuronsCount) {
		Hopfield nnet = new Hopfield(neuronsCount);
		return nnet;
	}
	
	/**
	 * Creates and returns a new instance of BAM network
	 * @param inputNeuronsCount number of input neurons
         * @param outputNeuronsCount number of output neurons
	 * @return instance of BAM network
	 */
	public static BAM createBam(int inputNeuronsCount, int outputNeuronsCount) {
		BAM nnet = new BAM(inputNeuronsCount, outputNeuronsCount);
		return nnet;
	}	

	/**
	 * Creates and returns a new instance of Kohonen network
	 * @param inputNeuronsCount number of input neurons
         * @param outputNeuronsCount number of output neurons
	 * @return instance of Kohonen network
	 */
	public static Kohonen createKohonen(int inputNeuronsCount, int outputNeuronsCount) {
		Kohonen nnet = new Kohonen(new Integer(inputNeuronsCount), new Integer(outputNeuronsCount));
		return nnet;
	}

	/**
	 * Creates and returns a new instance of Hebbian network
	 * @param inputNeuronsCount number of neurons in input layer
	 * @param outputNeuronsCount number of neurons in output layer
	 * @param transferFunctionType neuron's transfer function type
	 * @return instance of Hebbian network
	 */
	public static SupervisedHebbianNetwork createSupervisedHebbian(int inputNeuronsCount,
			int outputNeuronsCount, TransferFunctionType transferFunctionType) {
		SupervisedHebbianNetwork nnet = new SupervisedHebbianNetwork(inputNeuronsCount,
				outputNeuronsCount, transferFunctionType);
		return nnet;
	}
	
	/**
	 * Creates and returns a new instance of Unsupervised Hebbian Network
	 * @param inputNeuronsCount number of neurons in input layer
	 * @param outputNeuronsCount number of neurons in output layer
	 * @param transferFunctionType neuron's transfer function type
	 * @return instance of Unsupervised Hebbian Network
	 */
	public static UnsupervisedHebbianNetwork createUnsupervisedHebbian(int inputNeuronsCount,
			int outputNeuronsCount, TransferFunctionType transferFunctionType) {
		UnsupervisedHebbianNetwork nnet = new UnsupervisedHebbianNetwork(inputNeuronsCount,
				outputNeuronsCount, transferFunctionType);
		return nnet;
	}	

	/**
	 * Creates and returns a new instance of Max Net network
	 * @param neuronsCount number of neurons (same num in input and output layer)
	 * @return instance of Max Net network
	 */
	public static MaxNet createMaxNet(int neuronsCount) {
		MaxNet nnet = new MaxNet(neuronsCount);
		return nnet;
	}
	
	/**
	 * Creates and returns a new instance of Instar network
	 * @param inputNeuronsCount umber of input neurons
	 * @return instance of Instar network
	 */
	public static Instar createInstar(int inputNeuronsCount) {
		Instar nnet = new Instar(inputNeuronsCount);
		return nnet;
	}	
	
	/**
	 * Creates and returns a new instance of Outstar network
	 * @param outputNeuronsCount number of output neurons
	 * @return instance of Outstar network
	 */
	public static Outstar createOutstar(int outputNeuronsCount) {
		Outstar nnet = new Outstar(outputNeuronsCount);
		return nnet;
	}	

	/**
	 * Creates and returns a new instance of competitive network
	 * @param inputNeuronsCount number of neurons in input layer
	 * @param outputNeuronsCount number of neurons in output layer
	 * @return instance of CompetitiveNetwork
	 */
	public static CompetitiveNetwork createCompetitiveNetwork(
			int inputNeuronsCount, int outputNeuronsCount) {
		CompetitiveNetwork nnet = new CompetitiveNetwork(inputNeuronsCount, outputNeuronsCount);
		return nnet;
	}

	/**
	 * Creates and returns a new instance of RBF network
	 * @param inputNeuronsCount number of neurons in input layer
	 * @param rbfNeuronsCount number of neurons in RBF layer
	 * @param outputNeuronsCount number of neurons in output layer
	 * @return instance of RBF network
	 */
	public static RbfNetwork createRbfNetwork(int inputNeuronsCount,
			int rbfNeuronsCount, int outputNeuronsCount) {
		RbfNetwork nnet = new RbfNetwork(inputNeuronsCount, rbfNeuronsCount,
				outputNeuronsCount);
		return nnet;
	}

	/**
	 * Sets default input and output neurons for network (first layer as input,
	 * last as output)
	 */
	public static void setDefaultIO(NeuralNetwork nnet) {
		Vector<Neuron> inputNeurons = ((Layer) nnet.getLayers().firstElement())
				.getNeurons();
		Vector<Neuron> outputNeurons = ((Layer) nnet.getLayers().lastElement())
				.getNeurons();

		nnet.setInputNeurons(inputNeurons); // use first layer as input
		nnet.setOutputNeurons(outputNeurons); // use last layer as output
	}

}
