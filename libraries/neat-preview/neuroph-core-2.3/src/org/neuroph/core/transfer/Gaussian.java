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
 * Gaussian neuron transfer function.
 *             -(x^2) / (2 * sigma^2)
 *  f(x) =    e
 * </pre>
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Gaussian extends TransferFunction implements Serializable {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */		
	private static final long serialVersionUID = 1L;
	
	/**
	 * The sigma parametetar of the gaussian function
	 */	
	private double sigma = 0.5;

	/**
	 * Creates an instance of Gaussian neuron transfer
	 */	
	public Gaussian() {
	}

	/**
	 * Creates an instance of Gaussian neuron transfer function with the
	 * specified properties.
	 * @param properties properties of the Gaussian function
	 */	
	public Gaussian(Properties properties) {
		try {
			this.sigma = Double.parseDouble(properties
					.getProperty("transferFunction.sigma"));
		} catch (NullPointerException e) {
			// if properties are not set just leave default values
		} catch (NumberFormatException e) {
			System.err
					.println("Invalid transfer function properties! Using default values.");
		}
	}

	public double getOutput(double net) {
		return Math.exp(-0.5 * Math.pow((net / this.sigma), 2));
	}
	
	@Override
	public double getDerivative(double net) {
		// TODO: check if this is correct
		double out = getOutput(net);
		double derivative = out * ( -net / (sigma*sigma) );
		return derivative;
	}	

	/**
	 * Returns the sigma parametar of this function
	 * @return  sigma parametar of this function 
	 */	
	public double getSigma() {
		return this.sigma;
	}

	/**
	 * Sets the sigma parametar for this function
	 * @param sigma value for the slope parametar
	 */	
	public void setSigma(double sigma) {
		this.sigma = sigma;
	}

}
