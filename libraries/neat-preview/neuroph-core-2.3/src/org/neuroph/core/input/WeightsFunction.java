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

import java.util.Vector;

import org.neuroph.core.Connection;

/**
 * Abstract base class for all weights functions, which perform some operation on
 * neuron's input vector and weights vector and return vector.
 * WeightsFunction is subcomponents of InputFunction.
 * @see org.neuroph.core.input.InputFunction
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
abstract public class WeightsFunction {

	/**
	 * Returns function's output
	 * 
	 * @param inputs
	 *            neuron's input connections
	 * @return function's output
	 */
	abstract public Vector<Double> getOutput(Vector<Connection> inputs);
	
	@Override
	public String toString() {
		return getClass().getName();
	}	

}
