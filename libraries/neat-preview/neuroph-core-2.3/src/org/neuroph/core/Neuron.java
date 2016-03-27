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

import org.neuroph.core.input.InputFunction;
import org.neuroph.core.transfer.Step;
import org.neuroph.core.transfer.TransferFunction;

/**
 *<pre>
 * Basic general neuron model according to McCulloch-Pitts neuron model.
 * Different neuron models can be created by using different input and transfer functions for instances of this class,
 * or by deriving from this class. The neuron is basic processing element of neural network.
 * This class implements the following behaviour:
 *  
 * output = transferFunction( inputFunction(inputConnections) )
 *</pre>
 * 
 * @see InputFunction
 * @see TransferFunction
 * @author Zoran Sevarac <sevarac@gmail.com>
 */

public class Neuron implements Serializable {
	/**
	 * The class fingerprint that is set to indicate serialization 
	 * compatibility with a previous version of the class
	 */	
	private static final long serialVersionUID = 3L;

	/**
	 * Parent layer for this neuron
	 */
	protected Layer parentLayer;

	/**
	 * Collection of neuron's input connections (connections to this neuron)
	 */
	protected Vector<Connection> inputConnections = new Vector<Connection>();

	/**
	 * Collection of neuron's output connections (connections from this to other
	 * neurons)
	 */
	protected Vector<Connection> outConnections = new Vector<Connection>();

	/**
	 * Total net input for this neuron. Represents total input for this neuron
	 * received from input function.
	 */
	protected transient double netInput = 0;

	/**
	 * Neuron output
	 */
	protected transient double output = 0;

	/**
	 * Local error for this neuron
	 */
	protected transient double error = 0;

	/**
	 * Input function for this neuron
	 */
	protected InputFunction inputFunction;

	/**
	 * Transfer function for this neuron
	 */
	protected TransferFunction transferFunction;

	/**
	 * Creates an instance of Neuron with the weighted sum, input function 
	 * and Step transfer function. This is the original McCulloch-Pitts 
	 * neuron model.
	 */
	public Neuron() {
		this.inputFunction = new InputFunction();
		this.transferFunction = new Step();
	}

	/**
	 * Creates an instance of Neuron with the specified input and transfer functions.
	 * 
	 * @param inputFunction
	 *            input function for this neuron
	 * @param transferFunction
	 *            transfer function for this neuron
	 */
	public Neuron(InputFunction inputFunction, TransferFunction transferFunction) {
		this.inputFunction = inputFunction;
		this.transferFunction = transferFunction;
	}

	/**
	 * Calculates neuron's output
	 */
	public void calculate() {
		if (this.hasInputConnections()) {
			this.netInput = this.inputFunction.getOutput(this.inputConnections);
		}
		this.output = this.transferFunction.getOutput(this.netInput);
	}

	/**
	 * Sets input and output activation levels to zero
	 */
	public void reset() {
		this.setInput(0);
		this.setOutput(0);
	}

	/**
	 * Sets neuron's input
	 * 
	 * @param input
	 *            input value to set
	 */
	public void setInput(double input) {
		this.netInput = input;
	}

	/**
	 * Returns total net input
	 * 
	 * @return total net input
	 */
	public double getNetInput() {
		return this.netInput;
	}

	/**
	 * Returns neuron's output
	 * 
	 * @return neuron output
	 */
	public double getOutput() {
		return this.output;
	}

	/**
	 * Returns true if there are input connections for this neuron, false
	 * otherwise
	 * 
	 * @return true if there is input connection, false otherwise
	 */
	public boolean hasInputConnections() {
		return (!this.inputConnections.isEmpty());
	}

	/**
	 * Returns Iterator interface for accessing input connections
	 * 
	 * @return iterator interface for accessing input connections
	 */
	public Iterator<Connection> getInputsIterator() {
		return this.inputConnections.iterator();
	}

	/**
	 * Adds the specified input connection
	 * 
	 * @param connection
	 *            input connection to add
	 */
	public void addInputConnection(Connection connection) {
		this.inputConnections.add(connection);
		Neuron connected = connection.getConnectedNeuron();
		Connection backConnection = new Connection(this, connection.getWeight());
		connected.addOutputConnection(backConnection);
	}

	/**
	 * Adds input connection with the given weight, from given neuron
	 * 
	 * @param fromNeuron
	 *            neuron to connect from
	 * @param weightVal
	 *	      connection weight value
	 * 
	 */	
	public void addInputConnection(Neuron fromNeuron, double weightVal) {
		Connection connection = new Connection(fromNeuron, weightVal);
		this.addInputConnection(connection);
	}

	/**
	 * Adds the specified output connection
	 * 
	 * @param connection output connection to add
	 */
	protected void addOutputConnection(Connection connection) {
		this.outConnections.add(connection);
	}

	/**
	 * Returns input connections for this neuron as Vector collection
	 * 
	 * @return input connections of this neuron
	 */
	public Vector<Connection> getInputConnections() {
		return inputConnections;
	}

	/**
	 * Returns output connections from this neuron
	 * 
	 * @return output connections from this neuron
	 */
	public Vector<Connection> getOutConnections() {
		return outConnections;
	}

	/**
	 * Removes input connection which is connected to specified neuron
	 * 
	 * @param fromNeuron
	 *            neuron which is connected as input
	 */
	public void removeInputConnectionFrom(Neuron fromNeuron) {
		for(Connection connection : this.inputConnections) {
			if (connection.getConnectedNeuron() == fromNeuron) {
				this.inputConnections.remove(connection);
				return;
			}
		}				
	}

	/**
	 * Gets input connection from the specified neuron * @param fromNeuron
	 * neuron connected to this neuron as input
	 */
	public Connection getConnectionFrom(Neuron fromNeuron) {	
		for(Connection connection : this.inputConnections) {
			if (connection.getConnectedNeuron() == fromNeuron)
				return connection;		
		}
		return null;
	}

	/**
	 * Sets input function
	 * 
	 * @param inputFunction
	 *            input function to set for this neuron
	 */
	public void setInputFunction(InputFunction inputFunction) {
		this.inputFunction = inputFunction;
	}

	/**
	 * Sets transfer function
	 * 
	 * @param transferFunction
	 *            transfer function for this neuron
	 */
	public void setTransferFunction(TransferFunction transferFunction) {
		this.transferFunction = transferFunction;
	}

	/**
	 * Returns input function
	 * 
	 * @return input function
	 */
	public InputFunction getInputFunction() {
		return this.inputFunction;
	}

	/**
	 * Returns transfer function
	 * 
	 * @return transfer function
	 */
	public TransferFunction getTransferFunction() {
		return this.transferFunction;
	}

	/**
	 * Sets reference to parent layer for this neuron (layer in which the neuron
	 * is located)
	 * 
	 * @param parent
	 *            reference on layer in which the cell is located
	 */
	public void setParentLayer(Layer parent) {
		this.parentLayer = parent;
	}

	/**
	 * Returns reference on parent layer for this neuron
	 * 
	 * @return parent layer for this neuron
	 */
	public Layer getParentLayer() {
		return this.parentLayer;
	}

	/**
	 * Returns weights vector of input connections
	 * 
	 * @return weights vector of input connections
	 */
	public Vector<Weight> getWeightsVector() {
		Vector<Weight> weights = new Vector<Weight>();
		for(Connection connection : this.inputConnections) {
			Weight weight = connection.getWeight();
			weights.addElement(weight);			
		}		
		return weights;
	}

	/**
	 * Returns error for this neuron. This is used by supervised learing rules.
	 * 
	 * @return error for this neuron which is set by learning rule
	 */
	public double getError() {
		return error;
	}

	/**
	 * Sets error for this neuron. This is used by supervised learing rules.
	 * 
	 * @param error
	 *            neuron error
	 */
	public void setError(double error) {
		this.error = error;
	}

	/**
	 * Sets this neuron output
	 * 
	 * @param output
	 *            value to set
	 */
	public void setOutput(double output) {
		this.output = output;
	}

	/**
	 * Randomize all input weights
	 */
	public void randomizeInputWeights() {
		for(Connection connection : this.inputConnections) {
			connection.getWeight().randomize();
		}		
	}

}
