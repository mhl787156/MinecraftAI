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

import java.util.Vector;

import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;

/**
 * Provides methods to create instance of a Layer with specifed number of neurons and neuron's properties.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class LayerFactory {

	public static Layer createLayer(int neuronsNum, NeuronProperties neuronProperties) {
		Layer layer = new Layer(neuronsNum, neuronProperties);
		return layer;
	}

	public static Layer createLayer(int neuronsNum, TransferFunctionType transferFunctionType) {
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("transferFunction", transferFunctionType);
		Layer layer = createLayer(neuronsNum, neuronProperties);
		return layer;
	}

	public static Layer createLayer(Vector<NeuronProperties> neuronPropertiesVector) {
		Layer layer = new Layer();
		
		for(NeuronProperties neuronProperties : neuronPropertiesVector) {
			Neuron neuron = NeuronFactory.createNeuron(neuronProperties);
			layer.addNeuron(neuron);
		}
		
		return layer;
	}

}