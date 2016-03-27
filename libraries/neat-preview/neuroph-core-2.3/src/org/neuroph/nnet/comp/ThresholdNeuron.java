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

import org.neuroph.core.Neuron;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.transfer.TransferFunction;

/**
 * Provides behaviour for neurons with threshold.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class ThresholdNeuron extends Neuron {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */		
	private static final long serialVersionUID = 1L;
	
	/**
	 * Threshold value for this neuron
	 */
	protected double thresh = 0;

	/**
	 * Creates a neuron with threshold behaviour, and with the specified input
	 * and transfer functions.
	 * 
	 * @param inputFunction
	 *            input function for this neuron
	 * @param transferFunction
	 *            transfer function for this neuron
	 */
	public ThresholdNeuron(InputFunction inputFunction, TransferFunction transferFunction) {
		this.inputFunction = inputFunction;
		this.transferFunction = transferFunction;
	}

	/**
	 * Calculates neuron's output
	 */
	@Override
	public void calculate() {
		if (this.hasInputConnections()) {
			this.netInput = this.inputFunction.getOutput(this.inputConnections);
		}

		if (this.netInput > this.thresh) {
			this.output = this.transferFunction.getOutput(this.netInput);
		} else {
			this.output = 0;
		}
	}

	/**
	 * Returns threshold value for this neuron
	 * @return threshold value for this neuron
	 */
	public double getThresh() {
		return thresh;
	}

	/**
	 * Sets threshold value for this neuron
	 * @param thresh threshold value for this neuron
	 */
	public void setThresh(double thresh) {
		this.thresh = thresh;
	}

}
