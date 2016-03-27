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

import java.io.Serializable;
import java.util.Vector;

/**
 * Performs logic AND operation on input vector.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class And extends SummingFunction implements Serializable {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param inputVector Input values >= 0.5d are considered true, otherwise false.
	 */
	public double getOutput(Vector<Double> inputVector) {
		boolean res = true;
		
		for(Double input : inputVector) {
			res = res && (input >= 0.5);
		}

		return res ? 1d : 0d;
	}

}
