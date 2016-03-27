package org.neuroph.contrib.neat.gen.operations;

import java.util.List;

import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.Specie;

/**
 * A <code>Speciator</code> is responsible for examining the
 * <code>Organism</code>s for a generation and determining the
 * <code>Specie</code> that the <code>Organism</code> should belong to.
 * 
 * @author Aidan Morgan
 */
public interface Speciator {
	/**
	 * Performs the speciation for the provided <code>Organism</code>s.
	 * 
	 * If a <code>Organism</code> should belong to an existing species add it to
	 * the correct <code>Specie</code>, if no existing <code>Specie</code>
	 * exists then create a new one and add it to the <codE>List</code> of
	 * <code>Specie</code>s provided.
	 * 
	 * After this operation has completed, <b>ALL</b> <code>Organism</code>s
	 * should be associated with a <code>Specie</code>.
	 * 
	 * @param params
	 *            the <code>NeatParameters</code> that define the environment in
	 *            which this operation is performed.
	 * @param species
	 *            a <code>List</codE> of existing <code>Specie</code>s for the
	 *            evolution. If an <codE>Organism</code> does not belong to an
	 *            existing <code>Specie</code> then a new one should be created
	 *            and added to this <code>List</code>.
	 * @param scores
	 * @param chromosomes
	 *            a <code>List</code> of <code>Organism</code>s to evaluate for
	 *            which <code>Specie</code> they should belong to.
	 */
	public void speciate(NeatParameters params, List<Specie> species,
			FitnessScores scores, List<Organism> chromosomes);

	/**
	 * Used to return a comparison score between two <code>Organism</code>
	 * instances.
	 * 
	 * @param one
	 *            the first <code>Organism</code> to compare.
	 * @param two
	 *            the second <code>Organism</code> to compare.
	 * @return the comparison score for the two provided <code>Organism</code>s.
	 */
	public double compare(Organism one, Organism two);
}
