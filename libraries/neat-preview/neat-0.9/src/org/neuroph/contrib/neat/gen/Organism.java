package org.neuroph.contrib.neat.gen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.neuroph.contrib.neat.gen.util.GeneHelper;

/**
 * An <code>Organism</code> is a member of the genetic population that is
 * responsible for creating a <code>NeuralNetowork</code>.
 * 
 * The <code>Organism</code> is also what is mutated and crossed-over when we
 * need to evolve the population.
 * 
 * An <code>Organism</code> contains a <code>List</code> of
 * <code>ConnectionGene</code> and <code>NeuronGene</code>'s as the
 * representation of it's "genetic material".
 * 
 * @author Aidan Morgan
 */
public class Organism implements Innovation, Serializable {
	private static final long serialVersionUID = 27083115937924179L;

	/**
	 * The unique id of this organism.
	 */
	long organismId;

	/**
	 * A <code>List</code> of all <code>ConnectionGene</code>s in this
	 * <code>Organism</code>.
	 */
	private List<ConnectionGene> connections;

	/**
	 * A <code>Map</code> of <code>ConnectionGene</code> id to the
	 * <code>ConnectionGene</code> instance to facillitate fast by-id lookups.
	 */
	private Map<Long, ConnectionGene> connectionsMap = new HashMap<Long, ConnectionGene>();

	/**
	 * A <code>List</code> of all <code>NeuronGene</code>s in the input layer.
	 */
	private List<NeuronGene> inputNeurons;

	/**
	 * A <code>List</code> of all <code>NeuronGene</code>s in the hidden layer.
	 */
	private List<NeuronGene> hiddenNeurons;

	/**
	 * A <code>List</code> of all <code>NeuronGene</code>s in the output layer.
	 */
	private List<NeuronGene> outputNeurons;

	/**
	 * A <code>Map</code> of <code>NeuronGene</code> id to
	 * <code>NeuronGene</code> instance to facilitate fast by-id lookups.
	 */
	private Map<Long, NeuronGene> neuronsMap;

	/**
	 * The <code>Specie</code> this <code>Organism</code> belongs to.
	 */
	private Specie specie;

	/**
	 * A simple array containing the innovation id's of any parents of this <code>Organism</code>.
	 */
	private long[] ancestory;

	/**
	 * Constructor.
	 * 
	 * @param factory
	 *            the <code>NeatParameters</code> that describe the evolution
	 *            environment this <code>Organism</code> is in.
	 * @param inputs
	 *            <code>List</code> of all <code>NeuronGene</code>s that make up
	 *            the input layer.
	 * @param outputs
	 *            <code>List</code> of all <code>NeuronGene</code>s that make up
	 *            the output layer.
	 */
	public Organism(NeatParameters factory, List<NeuronGene> inputs,
			List<NeuronGene> outputs) {
		this(factory, inputs, outputs, true);
	}

	public Organism(NeatParameters factory, List<NeuronGene> inputs,
			List<NeuronGene> outputs, boolean fullyConnected) {
		this(factory, factory.nextInnovationId(), inputs, outputs,
				fullyConnected);
	}

	/**
	 * Constructor.
	 * 
	 * @param factory
	 *            the <code>NeatParameters</code> that describe the evolution
	 *            environment this <code>Organism</code> is in.
	 * @param inputs
	 *            <code>List</code> of all <code>NeuronGene</code>s that make up
	 *            the input layer.
	 * @param outputs
	 *            <code>List</code> of all <code>NeuronGene</code>s that make up
	 *            the output layer.
	 * @param fullyConnected
	 *            <code>true</code> if all input <code>NeuronGene</code>s should
	 *            be connected to all output <code>NeuronGene</code>s.
	 */
	public Organism(NeatParameters factory, long innovationId,
			List<NeuronGene> inputs, List<NeuronGene> outputs,
			boolean fullyConnected) {
		init();

		if (inputs == null || inputs.isEmpty()) {
			throw new IllegalArgumentException(
					"Attempting to create an Organism with no input neurons.");
		}

		if (outputs == null || outputs.isEmpty()) {
			throw new IllegalArgumentException(
					"Attempting to create an Organism with no output neurons.");
		}

		this.organismId = innovationId;

		for (NeuronGene n : inputs) {
			if (n.getNeuronType() == NeuronType.INPUT) {
				addNeuronGene(n);
			} else {
				throw new IllegalArgumentException(
						"List of input NeuronGene's contains a Neuron who's type is not NeuronType.INPUT.");
			}
		}

		for (NeuronGene n : outputs) {
			if (n.getNeuronType() == NeuronType.OUTPUT) {
				addNeuronGene(n);
			} else {
				throw new IllegalArgumentException(
						"List of output NeuronGene's contains a Neuron who's type is not NeuronType.OUTPUT.");
			}
		}

		if (fullyConnected) {
			for (NeuronGene in : inputNeurons) {
				for (NeuronGene out : outputNeurons) {
					addConnectionGene(factory, in.getInnovationId(), out
							.getInnovationId(), factory
							.getRandomGenerator().nextDouble());
				}
			}
		}
	}

	public Organism(long innoId, List<Gene> genes) {
		init();

		this.organismId = innoId;

		for (Gene g : genes) {
			addGene(g);
		}
	}

	/**
	 * Initialises the state of the fields used by this class.
	 */
	private void init() {
		this.connections = new ArrayList<ConnectionGene>();
		this.connectionsMap = new HashMap<Long, ConnectionGene>();

		this.inputNeurons = new ArrayList<NeuronGene>();
		this.hiddenNeurons = new ArrayList<NeuronGene>();
		this.outputNeurons = new ArrayList<NeuronGene>();

		this.neuronsMap = new HashMap<Long, NeuronGene>();
	}

	/**
	 * Adds the provided <code>NeuronGene</code> to this organism. The provided
	 * <code>NeuronGene</code> will be added to the appropriate
	 * <code>List</code> based on it's <code>NeuronType</code> as well as
	 * inserted into the <code>neuronMap</code>.
	 * 
	 * @param n
	 *            the <code>NeruonGene</code> to add to this
	 *            <code>Organism</code>
	 * @throws IllegalArgumentException
	 *             if the provided <code>NeuronGene</code> is null or if it
	 *             returns a <code>NeuronType</code> that is unrecognised.
	 */
	public void addNeuronGene(NeuronGene n) {
		if (n == null) {
			throw new IllegalArgumentException(
					"Attempting to add a null NeuronGene.");
		}

		if (n.getNeuronType() == NeuronType.INPUT) {
			inputNeurons.add(n);
		} else if (n.getNeuronType() == NeuronType.HIDDEN) {
			hiddenNeurons.add(n);
		} else if (n.getNeuronType() == NeuronType.OUTPUT) {
			outputNeurons.add(n);
		} else {
			throw new IllegalArgumentException(
					"Attempting to add a NeuronGene with an unknown NeuronType: "
							+ n.getNeuronType().name() + ".");
		}

		neuronsMap.put(n.getInnovationId(), n);
	}

	public Gene addConnectionGene(NeatParameters factory, long originId, long endpointId, double weight) {
		if (neuronsMap.get(originId) == null) {
			throw new IllegalArgumentException(
					"Attempting to add a connection from neuron " + originId
							+ " however no such Neuron exists.");
		}

		if (neuronsMap.get(endpointId) == null) {
			throw new IllegalArgumentException(
					"Attempting to add a connection from neuron " + originId
							+ " however no such Neuron exists.");
		}

		ConnectionGene gene = new ConnectionGene(factory, originId,
				endpointId, weight, true);
		addConnectionGene(gene);
		return gene;
	}

	private void addConnectionGene(ConnectionGene gene) {
		if (gene == null) {
			throw new IllegalArgumentException(
					"Attempting to add a null ConnectionGene.");
		}

		// no point adding a connection we already have...
		if (!connections.contains(gene)) {
			connectionsMap.put(gene.getInnovationId(), gene);
			connections.add(gene);
		} else {
			// TODO : turn this back on!!
			// throw new
			// IllegalArgumentException("Attempting to add a ConnectionGene from "
			// + gene.getOriginId() + " to " + gene.getEndpointId() +
			// ", however that connection already exists.");
		}
	}

	public long getInnovationId() {
		return organismId;
	}

	private boolean containsConnection(long out, long in) {
		for (ConnectionGene link : connectionsMap.values()) {
			if (link.getEndpointId() == in) {
				if (link.getOriginId() == out) {
					return true;
				} else {
					return containsConnection(out, link.getOriginId());
				}
			}
		}
		return false;
	}

	void setSpecies(Specie specie) {
		// if(this.specie != null) {
		// this.specie.removeOrganism(this);
		// }

		this.specie = specie;
	}

	public Specie getSpecie() {
		return specie;
	}

	// public boolean hasFitnessSet() {
	// if(specie == null) {
	// return false;
	// }
	//		
	// return specie.hasFitnessSet(this);
	// }

	// public double getFitness() {
	// if (specie == null) {
	// throw new IllegalArgumentException("Organism " + organismId +
	// " has no Specie set.");
	// }
	//
	// return specie.getFitnessForOrganism(this);
	// }

	/**
	 * Creates a copy of this <code>Organism</code> with a new organism ID. It
	 * is important that all of the <code>Gene</code>s that make up this
	 * organism retain their original innovation id's.
	 * 
	 * @param params
	 * @return
	 */
	public Organism copy(NeatParameters params) {
		if (params == null) {
			throw new IllegalArgumentException(
					"Attepting to clone an Organism with a null NeatParameters.");
		}

		Organism o = new Organism(params, inputNeurons, outputNeurons, false);

		for (NeuronGene ng : hiddenNeurons) {
			NeuronGene clone = ng.copy();
			o.addGene(clone);
		}

		for (ConnectionGene cg : connections) {
			o.addConnectionGene(cg.copy());
		}

		return o;
	}

	public List<ConnectionGene> getGenes() {
		return new ArrayList<ConnectionGene>(connectionsMap.values());
	}

	public Gene findMatchingGene(Gene g) {
		if (g == null) {
			throw new IllegalArgumentException(
					"Attempting to find a match for a null Gene.");
		}

		for (Gene cg : connectionsMap.values()) {
			if (g.equals(cg)) {
				return cg;
			}
		}

		return null;
	}

	public NeuronGene getNeuron(long id) {
		return neuronsMap.get(id);
	}

	public List<NeuronGene> getNeurons() {
		List<NeuronGene> all = new ArrayList<NeuronGene>();
		all.addAll(inputNeurons);
		all.addAll(hiddenNeurons);
		all.addAll(outputNeurons);

		return all;
	}

	public List<NeuronGene> getNeurons(NeuronType type) {
		if (type == null) {
			throw new IllegalArgumentException(
					"Attempting to get NeuronGene's with a null NeuronType.");
		}

		if (type == NeuronType.INPUT) {
			return inputNeurons;
		} else if (type == NeuronType.HIDDEN) {
			return hiddenNeurons;
		} else if (type == NeuronType.OUTPUT) {
			return outputNeurons;
		} else {
			throw new IllegalArgumentException(
					"Provided NeuronType is not a valid type.");
		}
	}

	public List<ConnectionGene> getConnections() {
		return connections;
	}

	public boolean canConnect(long st, long end) {
		for (ConnectionGene cg : connections) {
			if (cg.getOriginId() == st && cg.getEndpointId() == end) {
				return false;
			}

			if (cg.getEndpointId() == st && cg.getOriginId() == end) {
				return false;
			}
		}

		return true;
	}

	public boolean containsConnection(NeuronGene start, NeuronGene end) {
		return containsConnection(end.getInnovationId(), start
				.getInnovationId());
	}

	public void removeGenes(Set<Gene> genesToRemove) {
		if (genesToRemove == null) {
			throw new IllegalArgumentException(
					"Attempting to remove a Set of Gene's that is null.");
		}

		for (Gene g : genesToRemove) {
			if (g == null) {
				throw new IllegalArgumentException(
						"Attempting to remove a null Gene.");
			}

			removeGene(g);
		}
	}

	public void removeGene(Gene g) {
		if (g == null) {
			throw new IllegalArgumentException(
					"Attempting to remove a null gene");
		}

		if (g instanceof ConnectionGene) {
			removeConnectionGene((ConnectionGene) g);
		} else if (g instanceof NeuronGene) {
			removeNeuronGene((NeuronGene) g);
		} else {
			throw new IllegalArgumentException(
					"Attempting to remove a Gene that is neither a ConnectionGene or a NeuronGene.");
		}
	}

	public void addGenes(Set<Gene> genesToAdd) {
		if (genesToAdd == null) {
			throw new IllegalArgumentException(
					"Attempting to add a Set of Gene's that is null.");
		}
		for (Gene g : genesToAdd) {
			if (g == null) {
				throw new IllegalArgumentException(
						"Attempting to add a null Gene.");
			}

			addGene(g);
		}
	}

	/**
	 * Inserts the provided <code>Gene</code> into this <code>Organism</code>.
	 * 
	 * <b>NOTE:</b> it is not recommended that you use this method, use the more specific
	 * <code>addConnectionGene</code> or <code>addNeuronGene</code> methods instead, this
	 * is added to make development of <code>Persistence</code> implementations easier.
	 * 
	 * @param g the <code>Gene</code> to add.
	 */
	public void addGene(Gene g) {
		if (g == null) {
			throw new IllegalArgumentException("Attempting to add null Gene.");
		}
		if (g instanceof ConnectionGene) {
			addConnectionGene((ConnectionGene) g);
		} else if (g instanceof NeuronGene) {
			addNeuronGene((NeuronGene) g);
		} else {
			throw new IllegalArgumentException(
					"Attempting to add a Gene that is neither a ConnectionGene or a NeuronGene.");
		}

	}

	public void removeNeuronGene(NeuronGene n) {
		if (n == null) {
			throw new IllegalArgumentException(
					"Attempting to remove null NeuronGene.");
		}

		if (n.getNeuronType() == NeuronType.INPUT) {
			inputNeurons.remove(n);
		} else if (n.getNeuronType() == NeuronType.HIDDEN) {
			hiddenNeurons.remove(n);
		} else if (n.getNeuronType() == NeuronType.OUTPUT) {
			outputNeurons.remove(n);
		} else {
			throw new IllegalArgumentException(
					"Attempting to remove a NeuronGene with an unknown NeuronType.");
		}

		neuronsMap.remove(n.getInnovationId());
	}

	public void removeConnectionGene(ConnectionGene g) {
		if (g == null) {
			throw new IllegalArgumentException(
					"Attempting to null remove ConnectionGene.");
		}

		connections.remove(g);
		connectionsMap.remove(g.getInnovationId());
	}

	/**
	 * Creates an Identical copy of this <code>Organism</code> which will
	 * preserve the organism ID.
	 * 
	 * This should only be used for creating identical duplicates. For any
	 * copying to be used inside NEAT, it is recommended that you use the
	 * copy(NeatParameters) method instead. Using this method from within NEAT
	 * will cause the SimpleSpeciator to fail.
	 * 
	 * <b>You have been warned.</b>
	 * 
	 * @return creates an Identical copy of this <code>Organism</code> which
	 *         will preserve the organism ID.
	 */
	public Organism copy() {
		List<Gene> geneSet = new ArrayList<Gene>();
		geneSet.addAll(GeneHelper.copyConnections(connections));
		geneSet.addAll(GeneHelper.copyNeurons(new ArrayList<NeuronGene>(
				neuronsMap.values())));

		Organism o = new Organism(organismId, geneSet);
		return o;
	}

	public int geneticHash() {
		final int prime = 31;
		int result = 1;

		for (ConnectionGene cg : connections) {
			result = prime * result + cg.geneticHash();
		}

		for (NeuronGene ng : inputNeurons) {
			result = prime * result + ng.geneticHash();
		}

		for (NeuronGene ng : hiddenNeurons) {
			result = prime * result + ng.geneticHash();
		}

		for (NeuronGene ng : outputNeurons) {
			result = prime * result + ng.geneticHash();
		}

		return result;
	}

	public void setAncestory(long[] ls) {
		this.ancestory = ls;
	}

	/**
	 * The array returned by this method contains the <code>Organism</code> id's
	 * that were used in the creation of this <code>Organism</code>. It is
	 * typically used for tracing the genetic history of this
	 * <code>Organism</code>.
	 * 
	 * @return an array of organism id's which are the ancestors of this
	 *         <code>Organism</code>.
	 */
	public long[] getAncestory() {
		return ancestory;
	}

	/**
	 * Callback to inform the <code>Organism</code> about which stage the
	 * evolution process is in.
	 * 
	 * @param type
	 *            the <code>EvolutionEventType</code> describing the stage.
	 */
	public void update(EvolutionEventType type) {

	}
}
