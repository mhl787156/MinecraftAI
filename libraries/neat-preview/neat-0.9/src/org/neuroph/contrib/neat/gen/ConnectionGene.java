package org.neuroph.contrib.neat.gen;

/**
 * A <code>ConnectionGene</code> represents a connection between two
 * <code>NeuronGene</code>s.
 * 
 * @author Aidan Morgan
 */
public class ConnectionGene implements Gene {
	/**
	 * Serialization identifier.
	 */
	private static final long serialVersionUID = -4465944554121753537L;

	/**
	 * The innovation id of the start <code>NeuronGene</code>.
	 */
	private long originId;

	/**
	 * The innovation id of the end <code>NeuronGene</code>.
	 */
	private long endpointId;

	/**
	 * The weight useed for this connection.
	 */
	private double weight;

	/**
	 * Whether this <code>ConnectionGene</code> is enabled or not.
	 */
	private boolean enabled;

	/**
	 * The innovation id of this <code>ConnectionGene</code>.
	 */
	private long innovationId;

	/**
	 * Constructor.
	 * 
	 * @param fact
	 *            <code>NeatParameters</code> that describes the environment in
	 *            which this <code>ConnectionGene</code> exists.
	 * @param originId
	 *            the innovation id of the start <code>NeuronGene</code>.
	 * @param endpointId
	 *            the innovation id of the end <code>NeuronGene</code>.
	 * @param weight
	 *            the weight useed for this connection.
	 * @param enabled
	 *            whether this <code>ConnectionGene</code> is enabled or not.
	 */
	public ConnectionGene(NeatParameters fact, long originId, long endpointId,
			double weight, boolean enabled) {
		this(fact.nextInnovationId(), originId, endpointId, weight, enabled);
	}

	/**
	 * Constructor
	 * 
	 * @param innoid
	 *            the innovation id for this <code>ConnectionGene</code>.
	 * @param originId
	 *            the innovation id of the start <code>NeuronGene</code>.
	 * @param endpointId
	 *            the innovation id of the end <code>NeuronGene</code>.
	 * @param weight
	 *            the weight useed for this connection.
	 * @param enabled
	 *            whether this <code>ConnectionGene</code> is enabled or not.
	 */
	public ConnectionGene(long innoid, long originId, long endpointId,
			double weight, boolean enabled) {
		super();
		this.originId = originId;
		this.endpointId = endpointId;
		this.weight = weight;
		this.enabled = enabled;
		this.innovationId = innoid;
	}

	/**
	 * Returns the innovation id of the start <code>NeuronGene</code> for this
	 * <code>ConnectionGene</code>.
	 * 
	 * @return the innovation id of the start <code>NeuronGene</code> for this
	 *         <code>ConnectionGene</code>.
	 */
	public long getOriginId() {
		return originId;
	}

	/**
	 * Sets the innovation id of the start <code>NeuronGene</code> for this
	 * <code>ConnectionGene</code>.
	 * 
	 * @param originId
	 *            the innovation id of the start <code>NeuronGene</code> for
	 *            this <code>ConnectionGene</code>.
	 */
	public void setOriginId(long originId) {
		this.originId = originId;
	}

	/**
	 * Returns the innovation id of the end <code>NeuronGene</code> for this
	 * <code>ConnectionGene</code>.
	 * 
	 * @return the innovation id of the end <code>NeuronGene</code> for this
	 *         <code>ConnectionGene</code>.
	 */
	public long getEndpointId() {
		return endpointId;
	}

	/**
	 * Sets the innovation id of the end <code>NeuronGene</code> for this
	 * <code>ConnectionGene</code>.
	 * 
	 * @param endpointId
	 *            the innovation id of the end <code>NeuronGene</code> for this
	 *            <code>ConnectionGene</code>.
	 */
	public void setEndpointId(long endpointId) {
		this.endpointId = endpointId;
	}

	/**
	 * Returns the weight for this connection.
	 * 
	 * @return the weight for this connection.
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Sets the weight for this connection.
	 * 
	 * @param weight
	 *            the weight for this connection.
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neuroph.contrib.neat.gen.Gene#isEnabled()
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets whether this <code>ConnectionGene</code> is enabled or not.
	 * 
	 * @param enabled
	 *            <code>true</code> if enabled, <code>false</code> otherwise.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neuroph.contrib.neat.gen.Gene#getInnovationId()
	 */
	public long getInnovationId() {
		return innovationId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.neuroph.contrib.neat.gen.Gene#copy(org.neuroph.contrib.neat.gen.
	 * NeatParameters)
	 */
	public Gene copy(NeatParameters fact) {
		ConnectionGene cg = new ConnectionGene(fact, originId, endpointId,
				weight, enabled);
		return cg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.neuroph.contrib.neat.gen.Gene#copy()
	 */
	public ConnectionGene copy() {
		ConnectionGene cg = new ConnectionGene(innovationId, originId,
				endpointId, weight, enabled);
		return cg;
	}

	public int geneticHash() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (endpointId ^ (endpointId >>> 32));
		result = prime * result + (int) (originId ^ (originId >>> 32));
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + (int) (endpointId ^ (endpointId >>> 32));
		result = prime * result + (int) (innovationId ^ (innovationId >>> 32));
		result = prime * result + (int) (originId ^ (originId >>> 32));
		long temp;
		temp = Double.doubleToLongBits(weight);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		ConnectionGene other = (ConnectionGene) obj;

		if (endpointId == other.endpointId) {
			if (originId == other.originId) {
				return true;
			}
		}
		// need to cover the reverse case...
		else if (endpointId == other.originId) {
			if (originId == other.endpointId) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return "ConnectionGene[from:" + originId + " to:" + endpointId
				+ " weight:" + weight + "]";
	}

}
