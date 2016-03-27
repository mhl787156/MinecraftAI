package org.neuroph.contrib.neat.gen.operations.speciator;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.Specie;

/**
 * A <code>DynamicThresholdSpeciator</code> is a <code>Speciator</code> that
 * will adjust the comparison threshold in an attempt to keep the number of
 * <code>Specie</code>s within a certain range.
 * 
 * @author Aidan Morgan
 */
public class DynamicThresholdSpeciator extends SimpleSpeciator {
	private static Logger s_log = Logger
			.getLogger(DynamicThresholdSpeciator.class.getName());

	/**
	 * The default ratio of population size to use if the maximum species value
	 * is not set.
	 */
	public static final double DEFAULT_MAX_SPECIES_RATIO = 0.06;

	/**
	 * The default value to adjust <code>dynamicFitness</code> by if the
	 * required number of species cannot be calculated.
	 */
	public static final double DEFAULT_THRESHOLD_ADJUSTMENT_RATE = 0.5;

	/**
	 * The default threshold to use for equality.
	 */
	public static final double DEFAULT_THRESHOLD = 0.3;

	/**
	 * The ratio of population size to use for determining the maximum number of
	 * <code>Specie</code>s if <code>maxSpecies</code> is not set.
	 */
	private double maxSpeciesRatio = DEFAULT_MAX_SPECIES_RATIO;

	/**
	 * The value to adjust <code>dynamicThreshold</code> by in an effort to get
	 * the required number of <code>Specie</code>s.
	 */
	private double thresholdCompatabilityAdjustmentRate = DEFAULT_THRESHOLD_ADJUSTMENT_RATE;

	/**
	 * The threshold value to use for <code>Organism</code> comparison.
	 */
	private double dynamicThreshold = DEFAULT_THRESHOLD;

	/**
	 * If set, the maximum number of <code>Specie</code>s that should be
	 * created.
	 */
	private int maxSpecies = -1;

	public DynamicThresholdSpeciator() {

	}

	/**
	 * Helper method to determine the maximum number of <code>Specie</code>s
	 * that should be created.
	 * 
	 * @param params
	 *            the <code>NeatParameters</code> that describe the evolution
	 *            environment.
	 * 
	 * @return the maximum number of <code>Specie</code>s that should be
	 *         created.
	 */
	private int getMaxSpecies(NeatParameters params) {
		if (maxSpecies == -1) {
			return (int) Math
					.ceil(maxSpeciesRatio * params.getPopulationSize());
		}

		return maxSpecies;
	}

	/**
	 * @iheritDoc
	 */
	@Override
	public void speciate(NeatParameters params, List<Specie> species,
			FitnessScores fitnessScores, List<Organism> chromosomes) {
		int maxSpecies = getMaxSpecies(params);

		List<Specie> addedSpecies = new ArrayList<Specie>();
		int sizeBeforeSpeciation = species.size();

		// keep trying different threshold methods until we have found a
		// suitable specie size.
		do {
			// undo the results of the last speciation attempt.
			species.removeAll(addedSpecies);
			addedSpecies = super.performSpeciation(params, species,
					fitnessScores, chromosomes, dynamicThreshold);

			// we are above the max number of species, so increase the
			// comparison threshold.
			if (addedSpecies.size() + sizeBeforeSpeciation > maxSpecies) {
				dynamicThreshold += thresholdCompatabilityAdjustmentRate;
			} else {
				// just do this to try and keep the number down anyway.
				dynamicThreshold -= thresholdCompatabilityAdjustmentRate;
			}

			if (dynamicThreshold < 0) {
				dynamicThreshold = DEFAULT_THRESHOLD;
			}
		} while ((addedSpecies.size() + sizeBeforeSpeciation > maxSpecies));
	}

	public double getMaxSpeciesRatio() {
		return maxSpeciesRatio;
	}

	public void setMaxSpeciesRatio(double maxSpeciesRatio) {
		this.maxSpeciesRatio = maxSpeciesRatio;
	}

	public int getMaxSpecies() {
		return maxSpecies;
	}

	public void setMaxSpecies(int maxSpecies) {
		int val = maxSpecies;

		// if the user attempts to set the maximum species value
		// to less than 1, just reset and use the ratio.
		if (val < 1) {
			val = -1;
		}

		this.maxSpecies = val;
	}

	public double getThresholdCompatabilityAdjustmentRate() {
		return thresholdCompatabilityAdjustmentRate;
	}

	public void setThresholdCompatabilityAdjustmentRate(
			double thresholdCompatabilityAdjustmentRate) {
		this.thresholdCompatabilityAdjustmentRate = thresholdCompatabilityAdjustmentRate;
	}

}
