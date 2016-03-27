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

// TODO:  random pattern order

/**
 * Base class for all supervised learning algorithms.
 * It extends IterativeLearning, and provides general supervised learning principles.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */

abstract public class SupervisedLearning extends IterativeLearning implements
		Serializable {

	/**
	 * The class fingerprint that is set to indicate serialization 
	 * compatibility with a previous version of the class
	 */	
	private static final long serialVersionUID = 2L;

	/**
	 * Total network error
	 */
	protected double totalNetworkError;

	/**
	 * Max allowed network error (condition to stop learning)
	 */
	protected double maxError = 0.000001;

	/**
	 * Creates new supervised learning rule
	 */
	public SupervisedLearning() {
		super();
	}

	/**
	 * Creates new supervised learning rule and sets the neural network to train
	 * 
	 * @param network
	 *            network to train
	 */
	public SupervisedLearning(NeuralNetwork network) {
		super(network);
	}

	/**
	 * This method implements basic logic for one learning epoch for the
	 * supervised learning algorithms. Epoch is the one pass through the
	 * training set. This method  iterates through the training set
	 * and trains network for each element. It also sets flag if conditions 
	 * to stop learning has been reached: network error below some allowed
	 * value, or maximum iteration count 
	 * 
	 * @param trainingSet
	 *            training set for training network
	 */
	public void doLearningEpoch(TrainingSet trainingSet) {

		this.totalNetworkError = 0;
		
		Iterator<TrainingElement> iterator = trainingSet.iterator();
		while (iterator.hasNext() && !isStopped()) {
			TrainingElement trainingElement = iterator.next();
			if(trainingElement instanceof SupervisedTrainingElement) {
				SupervisedTrainingElement supervisedTrainingElement = (SupervisedTrainingElement)trainingElement;
				this.learnPattern(supervisedTrainingElement);
			}
		}
					
		if (this.totalNetworkError < this.maxError) 
			stopLearning();	
	}

	/**
	 * Trains network with the pattern from the specified training element
	 * 
	 * @param trainingElement
	 *            supervised training element which contains input and desired
	 *            output
	 */
	protected void learnPattern(SupervisedTrainingElement trainingElement) {
		Vector<Double> input = trainingElement.getInput();
		this.neuralNetwork.setInput(input);
		this.neuralNetwork.calculate();
		Vector<Double> output = this.neuralNetwork.getOutput();
		Vector<Double> desiredOutput = trainingElement.getDesiredOutput();
		Vector<Double> patternError = this.getPatternError(output, desiredOutput);
		this.updateTotalNetworkError(patternError);
		this.updateNetworkWeights(patternError);
	}

	/**
	 * Calculates the network error for the current pattern - diference between
	 * desired and actual output
	 * 
	 * @param output
	 *            actual network output
	 * @param desiredOutput
	 *            desired network output
	 * @return pattern error
	 */
	protected Vector<Double> getPatternError(Vector<Double> output, Vector<Double> desiredOutput) {
		Vector<Double> patternError = new Vector<Double>();

		for(int i = 0; i < output.size(); i++) {
			Double outputError = desiredOutput.elementAt(i) - output.elementAt(i);
			patternError.add(outputError);
		}
		
		return patternError;
	}



	/**
	 * Sets allowed network error, which indicates when to stopLearning training
	 * 
	 * @param maxError
	 *            network error
	 */
	public void setMaxError(Double maxError) {
		this.maxError = maxError.doubleValue();
	}

	/**
	 * Returns total network error
	 * 
	 * @return total network error
	 */
	public Double getTotalNetworkError() {
		return new Double(totalNetworkError);
	}

	/**
	 * Subclasses update total network error for each training pattern with this
	 * method. Error update formula is learning rule specific.
	 * 
	 * @param patternError
	 *            pattern error vector
	 */
	abstract protected void updateTotalNetworkError(Vector<Double> patternError);

	/**
	 * This method should implement the weights update procedure
	 * 
	 * @param patternError
	 *            pattern error vector
	 */
	abstract protected void updateNetworkWeights(Vector<Double> patternError);

}
