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
 * Represents single training element for neural network learning.
 * This class contains only network input and it is used for unsupervised learning algorithms.
 * It is also the base class for SupervisedTrainingElement.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class TrainingElement implements Serializable {
	
	/**
	 * The class fingerprint that is set to indicate serialization 
	 * compatibility with a previous version of the class
	 */	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Input vector for this training element
	 */
	protected Vector<Double> input;

        /**
         * Label for this training element
         */
        protected String label;

	/**
	 * Creates new training element with specified input vector
	 */
	public TrainingElement() {
		this.input = new Vector<Double>();
	}

	/**
	 * Creates new training element with specified input vector
	 * 
	 * @param input
	 *            input vector
	 */
	public TrainingElement(Vector<Double> input) {
		this.input = input;
	}

	/**
	 * Creates new training element with specified input vector
	 * 
	 * @param input
	 */
	public TrainingElement(String input) {
		this.input = VectorParser.parseDouble(input);
	}

	/**
	 * Creates new training element with input array
	 *
	 * @param input
	 *            input array
	 */
	public TrainingElement(double ... input) {
        this.input = new Vector<Double>();
        for(int i=0; i<input.length; i++)
            this.input.add(new Double(input[i]));
	}

	/**
	 * Returns input vector
	 * 
	 * @return input vector
	 */
	public Vector<Double> getInput() {
		return this.input;
	}

	/**
	 * Sets input vector
	 * 
	 * @param input
	 *            input vector
	 */
	public void setInput(Vector<Double> input) {
		this.input = input;
	}

        /**
         * Get training element label
         * @return training element label
         */
        public String getLabel() {
            return label;
        }

        /**
         * Set training element label
         * @param label label for this training element
         */
        public void setLabel(String label) {
            this.label = label;
        }



}