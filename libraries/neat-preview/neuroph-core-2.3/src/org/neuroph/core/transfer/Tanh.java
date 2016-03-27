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
 * Tanh neuron transfer function.
 * 
 * output = ( 1 - e^(-slope*input)) / ( 1 + e^(-slope*input) )
 * </pre>
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Tanh extends TransferFunction implements Serializable {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */		
	private static final long serialVersionUID = 2L;
	
	/**
	 * The slope parametetar of the Tanh function
	 */	
	private double slope = 1;

	/**
	 * Creates an instance of Tanh neuron transfer function with default
	 * slope=1.
	 */		
	public Tanh() {
	}

	/**
	 * Creates an instance of Tanh neuron transfer function with specified
	 * value for slope parametar.
	 * @param slope the slope parametar for the Tanh function
	 */	
	public Tanh(double slope) {
		this.slope = slope;
	}

	/**
	 * Creates an instance of Tanh neuron transfer function with the
	 * specified properties.
	 * @param properties properties of the Tanh function
	 */	
	public Tanh(Properties properties) {
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

	public double getOutput(double net) {
		double E_x = Math.exp(-this.slope * net);
		return (1 - E_x) / (1 + E_x);
	}
	
	@Override
	public double getDerivative(double net) {
		double out = getOutput(net);
		double derivative = (this.slope * (1 - out * out));
		return derivative;
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
	public void setSlope(double slope) {
		this.slope = slope;
	}
}
