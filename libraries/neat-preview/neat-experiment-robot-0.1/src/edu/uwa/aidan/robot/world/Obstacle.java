package edu.uwa.aidan.robot.world;

import java.awt.geom.Line2D;

/**
 * An <code>Obstacle</code> is a line in the <code>AgentWorld</code> which a <code>Robot</code>
 * may not cross.
 * 
 * <code>Obstacle</code>s are used to define the boundaries of the <code>AgentWorld</code> as well
 * as any internal "walls" which cannot be crossed.
 * 
 * The <code>GoalSensor</code> is not stopped from viewing through an <code>Obstacle</code>, whereas
 * the <code>RangeSensor</code> cannot "see through" an <code>Obstacle</code>. 
 * 
 * @author Aidan Morgan
 */
public class Obstacle {
	/**
	 * The x and y coordinates of the line that represents this <code>Obstacle</code>. 
	 */
	private double x1, y1, x2, y2;

	/**
	 * Constructor.
	 * 
	 * @param x1 the x-coordinate of the starting point of this <code>Obstacle</code>.
	 * @param y1 the y-coordinate of the starting point of this <code>Obstacle</code>.
	 * @param x2 the x-coordinate of the ending point of this <code>Obstacle</code>.
	 * @param y2 the y-coordinate of the ending point of this <code>Obstacle</code>.
	 */
	public Obstacle(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	/**
	 * Returns the java <code>Shape</code> representation of this <code>Obstacle</code> that is used 
	 * for all geometric operations.
	 * @return the java <code>Shape</code> representation of this <code>Obstacle</code> that is used 
	 * for all geometric operations.
	 */	
	public Line2D getShape() {
		Line2D line = new Line2D.Double(x1, y1, x2, y2);
		return line;
	}


	/**
	 * Does nothing, added for interface brevity, and also as a potential future
	 * extension point (i.e. for moving obstacles).
	 */
	public void step(long stepNum) {

	}

	/**
	 * Does nothing, added for interface brevity, and also as a potential future
	 * extension point (i.e. for moving obstacles).
	 */
	public void reset() {

	}

	/**
	 * Creates an identical copy of this <code>Obstacle</code>.
	 * @return an identical copy of this <code>Obstacle</code>.
	 */
	public Obstacle copy() {
		Obstacle copy = new Obstacle(x1, y1, x2, y2);
		return copy;
	}
}
