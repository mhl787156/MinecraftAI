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

import java.util.Vector;

import org.neuroph.core.Connection;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.transfer.TransferFunction;

/**
 * Provides neuron behaviour specific for competitive neurons which are used in
 * competitive layers, and networks with competitive learning.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class CompetitiveNeuron extends DelayedNeuron {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */		
	private static final long serialVersionUID = 1L;
	
	/**
	 * Flag indicates if this neuron is in competing mode
	 */
	private boolean isCompeting = false;
	
	/**
	 * Collection of conections from neurons in other layers
	 */
	private Vector<Connection> connectionsFromOtherLayers;
	
	/**
	 * Collection of connections from neurons in the same layer as this neuron
	 * (lateral connections used for competition)
	 */
	private Vector<Connection> connectionsFromThisLayer;

	/**
	 * Creates an instance of CompetitiveNeuron with specified input and transfer functions
	 * @param inputFunction neuron input function
	 * @param transferFunction neuron ransfer function
	 */
	public CompetitiveNeuron(InputFunction inputFunction, TransferFunction transferFunction) {
		super(inputFunction, transferFunction);
		connectionsFromOtherLayers = new Vector<Connection>();
		connectionsFromThisLayer = new Vector<Connection>();
		this.addInputConnection(this, 1);
	}

	@Override
	public void calculate() {
		if (this.isCompeting) {
			// get input only from neurons in this layer
			this.netInput = this.inputFunction
					.getOutput(this.connectionsFromThisLayer);
		} else {
			// get input from other layers
			this.netInput = this.inputFunction
					.getOutput(this.connectionsFromOtherLayers);
			this.isCompeting = true;
		}

		this.output = this.transferFunction.getOutput(this.netInput);
		outputHistory.add(0, new Double(this.output));
	}

	/**
	 * Adds input connection for this competitive neuron
	 * @param connection input connection
	 */
	@Override
	public void addInputConnection(Connection connection) {
		super.addInputConnection(connection);
		if (connection.getConnectedNeuron().getParentLayer() == this
				.getParentLayer()) {
			connectionsFromThisLayer.add(connection);
		} else {
			connectionsFromOtherLayers.add(connection);
		}
	}

	/**
	 * Returns collection of connections from other layers
	 * @return collection of connections from other layers
	 */
	public Vector<Connection> getConnectionsFromOtherLayers() {
		return connectionsFromOtherLayers;
	}

	/**
	 * Resets the input, output and mode for this neuron
	 */
	@Override
	public void reset() {
		super.reset();
		this.isCompeting = false;
	}

	/**
	 * Retruns true if this neuron is in competing mode, false otherwise
	 * @return true if this neuron is in competing mode, false otherwise
	 */
	public boolean isCompeting() {
		return isCompeting;
	}

	/**
	 * Sets the flag to indicate that this neuron is in competing mode
	 * @param isCompeting value for the isCompeting flag
	 */
	public void setIsCompeting(boolean isCompeting) {
		this.isCompeting = isCompeting;
	}

}
