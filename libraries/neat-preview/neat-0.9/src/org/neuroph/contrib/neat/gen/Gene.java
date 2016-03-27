package org.neuroph.contrib.neat.gen;

import java.io.Serializable;

/**
 * A <code>Gene</code> represents some "genetic material" inside an
 * <code>Organism</code>. This "genetic material" is what is reproduced and
 * mutated as part of the evolutionary process.
 * 
 * @author Aidan Morgan
 */
public interface Gene extends Innovation, Serializable {
	/**
	 * Whether this <code>Gene</code> is enabled or disabled.
	 * 
	 * @return <code>true</code> if this <code>Gene</code> is enabled,
	 *         <code>false</code> if it is disabled.
	 */
	public boolean isEnabled();

	/**
	 * Creates a new <code>Gene</code> the same as this one, but with a
	 * different innovation id.
	 * 
	 * @param fact
	 *            the <code>NeatParameters</code> that desctribe the evolution
	 *            environment.
	 * @return a new <code>Gene</code> the same as this one, but with a
	 *         different innovation id.
	 */
	public Gene copy(NeatParameters fact);

	/**
	 * Creates a new <code>Gene</code> the same as this one.
	 * 
	 * @return a new <code>Gene</code> the same as this one.
	 */
	public Gene copy();

	public void setEnabled(boolean b);
}