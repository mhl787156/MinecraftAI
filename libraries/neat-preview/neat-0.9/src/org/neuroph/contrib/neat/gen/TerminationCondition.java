package org.neuroph.contrib.neat.gen;

/**
 * Simple interface that is used by the <code>Evolver</code> to determine when
 * the evolutionary process should be halted.
 * 
 * @author Aidan Morgan
 */
public interface TerminationCondition {
	/**
	 * Returns <code>true</code> if the evolutionary process should be
	 * considered completed.
	 * 
	 * @param neatParameters
	 *            the <code>NeatParameters</code> describing the evolution
	 *            environment.
	 * @param generationNumber
	 *            the number of the generation that was just completed.
	 * @param currentGeneration
	 *            the current <code>Generation</code> which contains the
	 *            <code>Organism</code>s which make up the generation.
	 * @param fitness
	 *            the <code>FitnessScores</code> for the current generation.
	 * 
	 * @return <code>true</code> if the evolution should finish,
	 *         <code>false</code> otherwise.
	 */
	public boolean exitCriteriaMet(NeatParameters neatParameters,
			int generationNumber, Generation currentGeneration,
			FitnessScores fitness);
}
