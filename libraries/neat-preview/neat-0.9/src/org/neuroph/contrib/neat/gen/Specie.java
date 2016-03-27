package org.neuroph.contrib.neat.gen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.neuroph.contrib.neat.gen.util.OrganismHelper;

/**
 * A <code>Specie</code> represents a collection of <code>Organism</code>s that
 * are deemed similar by the <code>Speciator</code>.
 * 
 * It is also responsible for storing the fitness of each <code>Organism</code>
 * during the evaulation process.
 * 
 * @author Aidan Morgan
 */
public class Specie implements Innovation, Serializable {
	private static final long serialVersionUID = -1807782907280927450L;

	/**
	 * The innovation id of this <code>Specie</code>.
	 */
	private long specieId;

	/**
	 * A <code>List</code> of <code>Organism</code>s that are in this
	 * <code>Specie</code>.
	 */
	private List<Organism> organisms;

	/**
	 * A <code>Map</code> of <code>Organism</code> innovation id to fitness
	 * score.
	 */
	// private Map<Long, Double> fitnessMap;

	/**
	 * The <code>NeatParameters</code> that define the environment this
	 * <code>Specie</code> is operating in.
	 */
	private NeatParameters neatParams;

	/**
	 * Whether this <code>Specie</code> is dead or not.
	 */
	private boolean isDead;

	/**
	 * The <code>Organism</code> that best represents this <code>Specie</code>.
	 */
	private Organism representativeOrganism;

	/**
	 * Constructor.
	 * 
	 * @param params
	 *            the <code>NeatParameters</code> that define the environment
	 *            this <code>Specie</code> is operating in.
	 */
	public Specie(NeatParameters params) {
		this(params, new ArrayList<Organism>());
	}

	/**
	 * Constructor
	 * 
	 * @param params
	 *            the <code>NeatParameters</code> that define the environment
	 *            this <code>Specie</code> is operating in.
	 * @param organisms
	 *            a <code>List</code> of <code>Organism</code>s that make up
	 *            this <code>Specie</code>.
	 */
	public Specie(NeatParameters params, List<Organism> organisms) {
		this(params.nextInnovationId(), organisms);
	}

	public Specie(long specieId, List<Organism> organisms) {
		this.specieId = specieId;
		this.organisms = new ArrayList<Organism>();
		
		for(Organism o : organisms) {
			addOrganism(o);
		}
	}
	
	/**
	 * @inheritDoc
	 */
	public long getInnovationId() {
		return specieId;
	}

	/**
	 * Returns the <code>Organism</code>s that are determined to be part of this
	 * <code>Specie</code>.
	 * 
	 * @return the <code>Organism</code>s that are determined to be part of this
	 *         <code>Specie</code>.
	 */
	public List<Organism> getOrganisms() {
		return Collections.unmodifiableList(organisms);
	}

	/**
	 * Adds the provided <code>Organism</code> to this <code>Specie</code>.
	 * 
	 * @param o
	 *            the <code>Organism</code> to add to this <code>Specie</code>.
	 */
	public void addOrganism(Organism o) {
		if (o == null) {
			throw new IllegalArgumentException("Organism cannot be null.");
		}

		// This is a special case. If the organism being added is the first
		// organism then we know
		// this is a brand new Specie, so we have no way of knowing what the
		// best organism
		// actually is, so just take the first one as the representative.
		if (organisms.isEmpty()) {
			representativeOrganism = o;
		}			
		
		if (!organisms.contains(o)) {
			organisms.add(o);
			o.setSpecies(this);
			
			// double check that we still have a representative organism set.
			if(representativeOrganism == null) {
				representativeOrganism = organisms.get(0);
			}
		}
	}

	/**
	 * Remove any <code>Organism</code>s that are in this <code>Specie</code>
	 * that are not in the provided list of survivors.
	 * 
	 * @param o
	 *            a <code>List</code> of <code>Organism</code>s to keep, all
	 *            others are killed off.
	 */
	public void cull(List<Organism> org) {
		for (Organism o : organisms) {
			if (!org.contains(o)) {
				o.setSpecies(null);
			}
		}

		organisms.retainAll(org);
		
		// reset the representative organism to the first one in the list...
		if(!org.contains(representativeOrganism)) {
			representativeOrganism = org.get(0);
		}
	}

	/**
	 * Returns the number of <code>Organism</code>s in this <code>Specie</code>.
	 * 
	 * @return the number of <code>Organism</code>s in this <code>Specie</code>.
	 */
	public int getOrganismCount() {
		return organisms.size();
	}

	/**
	 * Sets whether this <code>Specie</code> is dead.
	 * 
	 * @param b
	 *            <code>true</code> if this <code>Specie</code> is dead,
	 *            <code>false</code> otherwise.
	 */
	public void setDead(boolean b) {
		this.isDead = b;
	}

	/**
	 * Returns whether this <code>Specie</code> is dead.
	 * 
	 * @return <code>true</code> if this <code>Specie</code> is dead,
	 *         <code>false</code> otherwise.
	 */
	public boolean isDead() {
		return isDead;
	}

	public Specie copy() {
		List<Organism> clones = OrganismHelper.copy(neatParams, organisms);

		Specie s = new Specie(specieId, clones);
		// s.fitnessMap = new HashMap<Long, Double>(this.fitnessMap);

		// make sure we maintain the link to the specie, but to the clone of the
		// specie now.
		for (Organism o : clones) {
			o.setSpecies(s);
		}

		return s;
	}

	public Organism getRepresentativeOrganism() {
		return representativeOrganism;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (specieId ^ (specieId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Specie other = (Specie) obj;
		if (specieId != other.specieId)
			return false;
		return true;
	}

	/**
	 * Callback to inform the <code>Specie</code> about which stage the
	 * evolution process is in.
	 * 
	 * @param type
	 *            the <code>EvolutionEventType</code> describing the stage.
	 */
	public void update(EvolutionEventType type) {
		// notify all organisms in this specie of the change.
		for (Organism o : organisms) {
			o.update(type);
		}
	}
}
