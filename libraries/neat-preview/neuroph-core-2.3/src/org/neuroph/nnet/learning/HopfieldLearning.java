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

package org.neuroph.nnet.learning;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;

/**
 * Learning algorithm for the Hopfield neural network.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class HopfieldLearning extends LearningRule {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new HopfieldLearning
	 */
	public HopfieldLearning() {
		super();
	}

	/**
	 * Creates new HopfieldLearning for the specified neural network
	 * 
	 * @param neuralNetwork
	 */
	public HopfieldLearning(NeuralNetwork neuralNetwork) {
		super(neuralNetwork);
	}

	/**
	 * Calculates weights for the hopfield net to learn the specified training
	 * set
	 * 
	 * @param trainingSet
	 *            training set to learn
	 */
	public void learn(TrainingSet trainingSet) {
		int M = trainingSet.size();
		int N = neuralNetwork.getLayerAt(0).getNeuronsCount();
		Layer hopfieldLayer = neuralNetwork.getLayerAt(0);

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (j == i)
					continue;
				Neuron ni = hopfieldLayer.getNeuronAt(i);
				Neuron nj = hopfieldLayer.getNeuronAt(j);
				Connection cij = nj.getConnectionFrom(ni);
				Connection cji = ni.getConnectionFrom(nj);
				double w = 0;
				for (int k = 0; k < M; k++) {
					TrainingElement trainingElement = trainingSet.elementAt(k);
					Double pki = (Double) trainingElement.getInput().elementAt(
							i);
					Double pkj = (Double) trainingElement.getInput().elementAt(
							j);
					w = w + pki.doubleValue() * pkj.doubleValue();
				} // k
				cij.getWeight().setValue(w);
				cji.getWeight().setValue(w);
			} // j
		} // i

	}

}
