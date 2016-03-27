package org.neuroph.contrib.neat.gen.persistence;

import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.Generation;
import org.neuroph.contrib.neat.gen.Innovations;

/**
 * Interface defining operations that must be performed to be able to 
 * persist a <code>Generation</code>.
 * 
 * The mechanism used to persist items associated with the NEAT engine is
 * abstracted behind this interface.
 * 
 * @author Aidan Morgan
 */
public interface Persistence {

	/**
	 * Adds the provided <code>Generation</code> to the <code>Persistence</code> mechanism,
	 * also included are the <code>Innovations</code> and <code>FitnessScores</code> created
	 * up to, and including this <code>Generation</code>.
	 * 
	 * @param i
	 * @param g
	 * @param fitness
	 * @throws PersistenceException
	 */
	public void addGeneration(Innovations i, Generation g, FitnessScores fitness)
			throws PersistenceException;

	/**
	 * Loads the <code>Innovations</code> from the persistent store for the provided generation
	 * number.
	 * 
	 * @param generation the generation up to which to load <code>Innovations</code> for. 
	 * 
	 * @return a <code>Innovations</code> instance that contains all innovations up to and
	 * including the provided generation number.
	 * 
	 * @throws PersistenceException
	 */
	public Innovations loadInnovations(long generation) throws PersistenceException;
	
	/**
	 * Loads the most recent <code>Innovations</code> from the persistence store. This includes all
	 * innovations up to and including the last persisted <code>Innovations</codE> instance.
	 * 
	 * @see #getGenerationCount()
	 * 
	 * @return the most recent <code>Innovations</code> from the persistence store.
	 * @throws PersistenceException
	 */
	public Innovations loadInnovations() throws PersistenceException;

	public Generation loadGeneration(Innovations innovations) throws PersistenceException;
	
	public Generation loadGeneration(long generation, Innovations innovations) throws PersistenceException;
	
	public FitnessScores loadFitnessScores(long generation) throws PersistenceException;
	
	public FitnessScores loadFitnessScores() throws PersistenceException;

	/**
	 * Returns the number of <code>Generation</code>s that are available in the persistence store.
	 * @return the number of <code>Generation</code>s that are available in the persistence store.
	 */
	public long getGenerationCount();
}
