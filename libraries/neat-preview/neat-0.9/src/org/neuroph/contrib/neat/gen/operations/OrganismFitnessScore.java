package org.neuroph.contrib.neat.gen.operations;

import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.core.NeuralNetwork;

public class OrganismFitnessScore {
	private Organism organism;
	private double fitness;
	private NeuralNetwork network;

	public OrganismFitnessScore(Organism organism, NeuralNetwork network) {
		super();
		this.organism = organism;
		this.network = network;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public Organism getOrganism() {
		return organism;
	}

	public NeuralNetwork getNeuralNetwork() {
		return network;
	}
}
