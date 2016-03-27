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
import java.util.Iterator;
import java.util.Vector;

import org.neuroph.core.NeuralNetwork;

/**
 * Base class for all unsupervised learning algorithms.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
abstract public class UnsupervisedLearning extends IterativeLearning implements
		Serializable {
	
	/**
	 * The class fingerprint that is set to indicate serialization 
	 * compatibility with a previous version of the class
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new unsupervised learning rule
	 */
	public UnsupervisedLearning() {
		super();
	}

	/**
	 * Creates new unsupervised learning rule and sets the neural network to train
	 * @param neuralNetwork
	 *            neural network to train
	 */
	public UnsupervisedLearning(NeuralNetwork neuralNetwork) {
		super(neuralNetwork);
	}

	/**
	 * This method does one learning epoch for the unsupervised learning rules.
	 * It iterates through the training set and trains network weights for each
	 * element
	 * 
	 * @param trainingSet
	 *            training set for training network
	 */
	public void doLearningEpoch(TrainingSet trainingSet) {
		Iterator<TrainingElement> iterator = trainingSet.iterator();
		while (iterator.hasNext() && !isStopped()) {
			TrainingElement trainingElement = iterator.next();
			learnPattern(trainingElement);
		}
	}	
	
	/**
	 * Trains network with the pattern from the specified training element
	 * 
	 * @param trainingElement
	 *            unsupervised training element which contains network input
	 */
	protected void learnPattern(TrainingElement trainingElement) {
		Vector<Double> input = trainingElement.getInput();
		this.neuralNetwork.setInput(input);
		this.neuralNetwork.calculate();
		this.adjustWeights();
	}



	/**
	 * This method implements the weight adjustment
	 */
	abstract protected void adjustWeights();

}