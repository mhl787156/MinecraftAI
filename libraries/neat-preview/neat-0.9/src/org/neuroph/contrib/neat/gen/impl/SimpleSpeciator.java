package org.neuroph.contrib.neat.gen.impl;

import java.util.List;
import java.util.logging.Logger;

import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.Specie;
import org.neuroph.contrib.neat.gen.operations.Speciator;

/**
 * Implementation of the <code>Speciator</code> interface which provides the same
 * speciation behaviour as Ken Stanley's implementation of NEAT.
 * 
 * This Speciator will create a comparison score between two <code>Organism</code>s 
 * based on their <code>Gene</code> structure. 
 * 
 * @author Aidan Morgan
 */
public class SimpleSpeciator implements Speciator {
	private static Logger s_log = Logger.getLogger(SimpleSpeciator.class
			.getName());

	public static final double DEFAULT_DISJOINT_MULTIPLIER = 1;
	public static final double DEFAULT_EXCESS_MULTIPLIER = 1;
	public static final double DEFAULT_MATCHED_MULTIPLIER = 0.4;

	public static final double DEFAULT_COMPATABILITY_THRESHOLD = 0.26;

	/**
	 * How much to multiply the disjoint comparison score by.
	 */
	private double disjointMultiplier = DEFAULT_DISJOINT_MULTIPLIER;

	/**
	 * How much to multiply the excess comparison score by.
	 */
	private double excessMultiplier = DEFAULT_EXCESS_MULTIPLIER;
	
	/**
	 * How much to multiply the matched comparison score by.
	 */
	private double matchedMultiplier = DEFAULT_MATCHED_MULTIPLIER;
	
	/**
	 * The maximum value for the compatability score, any value less than this 
	 * will consider two <code>Organism</code>s the same.
	 */
	private double compatabilityThreshold = DEFAULT_COMPATABILITY_THRESHOLD;

	public void speciate(NeatParameters params, List<Specie> species,
			FitnessScores fitnessScores, List<Organism> chromosomes) {
		int newSpeciesCreated = 0;

		for (Organism o : chromosomes) {
			Specie parentSpecie = null;

			// iterate all species, comparing with the representative organism,
			// or until
			// we have found a suitable parent specie.
			for (int i = 0; i < species.size() && parentSpecie == null; i++) {
				Specie s = species.get(i);

				if (o.equals(s.getRepresentativeOrganism())) {
					parentSpecie = s;
				} else {
					double score = compare(s.getRepresentativeOrganism(), o);

					if (score < compatabilityThreshold) {
						parentSpecie = s;
					}
				}
			}

			// if we haven't found a specie for this chromosome, then we create
			// a
			// new one.
			if (parentSpecie == null) {
				parentSpecie = new Specie(params);
				species.add(parentSpecie);

				newSpeciesCreated++;
			}

			parentSpecie.addOrganism(o);
		}
	}

	public double compare(Organism one, Organism two) {
		// travel down the length of each genome counting the number of
		// disjoint genes, the number of excess genes and the number of
		// matched genes
		double NumDisjoint = 0;
		double NumExcess = 0;
		double NumMatched = 0;

		// this records the summed difference of weights in matched genes
		double weightDifference = 0;

		// position holders for each genome. They are incremented as we
		// step down each genomes length.
		int g1 = 0;
		int g2 = 0;

		while ((g1 < one.getConnections().size() - 1)
				|| (g2 < two.getConnections().size() - 1)) {
			// we've reached the end of genome1 but not genome2 so increment
			// the excess score
			if (g1 == one.getConnections().size() - 1) {
				++g2;
				++NumExcess;

				continue;
			} else if (g2 == two.getConnections().size() - 1) {
				++g1;
				++NumExcess;

				continue;
			} else {

				// get innovation numbers for each gene at this point
				long id1 = one.getConnections().get(g1).getInnovationId();
				long id2 = two.getConnections().get(g2).getInnovationId();

				// innovation numbers are identical so increase the matched
				// score
				if (id1 == id2) {
					++g1;
					++g2;
					++NumMatched;

					// get the weight difference between these two genes
					weightDifference += Math.abs(one.getConnections().get(g1)
							.getWeight()
							- two.getConnections().get(g2).getWeight());
				}

				// innovation numbers are different so increment the disjoint
				// score
				if (id1 < id2) {
					++NumDisjoint;
					++g1;
				}

				if (id1 > id2) {
					++NumDisjoint;
					++g2;
				}
			}

		}// end while

		// get the length of the longest genome
		int longest = Math.max(one.getGenes().size(), two.getGenes().size());

		double excessScore = NumExcess / longest;
		double disjointScore = NumDisjoint / longest;
		double matchedScore = weightDifference / NumMatched;

		if (Double.isNaN(matchedScore)) {
			throw new IllegalStateException(
					"Invalid Organism comparison: [excessScore = "
							+ excessScore + " disjointScore = " + disjointScore
							+ " matchedScore = " + matchedScore + "].");
		}

		// finally calculate the scores
		return (excessMultiplier * excessScore)
				+ (disjointMultiplier * disjointScore)
				+ (matchedMultiplier * matchedScore);
	}

	public double getDisjointMultiplier() {
		return disjointMultiplier;
	}

	public double getExcessMultiplier() {
		return excessMultiplier;
	}

	public double getMatchedMultiplier() {
		return matchedMultiplier;
	}

	public double getCompatabilityThreshold() {
		return compatabilityThreshold;
	}

	public void setDisjointMultiplier(double disjointMultiplier) {
		this.disjointMultiplier = disjointMultiplier;
	}

	public void setExcessMultiplier(double excessMultiplier) {
		this.excessMultiplier = excessMultiplier;
	}

	public void setMatchedMultiplier(double matchedMultiplier) {
		this.matchedMultiplier = matchedMultiplier;
	}

	public void setCompatabilityThreshold(double compatabilityThreshold) {
		this.compatabilityThreshold = compatabilityThreshold;
	}
}
