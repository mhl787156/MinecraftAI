package edu.uwa.aidan.robot.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.neuroph.core.NeuralNetwork;

/**
 * A <code>Robot</code> is the entity that is controlled by a <code>NeuralNetwork</code>.
 * It's aim is to find the <code>Goal</code> within the number of steps available to it.
 * 
 *  It is equipped with <code>RangeSensor</code> and <code>GoalSensor</code>s which are used
 *  as the inputs to the <code>NeuralNetwork</code> to determine the movement behaviour.
 *  
 *  The output of the <code>NeuralNetwork</code> is interpreted to be the amount
 *  forward or backward the <code>Robot</code> should move, as well as the amount the <code>Robot</code>
 *  should turn left or right.
 * 
 * @author Aidan Morgan
 */
public class Robot {
	/**
	 * Logging instance.
	 */
	private static Logger s_log = Logger.getLogger(Robot.class
			.getSimpleName());

	/**
	 * The number of <code>RangeSensor</code>s that the <code>Robot</code> has.
	 */
	public static final int NUMBER_OF_RANGEFINDERS = 8;
	
	/**
	 * The number of <code>GoalSensor</code>s that the <code>Robot</code> has.
	 */
	public static final int NUMBER_OF_GOAL_SENSORS = 4;
	
	/**
	 * The radius of the <code>Ellipse2D</code> that represents the <code>Robot</code> for all
	 * geometric calculations.
	 */
	public static final double AGENT_RADIUS = 1.0;

	/**
	 * The <code>AgentWorld</code> that this <code>Robot</code> belongs to.
	 */
	private AgentWorld world;
	
	/**
	 * A <codE>List</code> of the <code>RangeSensor</code>s attached to this <code>Robot</code>.
	 */
	private List<RangeSensor> rangeSensors;
	
	/**
	 * A <code>List</code> of <code>GoalSensor</code>s attached to this <code>Robot</code>.
	 */
	private List<GoalSensor> goalSensors;

	/**
	 * The current rotation, in radians, of this <code>Robot</code>.
	 */
	private double rotation = 0;
	
	/**
	 * A <code>List</code> of <code>Point2D</code> recording every position that this <code>Robot</code>
	 * has been in through the duration of the simulation.
	 */
	private List<Point2D> trajectory;
	
	/**
	 * The current position of this <code>Robot</code>.
	 */
	private Point2D position;

	/**
	 * The <code>NeuralNetwork</code> that is being evaluated. This is the "brains" of the
	 * <code>Robot</code>.
	 */
	private NeuralNetwork network;

	/**
	 * If set to <code>true</code> then the <code>Robot</code> has found the goal and should no longer
	 * move.
	 */
	private boolean foundGoal = false;

	/**
	 * Constructor.
	 * @param nn the <code>NeuralNetwork</code> to use for determining the <code>Robot</code>s
	 * behaviour.
	 */
	public Robot(NeuralNetwork nn) {
		this.network = nn;
		this.trajectory = new ArrayList<Point2D>();

		createRangeSensors();
		createGoalSensors();
	}

	/**
	 * Helper method to initialise the <code>RangeSensor</code>s for this <code>Robot</code>.
	 */
	public void createRangeSensors() {
		this.rangeSensors = new ArrayList<RangeSensor>();
		double increment = (2.0 * Math.PI) / NUMBER_OF_RANGEFINDERS;

		for (int i = 0; i < NUMBER_OF_RANGEFINDERS; i++) {
			double angle = ((double) i) * increment;

			RangeSensor r = new RangeSensor(this, angle);
			rangeSensors.add(r);
		}
	}

	/**
	 * Helper method to initialise the <code>GoalSensor</code>s for this <code>Robot</code>.
	 */
	private void createGoalSensors() {
		goalSensors = new ArrayList<GoalSensor>();
		double increment = (2.0 * Math.PI) / (double) NUMBER_OF_GOAL_SENSORS;

		double currentAngle = Math.toRadians(45.0);

		for (int i = 0; i < NUMBER_OF_GOAL_SENSORS; i++) {
			double start = currentAngle;
			currentAngle += increment;
			double end = currentAngle;

			GoalSensor gs = new GoalSensor(this, i, start, end);
			goalSensors.add(gs);
		}
	}

	/**
	 * Returns the java <code>Shape</code> representation of this <code>Robot</code> that is used 
	 * for all geometric operations.
	 * @return the java <code>Shape</code> representation of this <code>Robot</code> that is used 
	 * for all geometric operations.
	 */		
	public Ellipse2D getShape() {
		Ellipse2D.Double ellipse = new Ellipse2D.Double(position.getX()
				- (AGENT_RADIUS), position.getY() - (AGENT_RADIUS),
				AGENT_RADIUS * 2.0, AGENT_RADIUS * 2.0);
		return ellipse;
	}

	/**
	 * Returns the current rotation, in radians, of this <code>Robot</code>.
	 * @return the current rotation, in radians, of this <code>Robot</code>.
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * Adjusts the current rotation of this <code>Robot</code> by the specified amount.
	 * @param rotDelta the amount to rotate the <code>Robot</code> by, in radians.
	 */
	public void rotate(double rotDelta) {
		this.rotation += rotDelta;
	}

	/**
	 * Returns the current <code>Point2D</code> position of this <code>Robot</code>.
	 * @return the current <code>Point2D</code> position of this <code>Robot</code>.
	 */
	public Point2D getPosition() {
		return position;
	}

	/**
	 * Returns the <code>AgentWorld</code> thay this <code>Robot</code> belongs to.
	 * @return the <code>AgentWorld</code> thay this <code>Robot</code> belongs to.
	 */
	public AgentWorld getWorld() {
		return world;
	}

	/**
	 * Moves this <code>Robot</code> forward by the provided amount, taking into consideration
	 * the <code>Robot</code>'s current rotation.
	 * 
	 * @param amount the amount to move this <code>Robot</code> forward by.
	 */
	public void moveForward(double amount) {
		double x = getPosition().getX() + (amount * Math.cos(rotation));
		double y = getPosition().getY() + (amount * Math.sin(rotation));

		Point2D newPosition = new Point2D.Double(x, y);

		setPosition(newPosition);
	}

	private void setPosition(Point2D newPosition) {
		// if we can move from the current position, to the new position, then update
		// our position, otherwise ignore it.
		if (world.canMove(position, newPosition)) {
			position = newPosition;
		}
	}

	public void moveBackwards(double amount) {
		moveForward(-amount);
	}

	public void turnLeft(double delta) {
		rotate(-delta);
	}

	public void turnRight(double delta) {
		rotate(delta);
	}

	void setWorld(AgentWorld agentWorld) {
		this.world = agentWorld;
		setPosition(world.getInitialLocation());
	}

	public void setPosition(double d, double e) {
		setPosition(new Point2D.Double(d, e));
	}

	public void step(long stepNum) {
		// short circuit out, if we've already found the goal then we're not
		// interested
		// in moving any more.
		if (foundGoal) {
			return;
		}

		// if we are inside the goal's radius, then set the foundGoal to true,
		// and return. This prevents the robot from moving once it has found the
		// goal.
		if (world.getGoal().containsPoint(position)) {
			foundGoal = true;
			return;
		}

		double[] inputs = new double[12];

		int index = 0;
		for (RangeSensor rs : rangeSensors) {
			inputs[index] = (rs.getDistanceToNearestObstacle() / rs.getSensorRange());
			index++;
		}

		for (GoalSensor gs : goalSensors) {
			if (gs.containsGoal()) {
				inputs[index] = (1.0);
			} else {
				inputs[index] = (0.0);
			}

			index++;
		}

		network.setInput(inputs);
		network.calculate();
		double[] outputs = network.getOutputAsArray();

		moveAgent(outputs);

		trajectory.add(getPosition());
	}

	private void moveAgent(double[] outputs) {
		double leftRight = outputs[1];

		if (leftRight > 0.5) {
			turnLeft(leftRight - 0.5);
		} else {
			turnRight(leftRight);
		}

		double forwardBackward = outputs[0];

		if (forwardBackward >= 0.5) {
			// amplify this value to make the agent move faster.
			moveForward((forwardBackward - 0.5) * 5);
		} else {
			moveBackwards(forwardBackward);
		}
	}

	public List<Point2D> getTrajectory() {
		return trajectory;
	}


	public void reset() {
		setPosition(world.getInitialLocation());
		trajectory.clear();
		rotation = (-Math.PI / 2.0);
		foundGoal = false;
	}

	public void paint(Graphics2D g, double pixelRatio) {
		g.setColor(Color.BLUE);
		g.draw(ShapeHelper.getEllipse(getShape(), pixelRatio));

		for (RangeSensor rs : rangeSensors) {
			rs.paint(g, pixelRatio);
		}

		for (GoalSensor gs : goalSensors) {
			gs.paint(g, pixelRatio);
		}

		double x = (position.getX() + (7.0 * Math.cos(rotation)));
		double y = (position.getY() + (7.0 * Math.sin(rotation)));

		g.setColor(Color.GREEN);
		g.draw(ShapeHelper.getLine(new Line2D.Double(position,
				new Point2D.Double(x, y)), pixelRatio));

		g.setColor(Color.ORANGE);
		Point2D last = null;
		for (Point2D point : trajectory) {
			if (last != null) {
				Line2D.Double line = new Line2D.Double(last, point);
				g.draw(ShapeHelper.getLine(line, pixelRatio));
			}

			last = point;
		}

	}

	List<RangeSensor> getRangeSensors() {
		return rangeSensors;
	}

	public boolean foundGoal() {
		return foundGoal;
	}

	public double getDistanceToGoal() {
		return world.getGoal().getPosition().distance(position);
	}

}
