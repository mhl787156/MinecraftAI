package org.neuroph.contrib.neat.gen.operations;

import java.util.List;

import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.Innovations;
import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.Specie;

public interface ReproductionOperation {

	/**
	 * Performs the reproduction operation.
	 * 
	 * @param params
	 *            the <code>NeatParameters</code> that describe the environment
	 *            for the evolution.
	 * @param innovations
	 *            the <code>Innovations</code> that contain all known
	 *            <code>ConnetionGene</code> and <code>NeuronGene</code>s.
	 * @param fitnessScores
	 *            the <code>FitnessScores</code> for getting fitness values of
	 *            an <code>Organism</code>
	 * @param requiredOrganisms
	 *            the number of <code>Organism</code>s this
	 *            <code>ReproductionOperation</code> is required to create.
	 * 
	 * @param species
	 *            the <code>Specie</code>s of the previous generation for
	 *            reproduction purposes.
	 * @param organisms
	 *            the <code>Organism</code>s of the previous generation for
	 *            reproduction purposes.
	 */
	public abstract void reproduce(NeatParameters params,
			Innovations innovations, FitnessScores fitnessScores,
			int requiredOrganisms, List<Specie> species,
			List<Organism> organisms, long generationNumber);

}