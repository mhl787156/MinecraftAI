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

package org.neuroph.core;

/**
 * Neuron connection weight.
 * 
 * @see Connection
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Weight implements java.io.Serializable {
	/**
	 * The class fingerprint that is set to indicate serialization 
	 * compatibility with a previous version of the class
	 */	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Weight value
	 */
	private double value;
	
	/**
	 * Previous weight value (used by some learning rules like momentum for backpropagation)
	 */
	private transient double previousValue;	

	/**
	 * Creates an instance of connection weight with random weight value in range [0..1]
	 */
	public Weight() {
		this.value = Math.random() - 0.5;
		this.previousValue = this.value;
	}

	/**
	 * Creates an instance of connection weight with the specified weight value
	 * 
	 * @param value
	 *            weight value
	 */
	public Weight(double value) {
		this.value = value;
		this.previousValue = this.value;
	}

	/**
	 * Increases the weight for the specified amount
	 * 
	 * @param amount
	 *            amount to add to current weight value
	 */
	public void inc(double amount) {
		this.value += amount;
	}

	/**
	 * Decreases the weight for specified amount
	 * 
	 * @param amount
	 *            amount to subtract from the current weight value
	 */
	public void dec(double amount) {
		this.value -= amount;
	}

	/**
	 * Sets the weight value
	 * 
	 * @param value
	 *            weight value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * Returns weight value
	 * 
	 * @return value of this weight
	 */
	public double getValue() {
		return this.value;
	}
	
	
	/**
	 * Sets the previous weight value
	 * 
	 * @param previousValue
	 *            weight value to set
	 */
	public void setPreviousValue(double previousValue) {
		this.previousValue = previousValue;
	}
	
	/**
	 * Returns previous weight value
	 * 
	 * @return value of this weight
	 */
	public double getPreviousValue() {
		return this.previousValue;
	}	
	
	

	/**
	 * Returns weight value as String
	 */
	@Override
	public String toString() {
		return (new Double(value)).toString();
	}

	/**
	 * Sets random weight value
	 */
	public void randomize() {
		this.value = Math.random() - 0.5;
	}

}
