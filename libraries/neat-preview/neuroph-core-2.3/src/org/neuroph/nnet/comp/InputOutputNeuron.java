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
 * Provides behaviour specific for neurons which act as input and the output
 * neurons within the same layer. For example in Hopfield network and BAM.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class InputOutputNeuron extends Neuron {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;
		
	/**
	 * Flag which is set true if neuron external input is set
	 */
	private boolean externalInputSet;
		
	/**
	 * Bias value for this neuron
	 */	
	private double bias = 0;

	/**
	 * Creates an instance of neuron for Hopfield network
	 */
	public InputOutputNeuron() {
		super();
	}

	/**
	 * Creates an instance of neuron for Hopfield network with specified input
	 * and transfer functions
	 * @param inFunc neuron input function
	 * @param transFunc neuron transfer function
	 */
	public InputOutputNeuron(InputFunction inFunc, TransferFunction transFunc) {
		super(inFunc, transFunc);
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
	 * Returns bias value for this neuron
	 * @return bias value for this neuron
	 */	
	public double getBias() {
		return bias;
	}

	/**
	 * Sets bias value for this neuron
	 * @param bias bias value for this neuron
	 */	
	public void setBias(double bias) {
		this.bias = bias;
	}

	/**
	 * Calculates neuron output
	 */
	@Override
	public void calculate() {

		if (!externalInputSet) { // ako ulaz nije setovan spolja
			if (this.hasInputConnections()) // bias neuroni ne racunaju ulaz iz mreze jer
									// nemaju ulaze
				netInput = inputFunction.getOutput(this.inputConnections);
		}

		// calculqate cell output
		this.output = transferFunction.getOutput(this.netInput + bias); // izracunaj
																		// izlaz

		if (externalInputSet) { // ulaz setovan 'spolja' vazi samo za jedno izracunavanje
			externalInputSet = false;
			netInput = 0;
		}
	}

}
