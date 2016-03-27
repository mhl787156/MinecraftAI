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

package org.neuroph.core;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

import org.neuroph.util.NeuronFactory;
import org.neuroph.util.NeuronProperties;

/**
 *<pre>
 * Layer of neurons in a neural network. The Layer is basic neuron container (a collection of neurons),
 * and it provides methods for manipulating neurons (add, remove, get, set, calculate, randomize).
 * </pre>
 * 
 * @see Neuron
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Layer implements Serializable {
	
	/**
	 * The class fingerprint that is set to indicate serialization 
	 * compatibility with a previous version of the class
	 */
	private static final long serialVersionUID = 2L;
	
	/**
	 * Reference to parent neural network
	 */
	private NeuralNetwork parentNetwork;

	/**
	 * Neurons collection
	 */
	protected Vector<Neuron> neurons;

	/**
	 *  Creates an instance of empty Layer
	 */
	public Layer() {
		this.neurons = new Vector<Neuron>();
	}

	/**
	 * Creates an instance of Layer with the specified number of neurons with
	 * specified neuron properties
	 */
	public Layer(int neuronsNum, NeuronProperties neuronProperties) {
		this();

		for (int i = 0; i < neuronsNum; i++) {
			Neuron neuron = NeuronFactory.createNeuron(neuronProperties);
			this.addNeuron(neuron);
		}
	}

	/**
	 * Sets reference on parent network
	 * 
	 * @param parent
	 *            parent network
	 */
	public void setParentNetwork(NeuralNetwork parent) {
		this.parentNetwork = parent;
	}

	/**
	 * Returns reference to parent network
	 * 
	 * @return reference on parent neural network
	 */
	public NeuralNetwork getParentNetwork() {
		return this.parentNetwork;
	}

	/**
	 * Returns interface for iterating neurons in this layer
	 * 
	 * @return interface for iterating neurons in this layer
	 */
	public Iterator<Neuron> getNeuronsIterator() {
		return this.neurons.iterator();
	}

	/**
	 * Returns collection of neurons in this layer
	 * 
	 * @return collection of neurons in this layer
	 */
	public Vector<Neuron> getNeurons() {
		return this.neurons;
	}

	/**
	 * Adds specified neuron to this layer
	 * 
	 * @param neuron
	 *            neuron to add
	 */
	public void addNeuron(Neuron neuron) {
		neuron.setParentLayer(this);
		this.neurons.add(neuron);
	}

	/**
	 * Adds specified neuron to this layer,at specified index position
	 * 
	 * @param neuron
	 *            neuron to add
	 * @param idx
	 *            index position at which neuron should be added
	 */
	public void addNeuron(int idx, Neuron neuron) {
		neuron.setParentLayer(this);
		this.neurons.add(idx, neuron);
	}

	/**
	 * Sets (replace) the neuron at specified position in layer
	 * 
	 * @param idx
	 *            index position to set/replace
	 * @param neuron
	 *            new Neuron object to set
	 */
	public void setNeuron(int idx, Neuron neuron) {
		neuron.setParentLayer(this);
		this.neurons.set(idx, neuron);
	}

	/**
	 * Removes neuron from layer
	 * 
	 * @param neuron
	 *            neuron to remove
	 */
	public void removeNeuron(Neuron neuron) {
		this.neurons.remove(neuron);
	}

	/**
	 * Removes neuron at specified index position in this layer
	 * 
	 * @param idx
	 *            index position of neuron to remove
	 */
	public void removeNeuronAt(int idx) {
		this.neurons.removeElementAt(idx);
	}

	/**
	 * Returns neuron at specified index position in this layer
	 * 
	 * @param idx
	 *            neuron index position
	 * @return neuron at specified index position
	 */
	public Neuron getNeuronAt(int idx) {
		return this.neurons.elementAt(idx);
	}

	/**
	 * Returns the index position in layer for the specified neuron
	 * 
	 * @param neuron
	 *            neuron object
	 * @return index position of specified neuron
	 */
	public int indexOf(Neuron neuron) {
		return this.neurons.indexOf(neuron);
	}

	/**
	 * Returns number of neurons in this layer
	 * 
	 * @return number of neurons in this layer
	 */
	public int getNeuronsCount() {
		return this.neurons.size();
	}

	/**
	 * Performs calculaton for all neurons in this layer
	 */
	public void calculate() {
		for(Neuron neuron : this.neurons) {
			neuron.calculate();
		}
	}

	/**
	 * Resets the activation and input levels for all neurons in this layer
	 */
	public void reset() {
		for(Neuron neuron : this.neurons) {
			neuron.reset();
		}		
	}

	/**
	 * Randomize input connection weights for all neurons in this layer
	 */
	public void randomizeWeights() {
		for(Neuron neuron : this.neurons) {
			neuron.randomizeInputWeights();
		}
	}

}
