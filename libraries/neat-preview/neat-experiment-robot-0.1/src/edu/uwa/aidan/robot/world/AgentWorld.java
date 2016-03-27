package edu.uwa.aidan.robot.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D.Double;
import java.util.ArrayList;
import java.util.List;


/**
 * Agent world is the simulated world in which the robot will move around in.
 * 
 * The <code>AgentWorld</code> is responsible for delegating all of the simulation 
 * time steps to all members of the world, as well as storing references to all of the
 * <code>Obstacle</code>s that a <code>Robot</code> will encounter whilst attempting to 
 * traverse the world.
 * 
 * <b>NOTE:</b> this code makes extensive use of the java 2D <code>Shape</code> classes
 * because it represents the simplest and fastest way of implementing 2D geometry. You do
 * <b>NOT</b> have to have a UI running in order to use this code, however this code also
 * provides a <code>paint</code> method if you wish to render the world to a <code>JComponent</code>.
 * 
 * @author Aidan Morgan
 */
public class AgentWorld {
	private double worstFitness = java.lang.Double.MAX_VALUE;

	/**
	 * The border of the world, the world is always assumed to be an enclosed
	 * space.
	 */
	private Rectangle2D border;

	/**
	 * The border as a <code>List</code> of <code>Line2D</code> objects, used
	 * for collision detection.
	 */
	private List<Line2D> boundary;

	/**
	 * The <code>Obstacle</code>s in the world. These are represented as
	 * <code>Line2D</code> objects.
	 */
	private List<Obstacle> obstacles;

	/**
	 * The <code>Goal</code> the robot is attempting to find.
	 */
	private Goal goal;

	/**
	 * The robot, controlled via NEAT.
	 */
	private Robot agent;

	/**
	 * The location at which the <code>Robot</code> will start from.
	 */
	private Point2D.Double agentStartPosition;

	/**
	 * Constructor.
	 * 
	 * @param width
	 *            the width of the world, in pixels.
	 * @param height
	 *            the height of the world, in pixels.
	 */
	public AgentWorld(double width, double height) {
		this.border = new Rectangle2D.Double(0.0, 0.0, width, height);
		obstacles = new ArrayList<Obstacle>();

		boundary = new ArrayList<Line2D>();
		boundary.add(new Line2D.Double(0.0, 0.0, width, 0.0));
		boundary.add(new Line2D.Double(0.0, 0.0, 0.0, height));
		boundary.add(new Line2D.Double(0.0, height, width, height));
		boundary.add(new Line2D.Double(width, 0.0, width, height));
	}

	/**
	 * Adds the provided <code>Obstacle</code> to the <code>List</code> of
	 * <code>Obstacle</code>s in the world.
	 * 
	 * @param o
	 *            the <code>Obstacle</code> to add.
	 */
	public void addObstacle(Obstacle o) {
		obstacles.add(o);
	}

	/**
	 * Returns the <code>List</code> of <code>Obstacle</code>s in the world.
	 * 
	 * @return the <code>List</code> of <code>Obstacle</code>s in the world.
	 */
	public List<Obstacle> getObstacles() {
		return obstacles;
	}

	/**
	 * Returns the <code>Goal</code> the agent is attempting to find.
	 * 
	 * @return the <code>Goal</code> the agent is attempting to find.
	 */
	public Goal getGoal() {
		return goal;
	}

	/**
	 * Sets the <code>Goal</code> the agent is attempting to find. There can be
	 * only one <code>Goal</code> in the world.
	 * 
	 * @param g
	 *            the <code>Goal</code> the agent is attempting to find.
	 */
	public void setGoal(Goal g) {
		this.goal = g;
		g.setWorld(this);
	}

	/**
	 * Returns the <code>Robot</code> that is going to move around the
	 * world.
	 * 
	 * @return the <code>Robot</code> that is going to move around the
	 *         world.
	 */
	public Robot getAgent() {
		return agent;
	}

	/**
	 * Sets the <code>Robot</code> that will be used to explore this world.
	 * @param agent the <code>Robot</code> that will be used to explore this world.
	 */
	public void setRobot(Robot agent) {
		this.agent = agent;
		agent.setWorld(this);

		agent.rotate(-(Math.PI / 2.0));
	}

	/**
	 * Returns the boundary of this <code>AgentWorld</code> as a <code>List</code> of <code>Line2D</code> objects,
	 * instead of a <code>Rectangle2D</code>.
	 * @return the boundary of this <code>AgentWorld</code> as a <code>List</code> of <code>Line2D</code> objects.
	 */
	public List<Line2D> getBoundary() {
		return boundary;
	}

	/**
	 * Used to render this <code>AgentWorld</code> to the <code>Graphics2D</code> instance, using the
	 * provided aspect ratio.
	 * 
	 * @param g2d the <code>Graphics2D</code> instance for rendering with,
	 * @param pixelRatio the aspect ratio to use for rendering.
	 */
	public void paint(Graphics2D g2d, double pixelRatio) {
		g2d.setColor(Color.BLACK);
		
		for (Line2D l : boundary) {
			g2d.draw(ShapeHelper.getLine(l, pixelRatio));
		}

		for (Obstacle o : obstacles) {
			g2d.draw(ShapeHelper.getLine(o.getShape(), pixelRatio));
		}

		if (goal != null) {
			goal.paint(g2d, pixelRatio);
		}

		if (agent != null) {
			agent.paint(g2d, pixelRatio);
		}
	}

	/**
	 * Returns the boundary of this <code>AgentWorld</code> as a <code>Rectangle2D</code>.
	 * @return the boundary of this <code>AgentWorld</code> as a <code>Rectangle2D</code>.
	 */
	public Rectangle2D getBoundaryRect() {
		return border;
	}

	/**
	 * Returns the width of this <code>AgentWorld</code>.
	 * @return the width of this <code>AgentWorld</code>.
	 */
	public double getWidth() {
		return border.getWidth();
	}

	/**
	 * Returns the height of this <code>AgentWorld</code>.
	 * @return the height of this <code>AgentWorld</code>.
	 */
	public double getHeight() {
		return border.getHeight();
	}
	
	/**
	 * Returns the score a <code>Robot</code> will get if it performs in absolutely the worst
	 * possible way.
	 * 
	 * This is used because NEAT requires a maximization problem, however goal finding is a problem
	 * of minimizing the distance of the <code>Robot</code> from the goal. If we simply subtract the
	 * <code>Robot</code>s distance from the goal from the value returned by this method, then we get
	 * the maximisation problem we require.
	 *  
	 * @return the score a <code>Robot</code> will get if it performs in absolutely the worst
	 * possible way.
	 */
	public double getWorstFitnessScore() {
		return worstFitness;
	}

	public void setWorstFitnessScore(double bestFitness) {
		this.worstFitness = bestFitness;
	}

	/**
	 * Updates everything in the world to the provided <code>stepNum</code>.
	 * 
	 * @param stepNum
	 *            the current step num.
	 */
	public void step(long stepNum) {
		for (Obstacle o : obstacles) {
			o.step(stepNum);
		}

		if (goal != null) {
			goal.step(stepNum);
		}

		if (agent != null) {
			agent.step(stepNum);
		}
	}

	/**
	 * Resets the world back to default.
	 */
	public void reset() {
		for (Obstacle o : obstacles) {
			o.reset();
		}

		if (goal != null) {
			goal.reset();
		}

		if (agent != null) {
			agent.reset();
		}
	}
	/**
	 * Creates a copy of this <code>AgentWorld</code>.
	 * 
	 * <b>Note:</b> this will not copy the <code>Robot</code>, it is used for copying
	 * the definition of the world to save loading it from disk repeatedly.
	 * 
	 * @return a copy of this <code>AgentWorld</code>.
	 */
	public AgentWorld copy() {
		AgentWorld copy = new AgentWorld(getWidth(), getHeight());
		for(Obstacle o : getObstacles()) {
			copy.addObstacle(o.copy());
		}
		
		if(getGoal() != null) {
			copy.setGoal(getGoal().copy());
		}
		
		copy.setWorstFitnessScore(worstFitness);
		copy.setInitialLocation(agentStartPosition.getX(), agentStartPosition.getY());
		
		return copy;
	}

	/**
	 * Returns <code>true</code> if it is possible to move from the
	 * <code>Point2D</code> which is the current position, to the
	 * <code>Point2D</code>.
	 * 
	 * i.e. is there a "clear" path between the two points.
	 * 
	 * @param lastPoint
	 *            the current location.
	 * @param newPoint
	 *            the location to move to.
	 * 
	 * @return <codE>true</code> if it is possible to move from the
	 *         <code>Point2D</code> which is the current position, to the
	 *         <code>Point2D</code>.
	 */
	public boolean canMove(Point2D lastPoint, Point2D newPoint) {
		if (lastPoint != null) {
			Double line = new Double(lastPoint, newPoint);

			for (Obstacle o : getObstacles()) {
				Line2D obstacle = o.getShape();

				if (obstacle.intersectsLine(line)) {
					return false;
				}

				// make sure the agent doesnt bump right into the obstacle.
				if (obstacle.ptSegDist(newPoint) < Robot.AGENT_RADIUS) {
					return false;
				}
			}

			for (Line2D o : getBoundary()) {
				if (o.intersectsLine(line)) {
					return false;
				}

				// make sure the agent doesnt bump right into the obstacle.
				if (o.ptSegDist(newPoint) < Robot.AGENT_RADIUS) {
					return false;
				}
			}
		} else {
			if (!getBoundaryRect().contains(newPoint)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Sets the initial location of the <code>Robot</code> when added to this world.
	 * 
	 * @param x the x coordinate of the <code>Robot</code>s initial location.
	 * @param y the y coordinate of the <code>Robot</code>s initial location.
	 */
	public void setInitialLocation(double x, double y) {
		agentStartPosition = new Point2D.Double(x, y);
	}
	
	/**
	 * Returns the initial location of the <code>Robot</code> when added to this world as a
	 * <codE>Point2D</code> instance.
	 * 
	 * @return the initial location of the <code>Robot</code> when added to this world as a
	 * <codE>Point2D</code> instance.
	 */
	public Point2D getInitialLocation() {
		return agentStartPosition;
	}
}
