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
import java.util.Observable;

import org.neuroph.core.NeuralNetwork;

/**
 * Base class for all neural network learning algorithms.
 * It provides the general principles for training neural network.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
abstract public class LearningRule extends Observable implements Runnable,
		Serializable {

	/**
	 * The class fingerprint that is set to indicate serialization 
	 * compatibility with a previous version of the class
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Neural network to train
	 */
	protected NeuralNetwork neuralNetwork;

	/**
	 * Collection of training elements
	 */
	private transient TrainingSet trainingSet;
	
	/**
	 * Flag to stop learning
	 */
	private transient boolean stopLearning=false;		

	/**
	 * Creates instance of learning rule
	 */
	public LearningRule() {	}

	/**
	 * Sets neural network for this learning rule
	 * 
	 * @param network
	 *            neural network to train
	 */
	public LearningRule(NeuralNetwork network) {
		this.neuralNetwork = network;
	}

	/**
	 * Sets training set for this learning rule
	 * 
	 * @param trainingSet
	 *            training set for this learning rule
	 */
	public void setTrainingSet(TrainingSet trainingSet) {
		this.trainingSet = trainingSet;
	}

	/**
	 * Gets training set
	 * 
	 * @return training set
	 */
	public TrainingSet getTrainingSet() {
		return trainingSet;
	}

        /**
         * Gets neural network
         * @return neural network
         */
        public NeuralNetwork getNeuralNetwork() {
            return neuralNetwork;
        }

        /**
         * Sets neural network for this learning rule
         * @param neuralNetwork
         *              neural network for this learning rule
         */
        public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
            this.neuralNetwork = neuralNetwork;
        }



	/**
	 * Method from Runnable interface for running learning procedure in separate
	 * thread.
	 */
        @Override
	public void run() {
		this.stopLearning = false;
		this.learn(this.trainingSet);
	}
	
	/**
	 * Stops learning
	 */
	public void stopLearning() {
		this.stopLearning = true;
	}	
	
	/**
	 * Returns true if learning has stopped, false otherwise
	 * @return true if learning has stopped, false otherwise
	 */
	public boolean isStopped() {
		return this.stopLearning;
	}


	/**
	 * Notify observers about change
	 */
	protected void notifyChange() {
		setChanged();
		notifyObservers();
		clearChanged();
	}

	/**
	 * Override this method to implement specific learning procedures
	 * 
	 * @param trainingSet
	 *            training set
	 */
	abstract public void learn(TrainingSet trainingSet);

}