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

package org.neuroph.core.input;

import java.io.Serializable;
import java.util.Vector;

import org.neuroph.core.Connection;

/**
 *<pre>
 * Neuron's input function. It has two subcomponents:
 * 
 * weightsFunction - which performs operation with input and weight vector
 * summingFunction - which performs operation with the resulting vector from weightsFunction
 * 
 * InputFunction implements the following behaviour:
 * output = summingFunction(weightsFunction(inputs))
 * 
 * Different neuron input functions can be created by setting different weights and summing functions.
 *</pre>
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 * @see WeightsFunction
 * @see SummingFunction
 * @see org.neuroph.core.Neuron
 */
public class InputFunction implements Serializable {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 2L;
	
	/**
	 * Weights function component of the input function. It performs some
	 * operation with weights and input vector, and ouputs vector.
	 */
	private WeightsFunction weightsFunction;
	
	/**
	 * Summing function component of the input function. It performs some 
	 * summing operation on output vector from weightsFunction and outputs scalar.
	 */
	private SummingFunction summingFunction;

	/**
	 * Creates an instance of WeightedSum input function by default.
	 */
	public InputFunction() {
		this.weightsFunction = new WeightedInput();
		this.summingFunction = new Sum();
	}

	/**
	 * Creates an instance of input function with specified weights and summing function
	 * 
	 * @param weightsFunction
	 *            vector function performs some operation on input and weight
	 *            vector
	 * @param summingFunction
	 *            scalar function transforms output from VectorFunction to
	 *            scalar
	 */
	public InputFunction(WeightsFunction weightsFunction, SummingFunction summingFunction) {
		this.weightsFunction = weightsFunction;
		this.summingFunction = summingFunction;
	}

	/**
	 * Returns ouput value of this input function for the given neuron inputs
	 * 
	 * @param inputConnections
	 *            neuron's input connections
	 * @return input total net input
	 */
	public double getOutput(Vector<Connection> inputConnections) {
		Vector<Double> inputVector = this.weightsFunction.getOutput(inputConnections);
		double output = this.summingFunction.getOutput(inputVector);

		return output;
	}

	/**
	 * Returns summing function component of this InputFunction
	 * 
	 * @return summing function
	 */
	public SummingFunction getSummingFunction() {
		return summingFunction;
	}

	/**
	 * Returns weights functioncomponent of this InputFunction
	 * 
	 * @return weights function
	 */
	public WeightsFunction getWeightsFunction() {
		return weightsFunction;
	}

}
