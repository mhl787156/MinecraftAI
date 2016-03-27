package org.neuroph.contrib.neat.gen.operations.mutation;

import java.util.List;
import java.util.Set;

import org.neuroph.contrib.neat.gen.ConnectionGene;
import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.Gene;
import org.neuroph.contrib.neat.gen.Innovations;
import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.Organism;

public class WeightMutationOperation extends AbstractMutationOperation {
	private double probabilityOfNewWeight = 0.1;
	private double maxWeightPertubation = 0.5;

	public WeightMutationOperation(double mutationProbability) {
		super(mutationProbability);
	}

	/**
	 * Creates a new <code>WeightMutationOperation</code> using the default
	 * mutation probability from the C++ NEAT implementation.
	 */
	public WeightMutationOperation() {
		super(0.2);
	}

	public double getProbabilityOfNewWeight() {
		return probabilityOfNewWeight;
	}

	public void setProbabilityOfNewWeight(double probabilityOfNewWeight) {
		this.probabilityOfNewWeight = probabilityOfNewWeight;
	}

	public double getMaxWeightPertubation() {
		return maxWeightPertubation;
	}

	public void setMaxWeightPertubation(double maxWeightPertubation) {
		this.maxWeightPertubation = maxWeightPertubation;
	}

	@Override
	protected boolean mutate(NeatParameters neatParameters,
			Innovations innovations, FitnessScores scores, Organism o,
			Set<Gene> genesToAdd, Set<Gene> genesToRemove, int generationNumber) {
		List<ConnectionGene> connections = o.getConnections();

		if (!super.shouldMutate(neatParameters, super.getMutationProbability())) {
			return false;
		}

		ConnectionGene randomConnection = connections.get(neatParameters
				.getRandomGenerator().nextInt(connections.size()));
		if (shouldUseNewWeight(neatParameters)) {
			randomConnection.setWeight(neatParameters.getRandomClamped());
		} else {
			double currentWeight = randomConnection.getWeight();
			randomConnection
					.setWeight(currentWeight
							+ (maxWeightPertubation * neatParameters
									.getRandomClamped()));
		}

		return true;
	}

	private boolean shouldUseNewWeight(NeatParameters np) {
		return np.getRandomGenerator().nextDouble() < probabilityOfNewWeight;
	}
}
