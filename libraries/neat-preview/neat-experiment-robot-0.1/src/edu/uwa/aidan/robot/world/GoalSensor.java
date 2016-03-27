package edu.uwa.aidan.robot.world;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

/**
 * A <code>GoalSensor</code> is a simple sensor that is either on or off, depending 
 * on if the <code>Goal</code> lies between it's angles (which are related to the
 * rotation of the <code>Robot</code>).
 * 
 * There are typically four <code>GoalSensor</code>s attached to a <code>Robot</code>,
 * and only one of them may be active at any time. A <code>GoalSensor</code> effectively
 * creates a large arc, which is then tested to determine if the <code>Goal</code> lies
 * somewhere inside that arc. 
 * 
 * The <code>GoalSensor</code> is static relative to the <code>Robot</code>, however when
 * evaluating whether the <code>Goal</code> lies inside it's range, the current rotation of
 * the <code>Robot</code> must also be taken into account.
 * 
 * The <code>GoalSensor</code> is not impeded by the presence of any <code>Obstacle</code>s.
 * 
 * @author Aidan Morgan
 */
public class GoalSensor {
	/**
	 * The range of this sensor.
	 */
	public static final float SENSOR_RANGE = 400;

	/**
	 * The <code>Robot</code> that this <code>GoalSensor</code> belongs to.
	 */
	private Robot agent;
	
	/**
	 * The angle (independent of <code>Robot</code> rotation) that this sensor
	 * starts from.
	 */
	private double endAngle;

	/**
	 * The angle (independent of <code>Robot</code> rotation) that this sensor
	 * ends at.
	 */
	private double startAngle;

	/**
	 * The index at which this <code>GoalSensor</code> is in the <code>Robot</code>s sensor
	 * suite.
	 */
	private int index;

	/**
	 * Constructor.
	 * 
	 * @param agent the <code>Robot</code> that this <code>GoalSensor</code> belongs to.
	 * @param index the index at which this <code>GoalSensor</code> is in the <code>Robot</code>s sensor
	 * suite.
	 * @param startAngle the starting angle (static in comparison to the robot) of this <code>GoalSensor</code>'s
	 * sweep range.
	 * @param endAngle the ending angle (static in comparison to the robot) of this <code>GoalSensor</code>'s
	 * sweep range.
	 */
	public GoalSensor(Robot agent, int index, double startAngle,
			double endAngle) {
		this.index = index;
		this.agent = agent;
		this.startAngle = startAngle;
		this.endAngle = endAngle;
	}

	/**
	 * Determines a x coordinate based on the <code>Robot</code>s current position, as well
	 * as it's current rotation, and using the <code>SENSOR_RANGE</code>.
	 * 
	 * This is used to determine the x coordinates of the boundaries of the sensor's sweep.
	 * 
	 *  
	 * @param angle the angle from the <code>Robot</code>'s forward direction that this x coordinate
	 * is to be calculated from.
	 * 
	 * @return the x coordinate, <code>SENSOR_RANGE</code> units away, at the <code>angle</code> from the
	 * <code>Robot</code>'s current rotation. 
	 */
	public double getX(double angle) {
		double newAngle = angle + agent.getRotation();

		Double x = (SENSOR_RANGE * (double) Math.cos(newAngle))
				+ agent.getPosition().getX();
		return x.doubleValue();
	}

	/**
	 * Determines a y coordinate based on the <code>Robot</code>s current position, as well
	 * as it's current rotation, and using the <code>SENSOR_RANGE</code>.
	 * 
	 * This is used to determine the y coordinates of the boundaries of the sensor's sweep.
	 *  
	 * @param angle the angle from the <code>Robot</code>'s forward direction that this y coordinate
	 * is to be calculated from.
	 * 
	 * @return the y coordinate, <code>SENSOR_RANGE</code> units away, at the <code>angle</code> from the
	 * <code>Robot</code>'s current rotation. 
	 */	
	public double getY(double angle) {
		double newAngle = angle + agent.getRotation();

		Double x = (SENSOR_RANGE * (double) Math.sin(newAngle))
				+ agent.getPosition().getY();
		return x.doubleValue();
	}

	 /**
	  * Returns <code>true</code> if the <code>Goal</code> is inside this <code>GoalSensor</code>,
	  * <code>false</code> otherwise. 
	  * @return <code>true</code> if the <code>Goal</code> is inside this <code>GoalSensor</code>,
	  * <code>false</code> otherwise.
	  */
	public boolean containsGoal() {
		Point2D goal = agent.getWorld().getGoal().getPosition();

		Arc2D arc = getShape();
		
		return arc.contains(goal.getX(), goal.getY());
	}

	/**
	 * Returns the java <code>Shape</code> representation of this <code>GoalSensor</code> that is used 
	 * for all geometric operations.
	 * @return the java <code>Shape</code> representation of this <code>GoalSensor</code> that is used 
	 * for all geometric operations.
	 */	
	private Arc2D getShape() {
		Point2D pos = agent.getPosition();

		double x = pos.getX() - (SENSOR_RANGE / 2.0);
		double y = pos.getY() - (SENSOR_RANGE / 2.0);

		Arc2D.Double arc = new Arc2D.Double(x, y, SENSOR_RANGE, SENSOR_RANGE,
				// why does Java2D insist on using degrees here?!?
				(double) Math.toDegrees(startAngle - agent.getRotation()),
				(double) Math.toDegrees(endAngle - startAngle), Arc2D.PIE);
		return arc;
	}

	/**
	 * Used to render this <code>GoalSensor</code> to the <code>Graphics2D</code> instance, using the
	 * provided aspect ratio.
	 * 
	 * @param g2d the <code>Graphics2D</code> instance for rendering with,
	 * @param pixelRatio the aspect ratio to use for rendering.
	 */	
	public void paint(Graphics2D g, double pixelRatio) {
		if (containsGoal()) {
			g.setColor(Color.MAGENTA);
			Composite lastComp = g.getComposite();

			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					0.05f));
			g.fill(ShapeHelper.convertArc(getShape(), pixelRatio));
			g.setComposite(lastComp);
		}
	}
}
