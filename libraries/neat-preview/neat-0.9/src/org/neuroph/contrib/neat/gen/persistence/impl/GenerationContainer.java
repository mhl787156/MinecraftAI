package org.neuroph.contrib.neat.gen.persistence.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.neuroph.contrib.neat.gen.Generation;
import org.neuroph.contrib.neat.gen.Innovations;

public class GenerationContainer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8632483604282784483L;

	private Innovations innovations;

	private List<Generation> generations;

	public GenerationContainer() {
		generations = new ArrayList<Generation>();
	}

	public GenerationContainer(Innovations inn, List<Generation> generations) {
		this.innovations = inn;
		this.generations = generations;
	}

	public Innovations getInnovations() {
		return innovations;
	}

	public List<Generation> getGenerations() {
		return generations;
	}

	public void setInnovations(Innovations innovations) {
		this.innovations = innovations;
	}

	public void setGenerations(List<Generation> generations) {
		this.generations = generations;
	}

	public Generation getMostRecentGeneration() {
		if (!generations.isEmpty()) {
			return generations.get(generations.size() - 1);
		} else {
			throw new IllegalArgumentException("No Generations available.");
		}
	}

	public void addGeneration(Generation g) {
		generations.add(g);
	}
}
