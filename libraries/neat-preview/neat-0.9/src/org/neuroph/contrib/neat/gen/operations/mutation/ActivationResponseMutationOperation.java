package org.neuroph.contrib.neat.gen.operations.mutation;

import java.util.List;
import java.util.Set;

import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.Gene;
import org.neuroph.contrib.neat.gen.Innovations;
import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.NeuronGene;
import org.neuroph.contrib.neat.gen.NeuronType;
import org.neuroph.contrib.neat.gen.Organism;

public class ActivationResponseMutationOperation extends
		AbstractMutationOperation {
	private double maxActivationPerturbation = 0.1;

	public ActivationResponseMutationOperation(double mutationProbability) {
		super(mutationProbability);
	}

	public ActivationResponseMutationOperation() {
		super(0.1);
	}

	public double getMaxActivationPerturbation() {
		return maxActivationPerturbation;
	}

	public void setMaxActivationPerturbation(double maxActivationPerturbation) {
		this.maxActivationPerturbation = maxActivationPerturbation;
	}

	@Override
	protected boolean mutate(NeatParameters neatParameters,
			Innovations innovations, FitnessScores scores, Organism o,
			Set<Gene> genesToAdd, Set<Gene> genesToRemove, int generationNumber) {
		List<NeuronGene> genes = o.getNeurons(NeuronType.HIDDEN);

		if (genes.isEmpty()) {
			return false;
		}

		if (!super.shouldMutate(neatParameters, super.getMutationProbability())) {
			return false;
		}

		NeuronGene randomNeuron = genes.get(neatParameters.getRandomGenerator()
				.nextInt(genes.size()));

		double existingActivationResponse = randomNeuron
				.getActivationResponse();
		randomNeuron
				.setActivationResponse(existingActivationResponse
						+ (neatParameters.getRandomClamped() * maxActivationPerturbation));

		return true;
	}
}
