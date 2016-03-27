package org.neuroph.contrib.neat.gen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The <code>Innovations</code> is a simple class that monitors the creation of
 * <code>ConnectionGene</code> and <code>NeuronGene</code>s as part of the
 * evolution process.
 * 
 * If a <code>Gene</code> is attempted to be added by one of the
 * <code>MutationOperator</code>s then this class is first checked to see if the
 * mutation was tried before. If it was tried before then the old
 * <code>Gene</code> is retrieved from this class and used instead of creating a
 * new <code>Gene</code>.
 * 
 * @author Aidan Morgan
 */
public class Innovations implements Serializable {
	/**
	 * Serialization identifier.
	 */
	private static final long serialVersionUID = 216047023917140135L;

	/**
	 * Convenience method that will create a new <code>Innovations</code> that
	 * only contains the elements that are in <code>current</code> <b>and
	 * not</b> in <code>base</code>.
	 * 
	 * @param base
	 *            the <code>Innovations</code> to use as the base model, all
	 *            elements in this <code>Innovations</code> will <b>NOT</b> be
	 *            present in the returned <code>Innovations</code>.
	 * 
	 * @param current
	 *            the <code>Innovations</code> to check for new entries (i.e.
	 *            entries that are not in the <code>base</code>
	 *            </code>Innovations</code> map.
	 * 
	 * @return a new <code>Innovations</code> instance that contains only the
	 *         entries that have been added to <code>current</code> in
	 *         comparison to <code>base</code>.
	 */
	public static Innovations calculateAdded(Innovations base,
			Innovations current) {
		Map<ConnectionKey, ConnectionGene> connectionClone = new HashMap<ConnectionKey, ConnectionGene>(
				current.connectionInnovations);
		for (ConnectionKey k : base.connectionInnovations.keySet()) {
			connectionClone.remove(base.connectionInnovations.get(k));
		}

		Map<NeuronKey, NeuronGene> neuronClone = new HashMap<NeuronKey, NeuronGene>(
				current.neuronInnovations);
		for (NeuronKey k : base.neuronInnovations.keySet()) {
			neuronClone.remove(base.neuronInnovations.get(k));
		}
		
		Map<Long, Innovation> innovationsClone = new HashMap<Long, Innovation>(current.innovationIds);
		for(Long l : base.innovationIds.keySet()) {
			innovationsClone.remove(base.innovationIds.get(l));
		}

		Innovations ret = new Innovations();
		ret.neuronInnovations = neuronClone;
		ret.connectionInnovations = connectionClone;
		ret.innovationIds = innovationsClone;

		return ret;
	}

	public static void append(Innovations base, Innovations toAdd) {
		base.connectionInnovations.putAll(toAdd.connectionInnovations);
		base.neuronInnovations.putAll(toAdd.neuronInnovations);
		base.innovationIds.putAll(toAdd.innovationIds);
		
	}

	/**
	 * A <code>Map</code> of <code>ConnectionKey</code> to
	 * <code>ConnectionGene</code>.
	 */
	private Map<ConnectionKey, ConnectionGene> connectionInnovations;

	/**
	 * A <code>Map</code> of <code>NeuronKey</code> to <code>NeuronGene</code>.
	 */
	private Map<NeuronKey, NeuronGene> neuronInnovations;

	/**
	 * A <code>Map</code> of innovation id to the <code>Innovation</code> that has that id.
	 */
	private Map<Long, Innovation> innovationIds;
	
	/**
	 * Constructor.
	 */
	public Innovations() {
		connectionInnovations = new HashMap<ConnectionKey, ConnectionGene>();
		neuronInnovations = new HashMap<NeuronKey, NeuronGene>();
		innovationIds = new HashMap<Long, Innovation>();
	}

	/**
	 * Retrieves a <code>ConnectionGene</code> connecting the two provided
	 * neuron id's, <code>null</code> if no connection has been created before.
	 * 
	 * @param originId
	 *            the id of the neuron the connection is starting at.
	 * @param endpointId
	 *            the id of the neuron the connection is ending at.
	 * @return a <code>ConnectionGene</code> connecting the two provided neuron
	 *         id's, <code>null</code> if no connection has been created before.
	 */
	public ConnectionGene getConnectionGene(long originId, long endpointId) {
		ConnectionKey ck = new ConnectionKey(originId, endpointId);
		return connectionInnovations.get(ck);
	}

	/**
	 * Stores the provided <code>ConnectionGene</code> in this
	 * <code>Innovations</code> map.
	 * 
	 * @param cg
	 *            the <code>ConnectionGene</code> to store.
	 */
	public void putConnectionGene(ConnectionGene cg) {
		ConnectionKey ck = new ConnectionKey(cg.getOriginId(), cg
				.getEndpointId());

		if (connectionInnovations.containsKey(ck)) {
			throw new IllegalArgumentException(
					"Attempting to register Connection innovation "
							+ cg.getInnovationId()
							+ " but one is already registered.");
		}

		connectionInnovations.put(ck, cg);
		
		// record the connection so we can do a reverse lookup later.
		registerInnovation(cg);
	}

	/**
	 * Retrieves a <code>NeuronGene</code> that would be created by splitting
	 * the connection with the provided connection id, <code>null</code> if no
	 * <code>NeuronGene</code> was created before.
	 * 
	 * @param connectionId
	 *            the id of the <code>ConnectionGene</code> to be split for
	 *            creating the <code>NeuronGene</code>.
	 * @return a <code>NeuronGene</code> that would be created by splitting the
	 *         connection with the provided connection id, <code>null</code> if
	 *         no <code>NeuronGene</code> was created before.
	 */
	public NeuronGene getNeuronGene(long connectionId) {
		NeuronKey nk = new NeuronKey(connectionId);
		return neuronInnovations.get(nk);
	}

	/**
	 * Stores the <code>NeuronGene</code> in this <code>Innovations</code>.
	 * 
	 * A <code>NeuronGene</code> innovation requires the id of the connection that was
	 * split in order to create it. The add neuron mutation operation initially identifies
	 * a connection, and then splits it into two connections, adding a neuron in the middle.
	 * The <code>connectionId</code> parameter is the id of the connection that was split
	 * (and subsequently removed) for the provided <code>NeuronGene</code> to be inserted.
	 *
	 * @param connectionId the id of the connection that was split
	 * (and subsequently removed) for the provided <code>NeuronGene</code> to be inserted.
	 * @param ng the <code>NeuronGene</code> that was inserted.
	 */
	public void putNeuronGene(long connectionId, NeuronGene ng) {
		NeuronKey nk = new NeuronKey(connectionId);

		if (neuronInnovations.containsKey(nk)) {
			throw new IllegalArgumentException(
					"Attempting to register Neuron innovation "
							+ ng.getInnovationId()
							+ " but one is already registered.");
		}

		neuronInnovations.put(nk, ng);
		
		// record the neuron genes so we can do a reverse lookup later.
		registerInnovation(ng);
	}
	
	/**
	 * Returns the innovation id of the <code>ConnectionGene</code> that was split
	 * when the provided <code>NeuronGene</code> was added to the <code>Organism</code>.
	 * 
	 * @param ng the <code>NeuronGene</code> to find the associated split <code>ConnectionGene</code>
	 * for.
	 * 
	 * @return the innovation id of the <code>ConnectionGene</code> that was split
	 * when the provided <code>NeuronGene</code> was added to the <code>Organism</code>.
	 */
	public long getConnectionInnovationForNeuron(NeuronGene ng) {
		for(NeuronKey key : neuronInnovations.keySet()) {
			NeuronGene temp = neuronInnovations.get(key);
			
			if(temp.getInnovationId() == ng.getInnovationId()) {
				return key.getConnectionId();
			}
		}
		
		return -1;
	}

	public List<Gene> getGenes() {
		List<Gene> genes = new ArrayList<Gene>();
		genes.addAll(connectionInnovations.values());
		genes.addAll(neuronInnovations.values());
		
		return genes;
	}

	public List<ConnectionGene> getConnectionGenes() {
		return new ArrayList<ConnectionGene>(connectionInnovations.values());
	}

	public List<NeuronGene> getNeuronGenes() {
		return new ArrayList<NeuronGene>(neuronInnovations.values());
	}

	public Innovation getInnovative(long innovationId) {
		return innovationIds.get(innovationId);
	}
	
	public List<NeuronGene> getRecordedNeuronGenes() {
		List<NeuronGene> genes = new ArrayList<NeuronGene>();
		
		for(Innovation g : innovationIds.values()) {
			if(g instanceof NeuronGene) {
				NeuronGene ng = (NeuronGene)g;
				
				if(!neuronInnovations.containsValue(ng)) {
					genes.add(ng);
				}
			}
		}
		
		return genes;
	}

	public List<ConnectionGene> getRecordedConnectionGenes() {
		List<ConnectionGene> genes = new ArrayList<ConnectionGene>();
		
		for(Innovation g : innovationIds.values()) {
			if(g instanceof ConnectionGene) {
				ConnectionGene ng = (ConnectionGene)g;
				ConnectionKey key = new ConnectionKey(ng.getOriginId(), ng.getEndpointId());
				
				if(!connectionInnovations.containsKey(key)) {
					genes.add(ng);
				}
			}
		}
		
		return genes;
	}	

	public void registerInnovation(Innovation i) {
		registerInnovations(Arrays.asList(i));
	}

	/**
	 * Called at the end of a generation to make sure that all of the <code>Innovative</code>
	 * implementations are indexed in this class.
	 * 
	 * @param g the <code>Generation</code> to update for.
	 */
	public void updateFromGeneration(Generation g) {
		for(Specie s : g.getSpecies()) {
			for(Organism o : s.getOrganisms()) {
				registerInnovations(o.getGenes());
			}
		}
		
	}

	/**
	 * Register the provided <code>List</code> of innovations with this object. 
	 * @param connections the <code>List</code> of <codE>Innovation</code>s to register.
	 */
	public void registerInnovations(List<? extends Innovation> connections) {
		for(Innovation i : connections) {
			if(!innovationIds.containsKey(i.getInnovationId())) {
				this.innovationIds.put(i.getInnovationId(), i);
			}
			else {
				Innovation o = innovationIds.get(i.getInnovationId());
				
				if(!i.equals(o)) {
					throw new IllegalStateException("Duplicate innovation id ("+i.getInnovationId()+") found: " + o.getClass().getName() + " and " + i.getClass().getName() + ".");
				}
			}
			
		}
		
	}
	
	
	/**
	 * Simple class that is used as a <code>Map</code> key for the
	 * <code>ConnectionGene</code>.
	 * 
	 * As <code>ConnectionGene</code>s are uniquely identified by their
	 * <code>originId</code> and <code>endpointId</code> this class is used to
	 * combine them into a single hash value.
	 * 
	 * @author Aidan Morgan
	 */
	private class ConnectionKey implements Serializable {
		private long originId;
		private long endpointId;

		public ConnectionKey(long originId, long endpointId) {
			this.originId = originId;
			this.endpointId = endpointId;
		}

		public long getOriginId() {
			return originId;
		}

		public long getEndpointId() {
			return endpointId;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (endpointId ^ (endpointId >>> 32));
			result = prime * result + (int) (originId ^ (originId >>> 32));
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
			ConnectionKey other = (ConnectionKey) obj;
			if (endpointId != other.endpointId)
				return false;
			if (originId != other.originId)
				return false;
			return true;
		}
	}

	/**
	 * Simple class that is used as a <code>Map</code> key for the
	 * <code>NeuronGene</code>.
	 * 
	 * This class isn't really needed, however I added it for interface
	 * consistency with the <code>ConnectionKey</code>.
	 * 
	 * @author Aidan Morgan
	 */
	private class NeuronKey implements Serializable {
		private long connectionId;

		public NeuronKey(long connId) {
			this.connectionId = connId;
		}

		public long getConnectionId() {
			return connectionId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ (int) (connectionId ^ (connectionId >>> 32));
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
			NeuronKey other = (NeuronKey) obj;
			if (connectionId != other.connectionId)
				return false;
			return true;
		}
	}	
}
