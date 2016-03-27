package org.neuroph.contrib.neat.gen.operations.mutation;

import java.util.List;
import java.util.Set;

import org.neuroph.contrib.neat.gen.ConnectionGene;
import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.Gene;
import org.neuroph.contrib.neat.gen.Innovations;
import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.NeuronGene;
import org.neuroph.contrib.neat.gen.NeuronType;
import org.neuroph.contrib.neat.gen.Organism;

public class AddConnectionMutationOperation extends AbstractMutationOperation {

	private static final double DEFAULT_ADD_CONNECTION_PROBABILITY = 0.07;
	private static final int NUMER_OF_ADD_CONNECTION_ATTEMPTS = 5;

	public AddConnectionMutationOperation(double mutationProbability) {
		super(mutationProbability);
	}

	public AddConnectionMutationOperation() {
		super(DEFAULT_ADD_CONNECTION_PROBABILITY);
	}

	@Override
	protected boolean mutate(NeatParameters neatParameters,
			Innovations innovations, FitnessScores scores, Organism o,
			Set<Gene> genesToAdd, Set<Gene> genesToRemove, int generationNumber) {
		List<NeuronGene> neurons = o.getNeurons();

		if (!super.shouldMutate(neatParameters, getMutationProbability())) {
			return false;
		}

		int attemptCount = 0;

		NeuronGene start = null;
		NeuronGene end = null;

		do {
			start = neurons.get(neatParameters.getRandomGenerator().nextInt(
					neurons.size()));
			end = neurons.get(neatParameters.getRandomGenerator().nextInt(
					neurons.size()));

			if (attemptCount > NUMER_OF_ADD_CONNECTION_ATTEMPTS) {
				return false;
			}

			attemptCount++;
		} while (!isValidConnection(o, innovations, genesToAdd, genesToRemove,
				start, end));

		// TODO: need to do something to detect and reject recurrent links
		// here...
		// 
		// it should be a configuration option if recurrent links are allowed
		// (personally I like recurrent links)
		// but some people don't :-)
		ConnectionGene connection = innovations.getConnectionGene(start
				.getInnovationId(), end.getInnovationId());
		if (connection == null) {
			connection = new ConnectionGene(neatParameters, start
					.getInnovationId(), end.getInnovationId(), neatParameters
					.getRandomGenerator().nextDouble(), true);
			innovations.putConnectionGene(connection);
		}

		genesToAdd.add(connection);
		return true;
	}

	private boolean isValidConnection(Organism o, Innovations inn,
			Set<Gene> genesToAdd, Set<Gene> genesToRemove, NeuronGene start,
			NeuronGene end) {
		if (!o.canConnect(start.getInnovationId(), end.getInnovationId())) {
			return false;
		}

		if (start.equals(end)) {
			return false;
		}

		if (start.getNeuronType() == NeuronType.INPUT) {
			if (end.getNeuronType() == NeuronType.INPUT) {
				return false;
			}
		}

		if (start.getNeuronType() == NeuronType.OUTPUT) {
			if (end.getNeuronType() == NeuronType.OUTPUT) {
				return false;
			}
		}

		for (Gene g : genesToAdd) {
			if (g instanceof ConnectionGene) {
				ConnectionGene cg = (ConnectionGene) g;

				if (cg.getOriginId() == start.getInnovationId()
						|| cg.getEndpointId() == end.getInnovationId()) {
					return false;
				}
			}
		}

		for (Gene g : genesToRemove) {
			if (g instanceof ConnectionGene) {
				ConnectionGene cg = (ConnectionGene) g;

				if (cg.getOriginId() == start.getInnovationId()
						|| cg.getEndpointId() == end.getInnovationId()) {
					return false;
				}
			}
		}

		return true;
	}

}
