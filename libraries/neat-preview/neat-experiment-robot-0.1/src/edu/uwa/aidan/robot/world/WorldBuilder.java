package edu.uwa.aidan.robot.world;

import java.io.File;

/**
 * A <code>WorldBuilder</code> is responsible for creating a <code>AgentWorld</code>
 * based on a file-format.
 * 
 * @author Aidan Morgan
 */
public abstract class WorldBuilder {
	private File worldFile;
	
	/**
	 * Constructor.
	 * 
	 * @param f the <code>File</code> to load the <code>AgentWorld</code> definition from.
	 */
	public WorldBuilder(File f) {
		this.worldFile = f;
	}

	/**
	 * Returns the <code>File</code> to load the <code>AgentWorld</code> definition from.
	 * @return the <code>File</code> to load the <code>AgentWorld</code> definition from.
	 */
	protected File getFile() {
		return worldFile;
	}
	
	/**
	 * Create a new <code>AgentWorld</code> based on the <code>File</code> wrapped by this
	 * <code>WorldBuilder</code>.
	 *  
	 * @return a new <code>AgentWorld</code> based on the <code>File</code> wrapped by this
	 * <code>WorldBuilder</code>.
	 */
	public abstract AgentWorld createWorld();

	public abstract double getWorstFitnessScore();
}
