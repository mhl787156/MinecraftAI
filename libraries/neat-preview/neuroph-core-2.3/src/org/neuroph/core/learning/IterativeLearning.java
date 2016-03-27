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

import org.neuroph.core.NeuralNetwork;

/**
 * Base class for all iterative learning algorithms.
 * It provides the iterative learning procedure for all of its subclasses.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
abstract public class IterativeLearning extends LearningRule implements
		Serializable {

	/**
	 * The class fingerprint that is set to indicate serialization 
	 * compatibility with a previous version of the class
	 */		
	private static final long serialVersionUID = 1L;

	/**
	 * Learning rate parametar
	 */
	protected double learningRate = 0.25;

	/**
	 * Current iteration counter
	 */
	protected int currentIteration = 0;
	
	/**
	 * Max training iterations (when to stopLearning training)
	 */
	protected int maxIterations = 0;

	/**
	 * Flag for indicating if the training iteration number is limited
	 */
	protected boolean iterationsLimited;	

	/**
	 * Creates new instannce of IterativeLearning learning algorithm
	 */
	public IterativeLearning() {
		super();
	}

	/**
	 * Creates new instannce of IterativeLearning learning algorithm for the
     * specified neural network.
	 * 
	 * @param network
	 *            neural network to train
	 */
	public IterativeLearning(NeuralNetwork network) {
		super(network);
	}

	/**
	 * Returns learning rate for this algorithm
	 * 
	 * @return learning rate for this algorithm
	 */
	public double getLearningRate() {
		return this.learningRate;
	}

	/**
	 * Sets learning rate for this algorithm
	 * 
	 * @param learningRate
	 *            learning rate for this algorithm
	 */
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}
	
	/**
	 * Sets iteration limit for this learning algorithm
	 * 
	 * @param maxIterations
	 *            iteration limit for this learning algorithm
	 */
	public void setMaxIterations(Integer maxIterations) {
		this.maxIterations = maxIterations.intValue();
	}	

	/**
	 * Returns current iteration of this learning algorithm
	 * 
	 * @return current iteration of this learning algorithm
	 */
	public Integer getCurrentIteration() {
		return new Integer(this.currentIteration);
	}
	
	public void learn(TrainingSet trainingSet) {
		if (this.maxIterations == 0)
			this.iterationsLimited = false;
		else
			this.iterationsLimited = true;

		this.currentIteration = 0;		
		
		while(!isStopped()) {
			doLearningEpoch(trainingSet);
			this.currentIteration++;
			if (iterationsLimited && (currentIteration >= maxIterations)) {
				stopLearning();
			}
			this.notifyChange(); // notify observers
		}
	}
	
	/**
	 * Override this method to implement specific learning epoch - one learning iteration, one pass through whole training set
	 * 
	 * @param trainingSet
	 *            training set
	 */
	abstract public void doLearningEpoch(TrainingSet trainingSet);	

}