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

/**
 * Abstract base class for all summing functions, which perform some summing
 * operation on weighted input vector and return scalar.
 * SummingFunctions is subcomponents of InputFunction.
 * @see org.neuroph.core.input.InputFunction
 * @author Zoran Sevarac <sevarac@gmail.com>
 */

abstract public class SummingFunction {

	/**
	 * Returns summing function output
	 * 
	 * @param inputVector
	 *            input vector for summing function
	 * @return summing function output
	 */
	abstract public double getOutput(Vector<Double> inputVector);

	@Override
	public String toString() {
		return getClass().getName();
	}	
}
