package org.neuroph.contrib.neat.gen;

/**
 * A simple <code>Exception</code> that is raised when there is a problem with
 * NEAT.
 * 
 * @author Aidan Morgan
 */
public class NeatException extends Exception {

	/**
	 * @inheritDoc
	 */
	public NeatException() {
		super();
	}

	/**
	 * @inheritDoc
	 */
	public NeatException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @inheritDoc
	 */
	public NeatException(String message) {
		super(message);
	}

	/**
	 * @inheritDoc
	 */
	public NeatException(Throwable cause) {
		super(cause);
	}

}
