package org.neuroph.contrib.neat.gen.operations.reproduction;

import java.util.List;

import org.neuroph.contrib.neat.gen.ConnectionGene;
import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.Innovations;
import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.Organism;

public class CrossoverReproductionOperator extends
		AbstractReproductionOperation {
	private static final double DEFAULT_CROSSOVER_REPRODUCTION = 1.0;

	public CrossoverReproductionOperator(double percentage) {
		super(percentage);
	}

	public CrossoverReproductionOperator() {
		super(DEFAULT_CROSSOVER_REPRODUCTION);
	}

	@Override
	protected void reproduce(NeatParameters config, Innovations innovations,
			FitnessScores scores, List<Organism> parentChroms,
			int numOffspring, List<Organism> offspring, long generationNumber) {

		if (parentChroms.size() < 1) {
			throw new IllegalArgumentException("Must have at least one parent to perform a crossover.");
		}

		if (parentChroms.size() < 2) {
			// we have to have two parent organisms to reproduce via crossover,
			// so just clone the one parent.
			CloneReproductionOperator.performClone(config, innovations, scores,
					parentChroms, numOffspring, offspring, generationNumber);
		} else {
			int targetSize = offspring.size() + numOffspring;

			while (offspring.size() < targetSize) {
				int motherIndex = config.getRandomGenerator().nextInt(
						parentChroms.size());
				int fatherIndex = motherIndex;

				do {
					fatherIndex = config.getRandomGenerator().nextInt(
							parentChroms.size());
				} while (fatherIndex == motherIndex);

				// determine dominance/recessiveness
				Organism dominant = null;
				Organism recessive = null;
				Organism mother = parentChroms.get(motherIndex);
				Organism father = parentChroms.get(fatherIndex);

				if (scores.getFitness(mother) > scores.getFitness(father)) {
					dominant = mother;
					recessive = father;
				} else {
					recessive = mother;
					dominant = father;
				}

				Organism child = dominant.copy(config);

				List<ConnectionGene> genes = child.getConnections();

				for (ConnectionGene gene : genes) {
					if (config.getRandomGenerator().nextBoolean()) {

						ConnectionGene dominantGene = gene;
						ConnectionGene recessiveGene = (ConnectionGene) recessive
								.findMatchingGene(dominantGene);

						if (recessiveGene != null) {
							double weight = recessiveGene.getWeight();
							dominantGene.setWeight(weight);
						}
					}
				}

				offspring.add(child);
				child.setAncestory(new long[] { mother.getInnovationId(),
						father.getInnovationId() });
			}
		}
	}

}
