package org.neuroph.contrib.neat.gen.operations.reproduction;

import java.util.List;

import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.Innovations;
import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.Organism;

/**
 * An implementation of the <code>ReproductionOperator</code> that performs a
 * simple clone reproduction.
 * 
 * @author Aidan Morgan
 */
public class CloneReproductionOperator extends AbstractReproductionOperation {

	public CloneReproductionOperator(double percentage) {
		super(percentage);
	}

	public CloneReproductionOperator() {
		super(0.0);
	}


	/**
	 * @see CloneReproductionOperator#reproduce(NeatParameters, Innovations, FitnessScores, int, List, List, long)
	 */
	public static void performClone(NeatParameters params,
			Innovations innovations, FitnessScores scores,
			List<Organism> parents, int numOffspring, List<Organism> offspring,
			long generationNumber) {
		int parentsSize = parents.size();

		for (int i = 0; i < numOffspring; i++) {
			Organism parent = parents.get(params.getRandomGenerator().nextInt(
					parentsSize));
			Organism copy = parent.copy(params);
			offspring.add(copy);

			copy.setAncestory(new long[] { parent.getInnovationId() });
		}

	}

	/**
	 * Creates <code>numOffspring</code> clones of <code>Organism</code>s in the 
	 * <code>parents</code> <code>List</code>.
	 *  
	 * @param params the <code>NeatParameters</code> describing the evolution environment.
	 * @param innovations the <code>Innovations</code> for getting/creating new <code>Gene</code>s.
	 * @param scores the <code>FitnessScores</code> for the <code>Organism</code>s in the <code>List</code>
	 * of <code>Organism</code>s (<code>parents</code>).
	 * @param parents the <code>Organism</code>s to randomly select from to create the clones.
	 * @param numOffspring the number of clones to create.
	 * @param offspring the <code>List</code> of <code>Organism</code>s that are the product of applying 
	 * this operation.
	 * @param generationNumber which generation this is for (not used).
	 */	
	@Override
	protected void reproduce(NeatParameters params, Innovations innovations,
			FitnessScores scores, List<Organism> organisms,
			int numSpecieOffspring, List<Organism> newOffspring,
			long generationNumber) {
		performClone(params, innovations, scores, organisms,
				numSpecieOffspring, newOffspring, generationNumber);
	}

}
