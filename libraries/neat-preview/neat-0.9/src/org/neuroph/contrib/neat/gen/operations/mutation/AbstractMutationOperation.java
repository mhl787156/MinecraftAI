package org.neuroph.contrib.neat.gen.operations.mutation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.Gene;
import org.neuroph.contrib.neat.gen.Innovations;
import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.operations.MutationOperation;

/**
 * A <code>MutationOperation</code> is applied to each <code>Organism</code> as
 * part of the evolutionary process. It is the responsibility of a
 * <code>MutationOperation</code> to determine which <code>Gene</code>s need to
 * be added, removed or modified.
 * 
 * @author Aidan Morgan
 */
public abstract class AbstractMutationOperation implements MutationOperation {
	private static Logger s_log = Logger
			.getLogger(AbstractMutationOperation.class.getName());

	/**
	 * The probability of a mutation occuring. This value is initialised to
	 * zero, however it must be provided via the constructor for each
	 * <code>MutationOperation</code>.
	 */
	private double mutationProbability = 0.0;

	/**
	 * Creates a new <code>MutationOperation</code> with the provided
	 * probability of occurring.
	 * 
	 * @param mutationProbability
	 *            the probability that this <codE>MutationOperation</codE>
	 *            should be applied.
	 */
	public AbstractMutationOperation(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	/**
	 * Returns the mutation probability for this <code>MutationOperation</code>
	 * 
	 * @return the mutation probability for this <code>MutationOperation</code>
	 */
	public double getMutationProbability() {
		return mutationProbability;
	}

	/**
	 * @inheritDoc
	 */
	public int mutate(NeatParameters neatParameters, Innovations innovations,
			FitnessScores fitnessScores, List<Organism> generation,
			int generationNumber) {

		int totalOrganismsMutated = 0;

		for (Organism o : generation) {
			Set<Gene> genesToAdd = new HashSet<Gene>();
			Set<Gene> genesToRemove = new HashSet<Gene>();

			if (mutate(neatParameters, innovations, fitnessScores, o,
					genesToAdd, genesToRemove, generationNumber)) {
				totalOrganismsMutated++;
			}

			updateOrganismGenesAfterMutation(o, genesToAdd, genesToRemove);
		}

		return totalOrganismsMutated;
	}

	/**
	 * Updates the provided <code>Organism</code>, adding the provided
	 * <codE>Gene</codE>s specified to be added and removing the provided
	 * <code>Gene</code>s specified to be removed.
	 * 
	 * @param o
	 *            the <code>Organism</code> to update the <code>Gene</code>s of.
	 * @param genesToAdd
	 *            a <codE>Set</code> of <code>Gene</code>s to add to the
	 *            provided <code>Organism</code>s gene collection.
	 * @param genesToRemove
	 *            a <codE>Set</code> of <code>Gene</code>s to remove from the
	 *            provided <code>Organism</code>s gene collection.
	 */
	private void updateOrganismGenesAfterMutation(Organism o,
			Set<Gene> genesToAdd, Set<Gene> genesToRemove) {
		o.removeGenes(genesToRemove);
		o.addGenes(genesToAdd);
	}

	/**
	 * Abstract method to be implemented by every <code>MutationOperation</code>
	 * .
	 * 
	 * It is expected that every sub-class of this determines the appropriate
	 * <code>Gene</code> changes that result from the application of that
	 * mutation operation and update the <code>genesToAdd</code> and
	 * <code>genesToRemove</code> collections provided appropriately.
	 * 
	 * e.g. an "add neuron" mutation operation is responsible for finding an
	 * appropriate <code>ConnectionGene</code> to split, creating a new
	 * <code>NeuronGene</code> and two new <code>ConnectionGene</code>s, the new
	 * <code>NeuronGene</code> and the two new <code>ConnectionGene</code>s
	 * should be added to the <code>genesToAdd</code> <code>Set</code> whilst
	 * the original <code>ConnectionGene</code> identified to be split should be
	 * added to the <code>genesToRemove</code> <code>Set</code> as it has been
	 * replaced.
	 * 
	 * @param neatParameters
	 *            the <code>NeatParameters</code> defining the environment in
	 *            which this operation is performed.
	 * @param innovations
	 *            the <code>Innovations</code> cache.
	 * @param fitnessScores
	 *            the <code>FitnessScores</code> for the current generation.
	 * @param o
	 *            the <code>Organism</code> this operation is to be performed
	 *            on.
	 * @param genesToAdd
	 *            the <code>Set</code> of <code>Gene</code>s that will be added
	 *            to the provided <code>Organism</code>s gene collection once
	 *            this operation has completed.
	 * @param genesToRemove
	 *            the <code>Set</code> of <code>Gene</code>s that will be
	 *            removed from the provided <code>Organism</code>s gene
	 *            collection once this operation has completed.
	 * @param generationNumber
	 *            which generation this mutation is for.
	 */
	protected abstract boolean mutate(NeatParameters neatParameters,
			Innovations innovations, FitnessScores fitnessScores, Organism o,
			Set<Gene> genesToAdd, Set<Gene> genesToRemove, int generationNumber);

	/**
	 * Helper method that determines how many of the
	 * <code>MutationOperation</code> should be performed given the maximum
	 * number, and the probabilty of mutation.
	 * 
	 * @param config
	 *            the <code>NeatParameters</code> defining the environment in
	 *            which this operation is performed.
	 * @param maximum
	 *            the maximum value to be returned.
	 * @return that determines how many of the <code>MutationOperation</code>
	 *         should be performed given the maximum number, and the probabilty
	 *         of mutation.
	 */
	protected int determineNumberOfMutationsToPerform(NeatParameters config,
			int maximum) {
		int total = 0;

		for (int i = 0; i < maximum; i++) {
			double rand = config.getRandomGenerator().nextDouble();

			if (rand < mutationProbability) {
				total++;
			}
		}

		return total;
	}

	protected boolean shouldMutate(NeatParameters params, double mutationRate) {
		return (params.getRandomGenerator().nextDouble() < mutationRate);
	}

}
