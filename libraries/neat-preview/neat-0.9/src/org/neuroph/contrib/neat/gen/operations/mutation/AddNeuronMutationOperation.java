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

public class AddNeuronMutationOperation extends AbstractMutationOperation {

	private static final double DEFAULT_NEURON_ADD_PROBABILITY = 0.04;

	public AddNeuronMutationOperation(double mutationProbability) {
		super(mutationProbability);
	}

	public AddNeuronMutationOperation() {
		super(DEFAULT_NEURON_ADD_PROBABILITY);
	}

	@Override
	protected boolean mutate(NeatParameters neatParameters,
			Innovations innovations, FitnessScores scores, Organism o,
			Set<Gene> genesToAdd, Set<Gene> genesToRemove, int generationNumber) {
		// adding a neuron is a matter of locating a connection and splitting it
		// into two parts.
		List<ConnectionGene> connections = o.getConnections();

		if (!super.shouldMutate(neatParameters, super.getMutationProbability())) {
			return false;
		}

		addNeuron(neatParameters, innovations, o, genesToAdd, connections);
		return true;
	}

	public static boolean addNeuron(NeatParameters neatParameters,
			Innovations innovations, Organism o, Set<Gene> genesToAdd,
			List<ConnectionGene> connections) {
		ConnectionGene theConnection = connections.get(neatParameters
				.getRandomGenerator().nextInt(connections.size()));

		NeuronGene start = o.getNeuron(theConnection.getOriginId());
		NeuronGene end = o.getNeuron(theConnection.getEndpointId());

		NeuronGene ng = innovations.getNeuronGene(theConnection
				.getInnovationId());

		if (ng == null) {
//			ng = new NeuronGene(NeuronType.HIDDEN, theConnection
//					.getInnovationId(), neatParameters.getRandomGenerator()
//					.nextDouble());
			
			ng = new NeuronGene(NeuronType.HIDDEN, 
					neatParameters.nextInnovationId(), 
					neatParameters.getRandomGenerator().nextDouble());
			
			innovations.putNeuronGene(theConnection.getInnovationId(), ng);
		}

		ConnectionGene firstHalf = innovations.getConnectionGene(start
				.getInnovationId(), ng.getInnovationId());
		if (firstHalf == null) {
			firstHalf = new ConnectionGene(neatParameters, start
					.getInnovationId(), ng.getInnovationId(), 1.0, true);
			innovations.putConnectionGene(firstHalf);
		}

		ConnectionGene secondHalf = innovations.getConnectionGene(ng
				.getInnovationId(), end.getInnovationId());

		if (secondHalf == null) {
			secondHalf = new ConnectionGene(neatParameters, ng
					.getInnovationId(), end.getInnovationId(), theConnection
					.getWeight(), true);
			innovations.putConnectionGene(secondHalf);
		}

		genesToAdd.add(ng);
		genesToAdd.add(firstHalf);
		genesToAdd.add(secondHalf);

		theConnection.setEnabled(false);

		return true;
	}

}
