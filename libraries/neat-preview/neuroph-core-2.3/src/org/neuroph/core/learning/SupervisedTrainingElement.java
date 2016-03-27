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

package org.neuroph.core.learning;

import java.io.Serializable;
import java.util.Vector;

import org.neuroph.util.VectorParser;

/**
 * Represents training element for supervised learning algorithms.
 * Each supervised training element contains network input and desired network output.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class SupervisedTrainingElement extends TrainingElement implements
		Serializable {

	/**
	 * The class fingerprint that is set to indicate serialization 
	 * compatibility with a previous version of the class
	 */	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Desired output for this training element
	 */
	private Vector<Double> desiredOutput;

	/**
	 * Creates new training element with specified input and desired output
	 * vectors
	 * 
	 * @param input
	 *            input vector
	 * @param desiredOutput
	 *            desired output vector
	 */
	public SupervisedTrainingElement(Vector<Double> input, Vector<Double> desiredOutput) {
		super(input);
		this.desiredOutput = desiredOutput;
	}

	/**
	 * Creates new training element with specified input and desired output
	 * vectors specifed as strings
	 * 
	 * @param input
	 *            input vector as space separated string
	 * @param desiredOutput
	 *            desired output vector as space separated string
	 */
	public SupervisedTrainingElement(String input, String desiredOutput) {
		super(input);
		this.desiredOutput = VectorParser.parseDouble(desiredOutput);
	}

	/**
	 * Creates new training element with specified input and desired output
	 * vectors
	 *
	 * @param input
	 *            input array
	 * @param desiredOutput
	 *            desired output array
	 */
	public SupervisedTrainingElement(double[] input, double[] desiredOutput) {
		super(input);

        this.desiredOutput = new Vector<Double>();
        for(int i=0; i<desiredOutput.length; i++)
            this.desiredOutput.add(new Double(desiredOutput[i]));
	}

	/**
	 * Returns desired output for this training element
	 * 
	 * @return desired output vector
	 */
	public Vector<Double> getDesiredOutput() {
		return this.desiredOutput;
	}

	/**
	 * Sets desired output vector for this training element
	 * 
	 * @param desiredOutput
	 *            desired output vector
	 */
	public void setDesiredOutput(Vector<Double> desiredOutput) {
		this.desiredOutput = desiredOutput;
	}
}