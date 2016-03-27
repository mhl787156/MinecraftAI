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

import org.neuroph.core.Neuron;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.transfer.TransferFunction;

/**
 * Provides behaviour for neurons with delayed output.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class DelayedNeuron extends Neuron {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Output history for this neuron
	 */
	protected Vector<Double> outputHistory;

	/**
	 * Creates an instance of neuron which can delay output
	 * @param inputFunction neuron input function
	 * @param transferFunction neuron transfer function
	 */
	public DelayedNeuron(InputFunction inputFunction,
			TransferFunction transferFunction) {
		super(inputFunction, transferFunction);
		outputHistory = new Vector<Double>(5); // default delay buffer size is 5
		outputHistory.add(new Double(0));
	}

	@Override
	public void calculate() {
		super.calculate();
		outputHistory.add(0, new Double(this.output));
		if (outputHistory.size() > 5)
			outputHistory.removeElementAt(5);
	}

	/**
	 * Returns neuron output with the specified delay
	 * @param delay output delay
	 * @return neuron output at (t-delay) moment
	 */
	public double getOutput(int delay) {
		return outputHistory.get(delay).doubleValue();
	}

}
