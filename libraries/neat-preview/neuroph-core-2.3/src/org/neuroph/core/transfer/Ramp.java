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
 * Ramp neuron transfer function.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Ramp extends TransferFunction implements Serializable {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;
	
	/**
	 * The slope parametetar of the ramp function
	 */	
	private double slope = 1;
	
	/**
	 * Threshold for the high output level
	 */	
	private double xHigh = 1;
	
	/**
	 * Threshold for the low output level
	 */	
	private double xLow = 0;
	
	/**
	 * Output value for the high output level
	 */	
	private double yHigh = 1;
	
	/**
	 * Output value for the low output level
	 */	
	private double yLow = 0;

	/**
	 * Creates an instance of Ramp transfer function with default settings
	 */	
	public Ramp() {
	}

	/**
	 * Creates an instance of Ramp transfer function with specified settings
	 */	
	public Ramp(double slope, double xLow, double xHigh, double yLow,
			double yHigh) {
		this.slope = slope;
		this.xLow = xLow;
		this.xHigh = xHigh;
		this.yLow = yLow;
		this.yHigh = yHigh;
	}

	/**
	 * Creates an instance of Ramp transfer function with specified properties.
	 */		
	public Ramp(Properties properties) {
		try {
			this.slope = Double.parseDouble(properties
					.getProperty("transferFunction.slope"));
			this.yHigh = Double.parseDouble(properties
					.getProperty("transferFunction.yHigh"));
			this.yLow = Double.parseDouble(properties
					.getProperty("transferFunction.yLow"));
			this.xHigh = Double.parseDouble(properties
					.getProperty("transferFunction.xHigh"));
			this.xLow = Double.parseDouble(properties
					.getProperty("transferFunction.xLow"));
		} catch (NullPointerException e) {
			// if properties are not set just leave default values
		} catch (NumberFormatException e) {
			System.err
					.println("Invalid transfer function properties! Using default values.");
		}
	}

	public double getOutput(double net) {
		if (net < this.xLow)
			return this.yLow;
		else if (net > this.xHigh)
			return this.yHigh;
		else
			return (double) (slope * net);
	}

	/**
	 * Returns threshold value for the low output level 
	 * @return threshold value for the low output level 
	 */
	public double getXLow() {
		return this.xLow;
	}

	/**
	 * Sets threshold for the low output level 
	 * @param x threshold value for the low output level
	 */		
	public void setXLow(double x) {
		this.xLow = x;
	}

	/**
	 * Returns threshold value for the high output level 
	 * @return threshold value for the high output level 
	 */		
	public double getXHigh() {
		return this.xHigh;
	}

	/**
	 * Sets threshold for the high output level 
	 * @param x threshold value for the high output level
	 */	
	public void setXHigh(double x) {
		this.xHigh = x;
	}

	/**
	 * Returns output value for low output level 
	 * @return output value for low output level 
	 */		
	public double getYLow() {
		return this.yLow;
	}

	/**
	 * Sets output value for the low output level 
	 * @param y value for the low output level 
	 */		
	public void setYLow(double y) {
		this.yLow = y;
	}

	/**
	 * Returns output value for high output level 
	 * @return output value for high output level 
	 */	
	public double getYHigh() {
		return this.yHigh;
	}

	/**
	 * Sets output value for the high output level 
	 * @param y value for the high output level 
	 */	
	public void setYHigh(double y) {
		this.yHigh = y;
	}

}