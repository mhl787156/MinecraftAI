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

/**
 * Weighted connection to another neuron.
 * 
 * @see Weight
 * @see Neuron
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Connection implements Serializable {
	
	/**
	 * The class fingerprint that is set to indicate serialization 
	 * compatibility with a previous version of the class
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Connected neuron
	 */
	protected Neuron connectedNeuron;

	/**
	 * Weight for this connection
	 */
	protected Weight weight;

	/**
	 * Creates a new connection to specified neuron with random weight
	 * 
	 * @param connectTo
	 *            neuron to connect to
	 */
	public Connection(Neuron connectTo) {
		this.connectedNeuron = connectTo;
		this.weight = new Weight();
	}

	/**
	 * Creates a new connection to specified neuron with specified weight object
	 * 
	 * @param connectTo
	 *            neuron to connect to
	 * @param weight
	 *            weight for this connection
	 */
	public Connection(Neuron connectTo, Weight weight) {
		this.connectedNeuron = connectTo;
		this.weight = weight;
	}

	/**
	 * Creates a new connection to specified neuron with specified weight value
	 * 
	 * @param connectTo
	 *            neuron to connect to
	 * @param weightVal
	 *            weight value for this connection
	 */
	public Connection(Neuron connectTo, double weightVal) {
		this.connectedNeuron = connectTo;
		this.weight = new Weight(weightVal);
	}

	/**
	 * Creates a new connection between specified neurons with random weight value
	 * 
	 * @param from
	 *            neron to connect
	 * @param connectTo
	 *            neuron to connect to
	 */
	public Connection(Neuron from, Neuron connectTo) {
		this.connectedNeuron = connectTo;
		this.weight = new Weight();
		from.addInputConnection(this);
	}

	/**
	 * Returns weight for this connection
	 * 
	 * @return weight for this connection
	 */
	public Weight getWeight() {
		return this.weight;
	}

	/**
	 * Returns the connected neuron of this connection
	 * 
	 * @return connected neuron of this connection
	 */
	public Neuron getConnectedNeuron() {
		return this.connectedNeuron;
	}

	/**
	 * Returns input received through this connection - the activation that
	 * comes from the output of the cell on the other end of connection
	 * 
	 * @return input received through this connection
	 */
	public double getInput() {
		return this.connectedNeuron.getOutput();
	}

	/**
	 * Returns the weighted input received through this connection
	 * 
	 * @return weighted input received through this connection
	 */
	public double getWeightedInput() {
		return getInput() * weight.getValue();
	}

}
