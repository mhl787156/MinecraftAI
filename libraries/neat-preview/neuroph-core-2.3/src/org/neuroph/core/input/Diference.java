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

import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;

/**
 * Performs the vector difference operation on input and
 * weight vector.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Diference extends WeightsFunction implements Serializable {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */		
	private static final long serialVersionUID =21L;
	

	public Vector<Double> getOutput(Vector<Connection> inputConnections) {
		Vector<Double> inputVector = new Vector<Double>();

		for(Connection connection : inputConnections) {
			Neuron neuron = connection.getConnectedNeuron();
			
			Weight weight = connection.getWeight();
			double input = neuron.getOutput() - weight.getValue();
			
			inputVector.addElement(input);
		}

		return inputVector;
	}

}
