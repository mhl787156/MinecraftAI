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
 * Linear neuron transfer function.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Linear extends TransferFunction implements Serializable {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */		
	private static final long serialVersionUID = 1L;
	
	/**
	 * The slope parametetar of the linear function
	 */	
	private double slope = 1;

	/**
	 * Creates an instance of Linear transfer function
	 */	
	public Linear() {
	}

	/**
	 * Creates an instance of Linear transfer function with specified value
	 * for getSlope parametar.
	 */	
	public Linear(double slope) {
		this.slope = slope;
	}

	/**
	 * Creates an instance of Linear transfer function with specified properties
	 */		
	public Linear(Properties properties) {
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
	public void setSlope(double slope) {
		this.slope = slope;
	}

	public double getOutput(double net) {
		return slope * net;
	}

	@Override
	public double getDerivative(double net) {
		return this.slope;
	}
}
