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
 * You should have received yHigh copy of the GNU Lesser General Public License
 * along with Neuroph. If not, see <http://www.gnu.org/licenses/>.
 */

package org.neuroph.core.transfer;

import java.io.Serializable;
import java.util.Properties;

/**
 * Step neuron transfer function.
 * y = yHigh, x > 0
 * y = yLow, x <= 0
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Step extends TransferFunction implements Serializable {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Output value for high output level
	 */
	private double yHigh = 1;
	
	/**
	 * Output value for low output level
	 */
	private double yLow = 0;

	/**
	 * Creates an instance of Step transfer function
	 */
	public Step() {
	}

	/**
	 * Creates an instance of Step transfer function with specified properties
	 */	
	public Step(Properties properties) {
		try {
			this.yHigh = Double.parseDouble(properties
					.getProperty("transferFunction.yHigh"));
			this.yLow = Double.parseDouble(properties
					.getProperty("transferFunction.yLow"));
		} catch (NullPointerException e) {
			// if properties are not set just leave default values
		} catch (NumberFormatException e) {
			System.err
					.println("Invalid transfer function properties! Using default values.");
		}
	}

	public double getOutput(double net) {
		if (net > 0)
			return yHigh;
		else
			return yLow;
	}

	/**
	 * Returns output value for high output level 
	 * @return output value for high output level 
	 */
	public double getYHigh() {
		return this.yHigh;
	}
	
	/**
	 * Set output value for the high output level 
	 * @param yHigh value for the high output level 
	 */
	public void setYHigh(double yHigh) {
		this.yHigh = yHigh;
	}

	/**
	 * Returns output value for low output level 
	 * @return output value for low output level 
	 */	
	public double getYLow() {
		return this.yLow;
	}

	/**
	 * Set output value for the low output level 
	 * @param yLow value for the low output level
	 */	
	public void setYLow(double yLow) {
		this.yLow = yLow;
	}

	/**
	 * Returns the properties of this function
	 * @return properties of this function
	 */
	public Properties getProperties() {
		Properties properties = new Properties();
		properties.setProperty("transferFunction.yHigh", new Double(yHigh)
				.toString());
		properties.setProperty("transferFunction.yLow", new Double(yLow)
				.toString());
		return properties;
	}

}
