package org.neuroph.contrib.neat.gen;

/**
 * A <code>EvolutionEventType</code> is sent to all instances of <code>
 * 
 * @author Aidan Morgan
 * 
 */
public enum EvolutionEventType {
	/** sent at the start of a generation */
	START_GENERATION,
	/** sent at the end of a generation */
	END_GENERATION,

	/** sent before selection has started */
	START_SELECTION,
	/** sent after selection has completed */
	END_SELECTION,

	/** sent before reproduction starts. */
	START_REPRODUCTION,
	/** sent after reproduction has finished */
	END_REPRODUCTION,

	/** sent before mutation starts. */
	START_MUTATION,
	/** sent after mutation has finished */
	END_MUTATION,

	/** sent before fitness calculation starts. */
	START_FITNESS_CALCULATION,
	/** sent after fitness calculation has finished */
	END_FITNESS_CALCULATION,

	/** sent before speciation starts. */
	START_SPECIATION,
	/** sent after speciation has finished */
	END_SPECIATION
}
