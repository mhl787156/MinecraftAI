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

package org.neuroph.nnet.comp;

import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;

/**
 * Represents the connection between neurons which can delay signal.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class DelayedConnection extends Connection {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Delay factor for this conection
	 */
	private int delay = 0;

	/**
	 * Creates an instance of delayed connection to cpecified neuron and
	 * with specified weight
	 * @param connectTo neuron to connect ti
	 * @param weightVal weight value for the connection
	 * @param delay delay for the connection
	 */
	public DelayedConnection(Neuron connectTo, double weightVal, int delay) {
		super(connectTo, weightVal);
		this.delay = delay;
	}

	/**
	 * Returns delay value for this connection
	 * @return delay value for this connection
	 */
	public int getDelay() {
		return this.delay;
	}

	/**
	 * Sets delay value for this connection
	 * @param delay value for this connection
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * Gets delayed input through this connection
	 * @return delayed output from connected neuron
	 */
	@Override
	public double getInput() {
		if (this.connectedNeuron instanceof DelayedNeuron)
			return ((DelayedNeuron) this.connectedNeuron).getOutput(delay);
		else
			return this.connectedNeuron.getOutput();
	}

}
