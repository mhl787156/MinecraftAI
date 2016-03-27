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

package org.neuroph.nnet.comp;

import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.util.NeuronProperties;

/**
 * Represents layer of competitive neurons, and provides methods for competition.
 * 
 * TODO: competitive learning 3. training dw=n(i-w)
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class CompetitiveLayer extends Layer {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */		
	private static final long serialVersionUID = 1L;
	
	/**
	 * Max iterations for neurons to compete
	 * This is neccesery to limit, otherwise if there is no winner there will
	 * be endless loop.
	 */
	private int maxIterations = 100;
	
	/**
	 * The competition winner for this layer
	 */
	private CompetitiveNeuron winner;

	/**
	 * Create an instance of CompetitiveLayer with the specified number of
	 * neurons with neuron properties
	 * @param neuronNum neuron number in this layer
	 * @param neuronProperties properties for the nurons in this layer
	 */
	public CompetitiveLayer(int neuronNum, NeuronProperties neuronProperties) {
		super(neuronNum, neuronProperties);
	}

	/**
	 * Performs calculaton for all neurons in this layer
	 */
	@Override
	public void calculate() {
		boolean hasWinner = false;
		
		int iterationsCount = 0;
		
		while (!hasWinner) {
			int fireingNeurons = 0;
			for(Neuron neuron : this.neurons) {
				neuron.calculate();
				if (neuron.getOutput() > 0)
					fireingNeurons += 1;
			} // for
			
			if (iterationsCount > this.maxIterations) break;
			
			if (fireingNeurons == 1)
				hasWinner = true;
			iterationsCount++;
			
		} // while !done

		if (hasWinner) { 
			// now set reference to winner
			double maxOutput = Double.MIN_VALUE;

			for(Neuron neuron : this.neurons) {	
				CompetitiveNeuron cNeuron = (CompetitiveNeuron)neuron;
				cNeuron.setIsCompeting(false); // turn off competing mode
				if (cNeuron.getOutput() > maxOutput) {
					maxOutput = cNeuron.getOutput();
					this.winner = cNeuron;
				}
			}
		}
		
	}

	/**
	 * Returns the winning neuron for this layer
	 * @return winning neuron for this layer
	 */
	public CompetitiveNeuron getWinner() {
		return this.winner;
	}

	/**
	 * Returns the maxIterations setting for this layer
	 * @return maxIterations setting for this layer
	 */
	public int getMaxIterations() {
		return maxIterations;
	}
	
	/**
	 * Sets max iterations for neurons to compete in this layer
	 * @param maxIterations  max iterations for neurons to compete in this layer
	 */
	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}
	
	

}
