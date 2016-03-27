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
 * Fuzzy trapezoid neuron tranfer function.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Trapezoid extends TransferFunction implements Serializable {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */		
	private static final long serialVersionUID = 1L;
	
	// these are the points of trapezoid function
	double leftLow, leftHigh, rightLow, rightHigh;


	/**
	 * Creates an instance of Trapezoid transfer function
	 */	
	public Trapezoid() {
		this.leftLow = 0;
		this.leftHigh = 1;
		this.rightLow = 3;
		this.rightHigh = 2;
	}

	/**
	 * Creates an instance of Trapezoid transfer function with the specified
	 * setting.
	 */	
	public Trapezoid(double leftLow, double leftHigh, double rightLow, double rightHigh) {
		this.leftLow = leftLow;
		this.leftHigh = leftHigh;
		this.rightLow = rightLow;
		this.rightHigh = rightHigh;
	}

	/**
	 * Creates an instance of Trapezoid transfer function with the specified
	 * properties.
	 */		
	public Trapezoid(Properties properties) {
		try {
			this.leftLow = Double.parseDouble(properties
					.getProperty("transferFunction.leftLow"));
			this.leftHigh = Double.parseDouble(properties
					.getProperty("transferFunction.leftHigh"));
			this.rightLow = Double.parseDouble(properties
					.getProperty("transferFunction.rightLow"));
			this.rightHigh = Double.parseDouble(properties
					.getProperty("transferFunction.rightHigh"));
		} catch (NullPointerException e) {
			// if properties are not set just leave default values
		} catch (NumberFormatException e) {
			System.err
					.println("Invalid transfer function properties! Using default values.");
		}
	}

	public double getOutput(double net) {
		if ((net >= leftHigh) && (net <= rightHigh)) {
			return 1;
		} else if ((net > leftLow) && (net < leftHigh)) {
			return (net - leftLow) / (leftHigh - leftLow);
		} else if ((net > rightHigh) && (net < rightLow)) {
			return (rightLow - net) / (rightLow - rightHigh);
		}

		return 0;
	}

	/**
	 * Sets left low point of trapezoid function
	 * @param leftLow left low point of trapezoid function
	 */
	public void setLeftLow(double leftLow) {
		this.leftLow = leftLow;
	}

	/**
	 * Sets left high point of trapezoid function
	 * @param leftHigh left high point of trapezoid function
	 */	
	public void setLeftHigh(double leftHigh) {
		this.leftHigh = leftHigh;
	}

	/**
	 * Sets right low point of trapezoid function
	 * @param rightLow right low point of trapezoid function
	 */	
	public void setRightLow(double rightLow) {
		this.rightLow = rightLow;
	}

	/**
	 * Sets right high point of trapezoid function
	 * @param rightHigh right high point of trapezoid function
	 */	
	public void setRightHigh(double rightHigh) {
		this.rightHigh = rightHigh;
	}

	/**
	 * Returns left low point of trapezoid function
	 * @return left low point of trapezoid function
	 */
	public double getLeftLow() {
		return leftLow;
	}

	/**
	 * Returns left high point of trapezoid function
	 * @return left high point of trapezoid function
	 */	
	public double getLeftHigh() {
		return leftHigh;
	}

	/**
	 * Returns right low point of trapezoid function
	 * @return right low point of trapezoid function
	 */	
	public double getRightLow() {
		return rightLow;
	}

	/**
	 * Returns right high point of trapezoid function
	 * @return right high point of trapezoid function
	 */		
	public double getRightHigh() {
		return rightHigh;
	}

}
