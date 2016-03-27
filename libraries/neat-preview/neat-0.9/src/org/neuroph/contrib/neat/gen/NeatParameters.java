package org.neuroph.contrib.neat.gen;

import java.util.List;
import java.util.Random;

import org.neuroph.contrib.neat.gen.operations.FitnessFunction;
import org.neuroph.contrib.neat.gen.operations.MutationOperation;
import org.neuroph.contrib.neat.gen.operations.OrganismSelector;
import org.neuroph.contrib.neat.gen.operations.ReproductionOperation;
import org.neuroph.contrib.neat.gen.operations.Speciator;
import org.neuroph.contrib.neat.gen.persistence.Persistence;

/**
 * NeatParameters defines the environment and the algorithm to use for a NEAT
 * process.
 * 
 * @author Aidan Morgan
 */
public interface NeatParameters {
	/**
	 * Returns the next innovation id.
	 * 
	 * @return
	 */
	public long nextInnovationId();

	/**
	 * Returns the size of the population.
	 */
	public int getPopulationSize();

	/**
	 * Returns the <code>FitnessFunction</code> to be used for evaluating each
	 * <code>Organism</code>s <code>NeuralNetwork</code>
	 */
	public FitnessFunction getFitnessFunction();

	/**
	 * Returns the <code>OrganismSelector</code> that will select the
	 * <code>Organism</code>s to go into the next generation.
	 */
	public OrganismSelector getOrganismSelector();

	/**
	 * Returns a <code>List</code> of <code>ReproductionOperation</code>s to be
	 * performed on the <code>Organism</code>s.
	 * 
	 * <b>IMPORTANT:</b> the order of these <code>ReproductionOperation</code>
	 * is important - they are executed in order.
	 * 
	 * @return a <code>List</code> of <code>ReproductionOperation</code>s to be
	 *         performed on the <code>Organism</code>s.
	 */
	public List<ReproductionOperation> getReproductionOperators();

	/**
	 * Returns the <code>Random</code> instance used for generating random
	 * numbers.
	 * 
	 * @return the <code>Random</code> instance used for generating random
	 *         numbers.
	 */
	public Random getRandomGenerator();

	/**
	 * Returns the <code>MutationOperator</code> that will be used to perform
	 * mutations on the <code>Organism</code>s.
	 */
	public List<MutationOperation> getMutationOperators();

	/**
	 * This is a reproduction of the original NEAT C++ RandomClamped() method.
	 * It should return a value in the range of
	 * 
	 * [-1 : 1]
	 * 
	 * @return
	 */
	public double getRandomClamped();

	/**
	 * Returns the <code>NeuralNetworkBuilder</code> that is responsible for
	 * creating the <code>NeuralNetwork</code> from an <code>Organism</code>.
	 * 
	 * @return the <code>NeuralNetworkBuilder</code> that is responsible for
	 *         creating the <code>NeuralNetwork</code> from an
	 *         <code>Organism</code>.
	 */
	public NeuralNetworkBuilder getNeuralNetworkBuilder();

	/**
	 * Returns the <code>Speciator</code> that is responsible for determining
	 * the appropriate <code>Specie</code> for an <code>Organism</code>.
	 * 
	 * @return the <code>Speciator</code> that is responsible for determining
	 *         the appropriate <code>Specie</code> for an <code>Organism</code>.
	 */
	public Speciator getSpeciator();

	/**
	 * Returns the <code>Persistence</code> mechanism that is used to save the
	 * state of the evolutionary process at the end of each generation.
	 * 
	 * @return the <code>Persistence</code> mechanism that is used to save the
	 *         state of the evolutionary process at the end of each generation.
	 */
	public Persistence getPersistence();

	/**
	 * Returns the <code>TerminationCondition</code> that determines when the
	 * evolutionary process should be halted.
	 * 
	 * @return the <code>TerminationCondition</code> that determines when the
	 *         evolutionary process should be halted.
	 */
	public TerminationCondition getTerminationCondition();

	/**
	 * Calling this allows the starting number of the innovation id's to be reset
	 * from zero, to the provided value.
	 * 
	 * It is the responsibility of the caller of this method to ensure that the
	 * provided id is not already assigned to something else inside the context
	 * of this <code>NeatParameters</code>.
	 * 
	 * @param l the id to start from.
	 */
	public void setInnovationIdsStart(long l);
}
