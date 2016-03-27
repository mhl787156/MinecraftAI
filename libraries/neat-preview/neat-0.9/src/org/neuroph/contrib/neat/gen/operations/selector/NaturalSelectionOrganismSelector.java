package org.neuroph.contrib.neat.gen.operations.selector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.Specie;
import org.neuroph.contrib.neat.gen.operations.OrganismSelector;

/**
 * <code>NaturalSelectionOrganismSelector</code> is a simple
 * <code>OrganismSelector</code> that provides elitism, survival ratio and
 * species killing.
 * 
 * @author Aidan Morgan
 */
public class NaturalSelectionOrganismSelector implements OrganismSelector {
	/**
	 * Default value for <code>survivalRatio</code>.
	 */
	public static final double DEFAULT_SURVIVAL_RATIO = 0.0;

	/**
	 * Default value for <code>maximumGenerationsSinceImprovement</code>.
	 */
	public static final int DEFAULT_MAX_GENERATIONS_SINCE_IMPROVEMENT = 50;

	/**
	 * Default value for <code>isElitismEnabled</code>.
	 */
	public static final boolean DEFAULT_IS_ELITISM_ENABLED = true;

	/**
	 * Default value for <code>isKillUnproductiveSpeciesEnabled</code>.
	 */
	public static final boolean DEFAULT_IS_KILL_UNPRODUCTIVE_SPECIES_ENABLED = false;

	/**
	 * If set to a value > 0.0, then this percentage of the population will also
	 * be automatically retained.
	 */
	private double survivalRatio = DEFAULT_SURVIVAL_RATIO;

	/**
	 * The number of generations that a <code>Specie</code> can show no
	 * improvement before it is killed off if
	 * <code>isKillUnproductiveSpeciesEnabled</code> is set to <code>true</code>
	 * .
	 */
	private int maximumGenerationsSinceImprovement = DEFAULT_MAX_GENERATIONS_SINCE_IMPROVEMENT;

	/**
	 * If set to <code>true</code> then the fittest <code>Organism</code> in
	 * each <code>Specie</code> will automatically be retained.
	 */
	private boolean isElitismEnabled = DEFAULT_IS_ELITISM_ENABLED;

	/**
	 * If set to <code>true</code> then this
	 * <code>NaturalSelectionOrganismSelector</code> will kill off any
	 * <code>Specie</code> if it has not shown any improvement in fitness value
	 * for <code>maximumGenerationsSinceImprovement</code> generations.
	 * 
	 * This does <b>NOT</b> mean that the <code>Organism</code>s are destroyed,
	 * they are just reallocated to new <code>Specie</code>s through the
	 * <code>Speciator</code>.
	 */
	private boolean isKillUnproductiveSpeciesEnabled = DEFAULT_IS_KILL_UNPRODUCTIVE_SPECIES_ENABLED;

	/**
	 * @inheritDoc
	 */
	public List<Organism> selectOrganismsForNextGeneration(
			NeatParameters params, FitnessScores fitnessScores,
			List<Specie> species, List<Organism> organisms, long generation) {
		List<Organism> survivors = new ArrayList<Organism>();

		// if we have elitism enabled, make sure we keep the fittest organism
		// of each species.
		if (isElitismEnabled) {
			for (Specie s : species) {
				Organism o = fitnessScores.getFittestOrganism(s.getOrganisms());

				if (o != null) {
					survivors.add(o);
				}
			}
		}

		Organism o = fitnessScores.getFittestOrganism(organisms);

		int numberOfOrganismsToKeep = (int) Math.round(survivalRatio
				* (params.getPopulationSize() - survivors.size()));

		while (numberOfOrganismsToKeep > 0) {
			Organism randomOrganism = organisms.get(params.getRandomGenerator()
					.nextInt(organisms.size()));

			if (!survivors.contains(randomOrganism)) {
				survivors.add(randomOrganism);
				numberOfOrganismsToKeep--;
			}
		}

		return survivors;
	}

	// track this externally to the Specie itself to avoid cluttering it up.
	// This is also a specific value for this feature, so it should not be added 
	// to the core Specie class.
	private Map<Specie, SpecieImprovementValue> specieBestGenerationMap = new HashMap<Specie, SpecieImprovementValue>();

	/**
	 * @inheritDoc
	 */
	public List<Specie> selectSpecies(NeatParameters neatParameters,
			List<Specie> species, FitnessScores fitnessScores,
			long generationNumber) {
		if (isKillUnproductiveSpeciesEnabled) {
			List<Specie> clone = new ArrayList<Specie>(species);
			List<Specie> deadSpecies = new ArrayList<Specie>();

			for (Specie s : species) {
				SpecieImprovementValue stats = specieBestGenerationMap.get(s);

				if (stats == null) {
					stats = new SpecieImprovementValue();
					specieBestGenerationMap.put(s, stats);
				}

				double maxFitness = Double.MIN_VALUE;
				for (Organism o : s.getOrganisms()) {
					maxFitness = Math.max(maxFitness, fitnessScores
							.getFitness(o));
				}

				// it's not improved in a long time, kill it off.
				if (stats.shouldKill(maxFitness, generationNumber)) {
					deadSpecies.add(s);
					specieBestGenerationMap.remove(s);
				}
			}

			clone.removeAll(deadSpecies);

			return clone;
		}

		return species;
	}

	public void setSurvivalRatio(double survivalRatio) {
		double temp = survivalRatio;

		if (survivalRatio > 1.0) {
			temp = 1.0;
		}

		if (survivalRatio < 0.0) {
			temp = 0.0;
		}

		this.survivalRatio = temp;
	}

	public double getSurvivalRatio() {
		return survivalRatio;
	}

	public int getMaximumGenerationsSinceImprovement() {
		return maximumGenerationsSinceImprovement;
	}

	public void setMaximumGenerationsSinceImprovement(
			int maximumGenerationsSinceImprovement) {
		int val = maximumGenerationsSinceImprovement;

		if (val < 0) {
			val = 0;
		}

		this.maximumGenerationsSinceImprovement = val;
	}

	public boolean killUnproductiveSpecies() {
		return isKillUnproductiveSpeciesEnabled;
	}

	public void setKillUnproductiveSpecies(
			boolean isKillUnproductiveSpeciesEnabled) {
		this.isKillUnproductiveSpeciesEnabled = isKillUnproductiveSpeciesEnabled;
	}

	public boolean isElitismEnabled() {
		return isElitismEnabled;
	}

	public void setElitismEnabled(boolean isElitismEnabled) {
		this.isElitismEnabled = isElitismEnabled;
	}

	/**
	 * A simple inner class that records the highest fitness and the generation
	 * that the highest fitness was recorded in.
	 * 
	 * @author Aidan Morgan
	 */
	private class SpecieImprovementValue {
		private double highestFitness = Double.MIN_VALUE;
		private long generation = Long.MAX_VALUE;

		public SpecieImprovementValue() {

		}

		public double getHighestFitness() {
			return highestFitness;
		}

		public long getGeneration() {
			return generation;
		}

		/**
		 * Returns <code>true</code> if the <code>Specie</code> that this
		 * <code>SpecieImprovementValue</code> is tracking has not shown
		 * improvement in <code>maximumGenerationsSinceImprovement</code>
		 * generations.
		 * 
		 * @param fitnessOfBestOrganism
		 *            the highest fitness value of the <code>Specie</code>.
		 * @param generation
		 *            which generation this is for.
		 * @return <code>true</code> if the <code>Specie</code> should be killed
		 *         off, <code>false</code> otherwise.
		 */
		public boolean shouldKill(double fitnessOfBestOrganism, long generation) {
			// if a specie has not shown improvement for
			// <code>maximumGenerationsSinceImprovement</code>
			// generations then we will kill it off. This is mainly added
			// to allow the <code>DynamicThresholdSpeciator</code> to
			// re-speciate
			// the <code>Organism</code>s into better fitting categories. It
			// also
			// prevents the elitism from continuing to keep poorly performing
			// <code>Organism</code>s in the system.
			if (fitnessOfBestOrganism > highestFitness) {
				this.highestFitness = fitnessOfBestOrganism;
				this.generation = generation;

				return false;
			}

			return (generation - this.generation) > maximumGenerationsSinceImprovement;
		}
	}
}
