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

package org.neuroph.util;

/**
 * Contains neural network types and labels.
 */
public enum NeuralNetworkType {
	ADALINE("Adaline"),
	PERCEPTRON("Perceptron"),
	MULTI_LAYER_PERCEPTRON("Multi Layer Perceptron"),
	HOPFIELD("Hopfield"),
	KOHONEN("Kohonen"),
	NEURO_FUZZY_REASONER("Neuro Fuzzy Reasoner"),
	SUPERVISED_HEBBIAN_NET("Supervised Hebbian network"),
	UNSUPERVISED_HEBBIAN_NET("Unsupervised Hebbian network"),
	COMPETITIVE("Competitive"),
	MAXNET("Maxnet"),
	INSTAR("Instar"),
	OUTSTAR("Outstar"),
	RBF_NETWORK("RBF Network"),
	BAM("BAM"),
	RECOMMENDER("Recommender");

	private String typeLabel;
	
	private NeuralNetworkType(String typeLabel) {
		this.typeLabel = typeLabel;
	}
	
	public String getTypeLabel() {
		return typeLabel;
	}
}
