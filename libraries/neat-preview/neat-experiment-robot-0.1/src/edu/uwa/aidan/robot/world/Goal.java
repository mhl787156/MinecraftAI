package edu.uwa.aidan.robot.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * The <code>Goal</code> is what the <code>Robot</code> is searching for through the world.
 * 
 * It is a circle, represented by a <code>Ellipse2D</code> with a radius of <code>GOAL_RADIUS</code>
 * units.
 * 
 * This implementation assumes that the <code>Goal</code> cannot move, further development could easily
 * make this <code>Goal</code> move around the <code>AgentWorld</code> if desired.
 * 
 * @author Aidan Morgan
 */
public class Goal {
	/**
	 * The capture-radius of the <code>Goal</code>.
	 */
	public static final double GOAL_RADIUS = 5.0;

	/**
	 * The <code>Point2D</code> which is the origin (i.e. the middle) of the <code>Goal</code>.
	 */
	private Point2D position;

	/**
	 * The <code>AgentWorld</code> which this <code>Goal</code> belongs to.
	 */
	private AgentWorld world;

	/**
	 * Constructor.
	 * 
	 * @param x the x coordinate of the center of the goal.
	 * @param y the y coordinate of the center of the goal.
	 */
	public Goal(double x, double y) {
		this(new Point2D.Double(x, y));
	}

	/**
	 * Constructor.
	 * 
	 * @param position the <code>Point2D</code> which is the center of the goal.
	 */
	public Goal(Point2D position) {
		this.position = position;
	}

	/**
	 * Returns the java <code>Shape</code> representation of this <code>Goal</code> that is used 
	 * for all geometric operations.
	 * @return the java <code>Shape</code> representation of this <code>Goal</code> that is used 
	 * for all geometric operations.
	 */
	public Ellipse2D getShape() {
		Ellipse2D.Double ellipse = new Ellipse2D.Double(position.getX(),
				position.getY(), GOAL_RADIUS, GOAL_RADIUS);
		return ellipse;
	}

	/**
	 * Returns <code>true</code> if the provided <code>Point2D</code> is inside the capture-radius
	 * of this <code>Goal</code>, <code>false</code> otherwise.
	 * @param point the <code>Point2D</code> to test.
	 * @return <code>true</code> if the provided <code>Point2D</code> is inside the capture-radius
	 * of this <code>Goal</code>, <code>false</code> otherwise.
	 */
	public boolean containsPoint(Point2D point) {
		return getShape().contains(point);
	}

	/**
	 * Returns the center point location of this <code>Goal</code> as a <code>Point2D</code>
	 * @return the center point location of this <code>Goal</code> as a <code>Point2D</code>
	 */
	public Point2D getPosition() {
		return position;
	}

	/**
	 * Called when this <code>Goal</code> is added to a <code>AgentWorld</code> to provide
	 * a reference to the owning <code>AgentWorld</code>.
	 * 
	 * @param agentWorld the <code>AgentWorld</code> that this <code>Goal</code> belongs to.
	 */
	void setWorld(AgentWorld agentWorld) {
		this.world = agentWorld;
	}

	/**
	 * Used to render this <code>AgentWorld</code> to the <code>Graphics2D</code> instance, using the
	 * provided aspect ratio.
	 * 
	 * @param g2d the <code>Graphics2D</code> instance for rendering with,
	 * @param pixelRatio the aspect ratio to use for rendering.
	 */	
	public void paint(Graphics2D g, double pixelRatio) {
		g.setColor(Color.YELLOW);
		g.fill(ShapeHelper.getEllipse(getShape(), pixelRatio));
	}

	/**
	 * Does nothing, added for interface brevity, and also as a potential future
	 * extension point (i.e. for moving goals).
	 */
	public void step(long stepNum) {

	}

	/**
	 * Does nothing, added for interface brevity, and also as a potential future
	 * extension point (i.e. for moving goals).
	 */
	public void reset() {

	}

	/**
	 * Creates a copy of this <code>Goal</code>.
	 * @return a copy of this <code>Goal</code>.
	 */
	public Goal copy() {
		Goal g = new Goal(position);
		return g;
	}
}
