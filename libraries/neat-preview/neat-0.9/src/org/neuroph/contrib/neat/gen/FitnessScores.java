package org.neuroph.contrib.neat.gen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.neuroph.contrib.neat.gen.util.LRUCache;

/**
 * A simple cache that stores all of the fitness scores for the evolutionary
 * process.
 * 
 * @author Aidan Morgan
 */
public class FitnessScores implements Serializable {
	/**
	 * Serialization identifier.
	 */
	private static final long serialVersionUID = -8240470644092428521L;

	/**
	 * How many generations that should be kept in the cache.
	 */
	private static final int DEFAULT_GENERATIONS_TO_KEEP = 2;

	/**
	 * Convenience method that will create a new <code>FitnessScores</code> that
	 * only contains the elements that are in <code>current</code> <b>and
	 * not</b> in <code>original</code>.
	 * 
	 * @param original
	 *            the <code>FitnessScores</code> to use as the base model, all
	 *            elements in this <code>FitnessScores</code> will <b>NOT</b> be
	 *            present in the returned <code>FitnessScores</code>.
	 * 
	 * @param current
	 *            the <code>FitnessScores</code> to check for new entries (i.e.
	 *            entries that are not in the <code>original</code>
	 *            </code>FitnessScores</code> map.
	 * 
	 * @return a new <code>FitnessScores</code> instance that contains only the
	 *         entries that have been added to <code>current</code> since
	 *         <code>original</code>.
	 */
	public static FitnessScores computeAdded(FitnessScores original,
			FitnessScores current) {
		FitnessScores scores = new FitnessScores(current.getCacheSize());
		LRUCache<Long, Double> currentClone = new LRUCache<Long, Double>(current.getCacheSize());
		currentClone.removeAll(original.fitnessMap);
		scores.fitnessMap = currentClone;

		return scores;
	}

	/**
	 * Convenience method that will add all of the fitness scores in
	 * <code>toAdd</code> to the <code>base</code> <code>FitnessScores</code>
	 * map.
	 * 
	 * @param base
	 *            the base <code>FitnessScores</code> instance, to which all of
	 *            the entries in <code>toAdd</code> will be added.
	 * @param toAdd
	 *            the <codE>FitnessScores</code> to add to the <code>base</code>
	 *            <code>FitnessScores</code>.
	 */
	public static void append(FitnessScores base, FitnessScores toAdd) {
		base.fitnessMap.addAll(toAdd.fitnessMap);
	}

	/**
	 * The <code>LRUCache</code> (least recently used cache) for storing the
	 * fitness values.
	 */
	private LRUCache<Long, Double> fitnessMap;

	private int cacheSize;

	/**
	 * Constructor.
	 * 
	 * @param params
	 *            the <code>NeatParameters</code> that describe the evolution
	 *            environment.
	 */
	public FitnessScores(NeatParameters params) {
		this(params, DEFAULT_GENERATIONS_TO_KEEP);
	}

	/**
	 * Constructor.
	 * 
	 * @param params
	 *            the <code>NeatParameters</code> that describe the evolution
	 *            environment.
	 * @param numGenerationsToKeep
	 *            the number of generations to keep in the <code>LRUCache</code>
	 *            .
	 */
	public FitnessScores(NeatParameters params, int numGenerationsToKeep) {
		this.cacheSize = params.getPopulationSize() * numGenerationsToKeep;
		this.fitnessMap = new LRUCache<Long, Double>(cacheSize);
	}

	public FitnessScores(int cacheSize) {
		this.fitnessMap = new LRUCache<Long, Double>(cacheSize);
		this.cacheSize = cacheSize;
	}

	/**
	 * Store the fitness value for the provided <code>Organism</code> in the
	 * cache.
	 * 
	 * @param o
	 *            the <code>Organism</code> to store the fitness value for.
	 * @param fitness
	 *            the score for the provided <code>Organism</code>.
	 */
	public void setFitness(Organism o, double fitness) {
		setFitness(o.getInnovationId(), fitness);
	}

	/**
	 * Returns the fitness value for the provided <code>Organism</code>.
	 * 
	 * @param o
	 *            the <code>Organism</code> to get the fitness score for.
	 * @return the fitness score for the provided <code>Organism</code>.
	 */
	public double getFitness(Organism o) {
		if(!fitnessMap.containsKey(o.getInnovationId())) {
			throw new IllegalArgumentException("Could not find Organism with innovation id " + o.getInnovationId() + ".");
		}
		
		return fitnessMap.get(o.getInnovationId());
	}

	/**
	 * Determines the <code>Organism</code> from the provided <code>List</code>
	 * which has the highest fitness score.
	 * 
	 * @param organisms
	 *            a <code>List</code> of <code>Organism</code>s to find the
	 *            fittest one from.
	 * @return the <code>Organism</code> from the provided <code>List</code>
	 *         with the highest fitness.
	 */
	public Organism getFittestOrganism(List<Organism> organisms) {
		Organism fittest = null;
		double best = Double.MIN_VALUE;

		for (Organism o : organisms) {
			if (fittest == null) {
				best = getFitness(o);
				fittest = o;
			} else {
				double temp = getFitness(o);

				if (temp > best) {
					best = temp;
					fittest = o;
				}
			}
		}

		return fittest;
	}

	/**
	 * Returns the highest fitness value.
	 * 
	 * @return the highest fitness value in this cache.
	 */
	public double getBestFitness() {
		double max = Double.MIN_VALUE;

		for (Double d : fitnessMap.values()) {
			max = Math.max(d, max);
		}

		return max;
	}

	/**
	 * Returns the <u>total</u> fitness for all of the <code>Organism</code>s in
	 * the provided <code>List</code>.
	 * 
	 * @param organisms
	 *            the <code>List</code> of <code>Organism</code>s to get the
	 *            total fitness for.
	 * @return the <u>total</u> fitness for all of the <code>Organism</code>s in
	 *         the provided <code>List</code>.
	 */
	public double getFitness(List<Organism> organisms) {
		double total = 0.0;

		for (Organism o : organisms) {
			total += getFitness(o);
		}

		return total;
	}
	
	public List<Long> getOrganismIds() {
		return new ArrayList<Long>(fitnessMap.keySet());
	}
	
	public Double getFitnessForOrganism(long id) {
		return fitnessMap.get(id);
	}

	public void setFitness(long longValue, double doubleValue) {
		fitnessMap.put(longValue, doubleValue);
	}

	public int getCacheSize() {
		return cacheSize;
	}

	public long getLastOrganismId() {
		List<Long> keys = new ArrayList<Long>(fitnessMap.keySet());
		Collections.sort(keys);
		
		if(keys.isEmpty()) {
			throw new IllegalStateException("Cannot get last organism id from an empty FitnessScores.");
		}
		return keys.get(keys.size() - 1);
	}
}
