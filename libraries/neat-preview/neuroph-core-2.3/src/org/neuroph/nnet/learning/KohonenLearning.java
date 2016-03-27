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

import java.util.Enumeration;
import java.util.Iterator;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.Kohonen;

/**
 * Learning algorithm for Kohonen network.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class KohonenLearning extends LearningRule {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;
	
	double learningRate = 0.9;
	int[] iterations = { 100, 0 };
	double decStep[] = new double[2];
	int mapSize = 0;
	int[] nR = { 1, 1 }; // neighborhood radius
	int currentIteration;

    
	public KohonenLearning(Kohonen nnet) {
		super(nnet);
		int neuronsNum = neuralNetwork.getLayerAt(1).getNeuronsCount();
		mapSize = (int) Math.sqrt(neuronsNum);
	}

	public void learn(TrainingSet trainingSet) {
		for (int phase = 0; phase < 2; phase++) {
			for (int k = 0; k < iterations[phase]; k++) {
				Enumeration<TrainingElement> e = trainingSet.elements();
				while (e.hasMoreElements() && !isStopped()) {
					TrainingElement tE = e.nextElement();
					learnPattern(tE, nR[phase]);				
				} // while
				currentIteration = k;
				this.notifyChange();	
				if (isStopped()) return;
			} // for k
			learningRate = learningRate * 0.5;
		} // for phase
	}

	private void learnPattern(TrainingElement tE, int neighborhood) {
		neuralNetwork.setInput(tE.getInput());
		neuralNetwork.calculate();
		Neuron winner = getClosest();
		if (winner.getOutput() == 0)
			return; // ako je vec istrenirana jedna celija, izadji

		Layer mapLayer = neuralNetwork.getLayerAt(1);
		int winnerIdx = mapLayer.indexOf(winner);
		adjustCellWeights(winner, 0);

		int cellNum = mapLayer.getNeuronsCount();
		for (int p = 0; p < cellNum; p++) {
			if (p == winnerIdx)
				continue;
			if (isNeighbor(winnerIdx, p, neighborhood)) {
				Neuron cell = mapLayer.getNeuronAt(p);
				adjustCellWeights(cell, 1);
			} // if
		} // for

	}

	// get unit with closetst weight vector
	private Neuron getClosest() {
		Iterator<Neuron> i = this.neuralNetwork.getLayerAt(1)
				.getNeuronsIterator();
		Neuron winner = new Neuron();
		double minOutput = 100;
		while (i.hasNext()) {
			Neuron n = i.next();
			double out = n.getOutput();
			if (out < minOutput) {
				minOutput = out;
				winner = n;
			} // if
		} // while
		return winner;
	}

	private void adjustCellWeights(Neuron cell, int r) {
		Iterator<Connection> i = cell.getInputsIterator();
		while (i.hasNext()) {
			Connection conn = i.next();
			double dWeight = (learningRate / (r + 1))
					* (conn.getInput() - conn.getWeight().getValue());
			conn.getWeight().inc(dWeight);
		}// while
	}

	private boolean isNeighbor(int i, int j, int n) {
		// i - centralna celija
		// n - velicina susedstva
		// j - celija za proveru
		n = 1;
		int d = mapSize;

		// if (j<(i-n*d-n)||(j>(i+n*d+n))) return false;

		int rt = n; // broj celija ka gore
		while ((i - rt * d) < 0)
			rt--;

		int rb = n; // broj celija ka dole
		while ((i + rb * d) > (d * d - 1))
			rb--;

		for (int g = -rt; g <= rb; g++) {
			int rl = n; // broj celija u levu stranu
			int rl_mod = (i - rl) % d;
			int i_mod = i % d;
			while (rl_mod > i_mod) {
				rl--;
				rl_mod = (i - rl) % d;
			}

			int rd = n; // broj celija u desnu stranu
			int rd_mod = (i + rd) % d;
			while (rd_mod < i_mod) {
				rd--;
				rd_mod = (i + rd) % d;
			}

			if ((j >= (i + g * d - rl)) && (j <= (i + g * d + rd)))
				return true;
			// else if (j<(i+g*d-rl)) return false;
		} // for
		return false;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public void setIterations(int Iphase, int IIphase) {
		this.iterations[0] = Iphase;
		this.iterations[1] = IIphase;
	}

	public Integer getIteration() {
		return new Integer(currentIteration);
	}

	public int getMapSize() {
		return mapSize;
	}

}
