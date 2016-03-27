package org.neuroph.contrib.neat.gen;

/**
 * Any class that implements this <code>Innovation</code> interface 
 * should have it's creation recorded in the <code>Innovations</code>.
 * 
 * An innovation id is a mechanism for uniquely identifying items in the
 * NEAT evolution, and is also used for historical tracking purposes. 
 * 
 * @author Aidan Morgan
 */
public interface Innovation {
	/**
	 * Returns the innovation id for this object.
	 * @return the innovation id for this object.
	 */
	public long getInnovationId();
}
