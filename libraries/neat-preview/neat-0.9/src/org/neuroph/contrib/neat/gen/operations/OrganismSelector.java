package org.neuroph.contrib.neat.gen.operations;

import java.util.List;

import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.Specie;

/**
 * An <code>OrganismSelector</code> is responsible for determining which
 * <code>Organism</code>s to keep after a generation step.
 * 
 * Simple implementations will consider issues such as roulette wheel selection
 * and elitism.
 * 
 * 
 * @author Aidan Morgan
 */
public interface OrganismSelector {
	/**
	 * Returns a <code>List</code> of <code>Organism</code>s to keep for the
	 * next generation.
	 * 
	 * @param params
	 *            the <code>NeatParameters</code> describing the environment
	 *            NEAT is running in.
	 * @param organisms
	 *            the <code>Organism</code>s to select from.
	 * @return a <code>List</code> of <code>Organism</code>s to keep for the
	 *         next generation.
	 */
	public List<Organism> selectOrganismsForNextGeneration(
			NeatParameters params, FitnessScores scores, List<Specie> species,
			List<Organism> organisms, long generation);

	/**
	 * Returns a <code>List</code> of <code>Specie</code>s that should be kept
	 * for the next generation.
	 * 
	 * @param neatParameters
	 *            the <code>NeatParameters</code> describing the environment
	 *            NEAT is running in.
	 * @param species
	 *            the <code>List</code> of <code>Specie</code>s to select from.
	 * @param scores
	 *            the <code>FitnessScores</code> to get the <code>Fitness</code>
	 *            of <code>Specie</code>s from.
	 * @param generation
	 *            the last <code>Generation</code>.
	 * 
	 * @return a <code>List</code> of <code>Specie</code>s that should be kept
	 *         for the next generation.
	 */
	public List<Specie> selectSpecies(NeatParameters neatParameters,
			List<Specie> species, FitnessScores scores, long generation);

}
