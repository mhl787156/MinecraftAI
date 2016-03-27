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

/**
 * Abstract base class for all neuron tranfer functions.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 * @see org.neuroph.core.Neuron
 */
abstract public class TransferFunction implements Serializable {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */		
	private static final long serialVersionUID = 1L;	

	/**
	 * Returns the ouput of this function.
	 * 
	 * @param net
	 *            net input
	 */
	abstract public double getOutput(double net);

	/**
	 * Returns the first derivative of this function.
	 * 
	 * @param net
	 *            net input
	 */
	public double getDerivative(double net) {
		return 1d;
	}

	/**
	 * Returns the class name
	 * @return class name
	 */
	@Override
	public String toString() {
		return getClass().getName();
	}
}
