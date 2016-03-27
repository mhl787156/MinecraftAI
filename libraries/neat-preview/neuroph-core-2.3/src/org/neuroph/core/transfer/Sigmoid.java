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

package org.neuroph.core.transfer;

import java.io.Serializable;
import java.util.Properties;

/**
 * <pre>
 * Sigmoid neuron transfer function.
 * 
 * output = 1/(1+ e^(-slope*input))
 * </pre>
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Sigmoid extends TransferFunction implements Serializable {
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */		
	private static final long serialVersionUID = 2L;
	
	/**
	 * The slope parametetar of the sigmoid function
	 */
	private double slope = 1;

	/**
	 * Creates an instance of Sigmoid neuron transfer function with default
	 * slope=1.
	 */	
	public Sigmoid() {
	}

	/**
	 * Creates an instance of Sigmoid neuron transfer function with specified
	 * value for slope parametar.
	 * @param slope the slope parametar for the sigmoid function
	 */
	public Sigmoid(double slope) {
		this.slope = slope;
	}

	/**
	 * Creates an instance of Sigmoid neuron transfer function with the
	 * specified properties.
	 * @param properties properties of the sigmoid function
	 */	
	public Sigmoid(Properties properties) {
		try {
			this.slope = Double.parseDouble(properties
					.getProperty("transferFunction.slope"));
		} catch (NullPointerException e) {
			// if properties are not set just leave default values
		} catch (NumberFormatException e) {
			System.err
					.println("Invalid transfer function properties! Using default values.");
		}
	}
	
	/**
	 * Returns the slope parametar of this function
	 * @return  slope parametar of this function 
	 */
	public double getSlope() {
		return this.slope;
	}

	/**
	 * Sets the slope parametar for this function
	 * @param slope value for the slope parametar
	 */
	public void setSlope(Double slope) {
		this.slope = slope.doubleValue();
	}

	
	public double getOutput(double net) {
		double den = (double) (1 + Math.exp(-this.slope * net)); // imenioc
		return (1 / den);
	}

	@Override
	public double getDerivative(double net) {
		double out = getOutput(net);
		double derivative = this.slope * out * (1 - out);
		return derivative;
	}

}
