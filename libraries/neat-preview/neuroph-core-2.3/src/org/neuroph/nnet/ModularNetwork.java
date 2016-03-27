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

package org.neuroph.nnet;

import java.util.HashMap;
import java.util.Map;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.util.plugins.LabelsPlugin;

/**
 * Neural networks container for creating modular neural networks
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class ModularNetwork {

	/**
	 * Neural networks collection
	 */
	private Map<String, NeuralNetwork> neuralNets = new HashMap<String, NeuralNetwork>();
	
	// add neural connectors collection  which will be used to interconnect neural networks
	// neural connector could be collection of connection objects, put it in nnet.comp package
	// also it may be collection of linear neurons which will just pass signals, like patch panel
	// that way the networks will not be coupled
	
	public ModularNetwork() {
		
	}

        /**
         * Adds neural network
         * @param neuralNet
         */
	public void addNetwork(NeuralNetwork neuralNet) {
		LabelsPlugin labelsPlugin = ((LabelsPlugin)neuralNet.getPlugin("LabelsPlugin"));
		String label = labelsPlugin.getLabel(neuralNet);
		neuralNets.put(label, neuralNet);
	}

        /**
         * Gets neural network
         * @param label
         * @return neural network
         */
	public NeuralNetwork getNetwork(String label) {
		return neuralNets.get(label);
	}

        /**
         * Removes neural network
         * @param label
         */
	public void removeNetwork(String label) {
		// maybe we need to remove connections to other networks
		neuralNets.remove(label);
	}
	
	// calculate(what to calculate - labels sequence)
	
	
	
}
