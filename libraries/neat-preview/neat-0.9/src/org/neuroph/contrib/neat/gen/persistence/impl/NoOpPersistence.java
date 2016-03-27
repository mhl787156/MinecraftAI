package org.neuroph.contrib.neat.gen.persistence.impl;

import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.Generation;
import org.neuroph.contrib.neat.gen.Innovations;
import org.neuroph.contrib.neat.gen.persistence.Persistence;
import org.neuroph.contrib.neat.gen.persistence.PersistenceException;

/**
 * A no-operation implementation of the <code>Persistence</code> interface.
 * 
 * Use this instance if you do not wish to persist any values.
 * 
 * @author Aidan Morgan
 */
public class NoOpPersistence implements Persistence {

	public void setInnovations(Innovations i) {

	}

	public Generation loadGeneration(Innovations i) {
		return null;
	}

	public Innovations loadInnovations() {
		return null;
	}

	public void addGeneration(Innovations i, Generation g, FitnessScores fitness) {

	}

	public FitnessScores loadFitnessScores() {
		return null;
	}

	@Override
	public long getGenerationCount() {
		return 0;
	}

	@Override
	public Generation loadGeneration(long l, Innovations innovations)
			throws PersistenceException {
		return null;
	}

	@Override
	public FitnessScores loadFitnessScores(long generation)
			throws PersistenceException {
		return null;
	}

	@Override
	public Innovations loadInnovations(long generation)
			throws PersistenceException {
		return null;
	}

}
