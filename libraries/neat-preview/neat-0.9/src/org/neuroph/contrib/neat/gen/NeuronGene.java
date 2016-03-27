package org.neuroph.contrib.neat.gen;

/**
 * A <code>NeuronGene</code> is a type of <code>Gene</code> that represents a
 * <code>Neuron</code> in the <code>NeuralNetwork</code>.
 * 
 * @author Aidan Morgan
 */
public class NeuronGene implements Gene {
	private static final long serialVersionUID = -7208178499417826008L;

	/**
	 * The innovation id for this <code>NeuronGene</code>.
	 */
	private long innovationId;

	/**
	 * The activation response for the neuron.
	 */
	private double activationResponse;

	/**
	 * The type of the neuron (INPUT, HIDDEN, OUTPUT).
	 */
	private NeuronType type;

	/**
	 * <code>true</code> if this <code>NeuronGene</code> is enabled,
	 * <code>false</code> otherwise.
	 */
	private boolean enabled = true;

	/**
	 * Constructor.
	 * 
	 * @param type
	 *            the type of the neuron.
	 * @param neatParams
	 *            the <code>NeatParameters</code> that describe the environment
	 *            in which this <codE>NeuronGene</code> exists.
	 * @param activationResponse
	 *            the activation response for the neuron.
	 */
	public NeuronGene(NeuronType type, NeatParameters neatParams,
			double activationResponse) {
		this(type, neatParams.nextInnovationId(), activationResponse);
	}

	/**
	 * Constructor.
	 * 
	 * @param type
	 *            the type of the neuron.
	 * @param innoId
	 *            the innovation of this neuron gene.
	 * @param activationResponse
	 *            the activation response for the neuron.
	 */
	public NeuronGene(NeuronType type, long innoId, double activationResponse) {
		this.type = type;
		this.innovationId = innoId;
		this.activationResponse = activationResponse;
	}

	/**
	 * Constructor.
	 * 
	 * @param type
	 *            the type of the neuron.
	 * @param params
	 *            the <code>NeatParameters</code> that describe the environment
	 *            in which this <codE>NeuronGene</code> exists.
	 */
	public NeuronGene(NeuronType type, NeatParameters params) {
		this(type, params, params.getRandomGenerator().nextDouble());
	}

	/**
	 * Creates a copy of this <code>NeuronGene</code> with a new innovation id.
	 */
	public NeuronGene copy(NeatParameters fact) {
		NeuronGene ng = new NeuronGene(type, fact, activationResponse);
		return ng;
	}

	/**
	 * Creates a copy of this <code>NeuronGene</code> with the same innovation
	 * id.
	 */
	public NeuronGene copy() {
		NeuronGene ng = new NeuronGene(type, innovationId, activationResponse);
		return ng;
	}

	public long getInnovationId() {
		return innovationId;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean b) {
		this.enabled = b;
	}

	public NeuronType getNeuronType() {
		return type;
	}

	public double getActivationResponse() {
		return activationResponse;
	}

	public void setActivationResponse(double d) {
		this.activationResponse = d;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (innovationId ^ (innovationId >>> 32));
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
		NeuronGene other = (NeuronGene) obj;
		if (innovationId != other.innovationId)
			return false;
		return true;
	}

	public int geneticHash() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(activationResponse);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + (int) (innovationId ^ (innovationId >>> 32));
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

}
