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

package org.neuroph.contrib;

import org.neuroph.core.Neuron;

/**
 * Neuron for Interactive Activation Neural Network.
 * @see org.neuroph.contrib.IACNetwork
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class IACNeuron extends Neuron {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Flag which is set true if neuron input is externaly set
	 */
	private boolean externalInputSet;


    public IACNeuron() {
        this.setTransferFunction(new IACFunction());
    }

	/**
	 * Sets total net input for this cell
	 *
	 * @param input
	 *            input value
	 */
	@Override
	public void setInput(double input) {
		this.netInput = input;
		this.externalInputSet = true;
	}

	/**
	 * Calculates neuron output
	 */
	@Override
	public void calculate() {
		if (!externalInputSet) { // ako ulaz nije setovan spolja
				netInput = inputFunction.getOutput(this.inputConnections);
		}

		// calculate neuron output
		this.output = ((IACFunction)transferFunction).getOutput(this.netInput, this.output);

		if (externalInputSet) { // ulaz setovan 'spolja' vazi samo za jedno izracunavanje
			externalInputSet = false;
			netInput = 0;
		}
	}

}
