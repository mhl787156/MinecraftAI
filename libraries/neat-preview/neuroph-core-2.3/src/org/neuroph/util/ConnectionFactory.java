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

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.nnet.comp.DelayedConnection;

/**
 * Provides methods to connect neurons by creating Connection objects.
 */
public class ConnectionFactory {

	/**
	 * Creates connection between two specified neurons
	 * 
	 * @param from
	 *            output neuron
	 * @param to
	 *            input neuron
	 */
	public static void createConnection(Neuron from, Neuron to) {
		Connection connection = new Connection(from);
		to.addInputConnection(connection);
	}

	/**
	 * Creates connection between two specified neurons
	 * 
	 * @param from
	 *            output neuron
	 * @param to
	 *            input neuron
	 * @param weightVal
	 *            connection weight value
	 */
	public static void createConnection(Neuron from, Neuron to, Double weightVal) {
		Connection connection = new Connection(from, weightVal.doubleValue());
		to.addInputConnection(connection);
	}

	public static void createConnection(Neuron from, Neuron to,
			Double weightVal, int delay) {
		DelayedConnection connection = new DelayedConnection(from, weightVal
				.doubleValue(), delay);
		to.addInputConnection(connection);
	}

	/**
	 * Creates connection between two specified neurons
	 * 
	 * @param from
	 *            output neuron
	 * @param to
	 *            input neuron
	 * @param weight
	 *            connection weight
	 */
	public static void createConnection(Neuron from, Neuron to, Weight weight) {
		Connection connection = new Connection(from, weight);
		to.addInputConnection(connection);
	}

	/**
	 * Creates full connectivity between the two specified layers
	 * 
	 * @param fromLayer
	 *            layer to connect
	 * @param toLayer
	 *            layer to connect to
	 */
	public static void fullConnect(Layer fromLayer, Layer toLayer) {
		for(Neuron fromNeuron : fromLayer.getNeurons()) {
			for (Neuron toNeuron : toLayer.getNeurons()) {
				createConnection(fromNeuron, toNeuron);
			} 
		} 
	}

	/**
	 * Creates full connectivity between two specified layers with specified
	 * weight for all connections
	 * 
	 * @param fromLayer
	 *            output layer
	 * @param toLayer
	 *            input layer
         * @param weightVal
         *             connection weight value
	 */
	public static void fullConnect(Layer fromLayer, Layer toLayer, double weightVal) {
		for(Neuron fromNeuron : fromLayer.getNeurons()) {
			for (Neuron toNeuron : toLayer.getNeurons()) {
				createConnection(fromNeuron, toNeuron, weightVal);
			} 
		} 		
	}

	/**
	 * Creates full connectivity within layer - each neuron with all other
	 * within the same layer
	 */
	public static void fullConnect(Layer layer) {
		int neuronNum = layer.getNeuronsCount();
		for (int i = 0; i < neuronNum; i++) {
			for (int j = 0; j < neuronNum; j++) {
				if (j == i)
					continue;
				Neuron from = layer.getNeuronAt(i);
				Neuron to = layer.getNeuronAt(j);
				createConnection(from, to);
			} // j
		} // i
	}

	/**
	 * Creates full connectivity within layer - each neuron with all other
	 * within the same layer with the specified weight values for all
	 * conections.
	 */
	public static void fullConnect(Layer layer, double weightVal) {
		int neuronNum = layer.getNeuronsCount();
		for (int i = 0; i < neuronNum; i++) {
			for (int j = 0; j < neuronNum; j++) {
				if (j == i)
					continue;
				Neuron from = layer.getNeuronAt(i);
				Neuron to = layer.getNeuronAt(j);
				createConnection(from, to, new Double(weightVal));
			} // j
		} // i
	}

	/**
	 * Creates full connectivity within layer - each neuron with all other
	 * within the same layer with the specified weight and delay values for all
	 * conections.
	 */
	public static void fullConnect(Layer layer, double weightVal, int delay) {
		int neuronNum = layer.getNeuronsCount();
		for (int i = 0; i < neuronNum; i++) {
			for (int j = 0; j < neuronNum; j++) {
				if (j == i)
					continue;
				Neuron from = layer.getNeuronAt(i);
				Neuron to = layer.getNeuronAt(j);
				createConnection(from, to, new Double(weightVal), delay);
			} // j
		} // i
	}

	/**
	 * Creates forward connectivity pattern between the specified layers
	 * 
	 * @param fromLayer
	 *            layer to connect
	 * @param toLayer
	 *            layer to connect to
	 */
	public static void forwardConnect(Layer fromLayer, Layer toLayer, double weightVal) {
		for(int i=0; i<fromLayer.getNeuronsCount(); i++) {
			Neuron fromNeuron = fromLayer.getNeuronAt(i);
			Neuron toNeuron = toLayer.getNeuronAt(i);
			createConnection(fromNeuron, toNeuron, new Double(weightVal));
		}
	}

	/**
	 * Creates forward connection pattern between specified layers
	 * 
	 * @param fromLayer
	 *            layer to connect
	 * @param toLayer
	 *            layer to connect to
	 */
	public static void forwardConnect(Layer fromLayer, Layer toLayer) {
		for(int i=0; i<fromLayer.getNeuronsCount(); i++) {
			Neuron fromNeuron = fromLayer.getNeuronAt(i);
			Neuron toNeuron = toLayer.getNeuronAt(i);
			createConnection(fromNeuron, toNeuron, new Double(1));
		}		
	}

}