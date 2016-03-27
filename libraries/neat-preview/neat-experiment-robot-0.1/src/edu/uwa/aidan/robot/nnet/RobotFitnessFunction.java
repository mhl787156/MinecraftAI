package edu.uwa.aidan.robot.nnet;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.neuroph.contrib.neat.gen.operations.FitnessFunction;
import org.neuroph.contrib.neat.gen.operations.OrganismFitnessScore;
import org.neuroph.core.NeuralNetwork;

import edu.uwa.aidan.robot.world.AgentWorld;
import edu.uwa.aidan.robot.world.Robot;
import edu.uwa.aidan.robot.world.WorldBuilder;

/**
 * The <code>FitnessFunction</code> for evaluating how the robot moves in the 
 * world.
 * 
 * This is an example of how you can easily parallelise the evaluation of
 * the <code>Organism</code>s to get a decent speedup.
 * 
 * @author Aidan Morgan
 */
public class RobotFitnessFunction implements FitnessFunction {
	/**
	 * The number of steps to run the simulation for.
	 */
	public static final long MAX_STEPS = 500;
	
	/**
	 * The <code>WorldBuilder</code> for creating new <code>AgentWorld</code> instances from.
	 */
	private WorldBuilder worldBuilder;

	/**
	 * Constructor.
	 * @param builder the <code>WorldBuilder</code> for creating new <code>AgentWorld</code> instances from.
	 */
	public RobotFitnessFunction(WorldBuilder builder) {
		this.worldBuilder = builder;
	}

	/**
	 * Returns the <code>WorldBuilder</code> for creating new <code>AgentWorld</code> instances from.
	 * @return the <code>WorldBuilder</code> for creating new <code>AgentWorld</code> instances from.
	 */
	public WorldBuilder getWorldBuilder() {
		return worldBuilder;
	}

	
	@Override
	public void evaluate(List<OrganismFitnessScore> fitnesses) {
		// leave 1 CPU spare for the OS etc., lets hog the rest!
		int numberOfParallelExecutions = Runtime.getRuntime().availableProcessors() - 1;
		
		// defect fix : this assumed that there was always > 1 processors, so take the minimum
		// of number of numberOfParallelExecutions (could potentially be 0 on single-core machines)
		// and 1.
		ExecutorService executor = Executors.newFixedThreadPool(Math.max(numberOfParallelExecutions, 1));
		
		for(OrganismFitnessScore ofs : fitnesses) {
			RobotEvaluator eval = new RobotEvaluator(worldBuilder, ofs, ofs.getNeuralNetwork());
			executor.submit(eval);
		}
		
		try {
			// this will block until all of the <code>Runnable</code>s have finished 
			// executing.
			executor.shutdown();
			
			// if we have waited more than an hour, then break out and shut the evolution
			// down, the processing shouldn't take more than an hour!
			executor.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e1) {
			throw new IllegalStateException(e1);
		}
	}

	/**
	 * Simple <code>Runnable</code> that will perform the evaluation of the robot.
	 * 
	 * @author Aidan Morgan
	 */
	private class RobotEvaluator implements Runnable {
		/**
		 * The <code>NeuralNetwork</code> under evaluation.
		 */
		private NeuralNetwork network;
		
		/**
		 * The <code>WorldBuilder</code> used for creating new <code>AgentWorld</code> instances.
		 */
		private WorldBuilder builder;
		
		/**
		 * The <code>OrganismFitnessScore</code> to update with the fitness once the evaluation is complete.
		 */
		private OrganismFitnessScore score;
		
		/**
		 * Constructor.
		 * 
		 * @param builder the <code>WorldBuilder</code> used for creating new <code>AgentWorld</code> instances.
		 * @param score the <code>OrganismFitnessScore</code> to update with the fitness once the evaluation is complete.
		 * @param network the <code>NeuralNetwork</code> under evaluation.
		 */
		public RobotEvaluator(WorldBuilder builder, OrganismFitnessScore score, NeuralNetwork network) {
			this.network = network;
			this.builder = builder;
			this.score = score;
		}
		
		@Override
		public void run() {
			Robot agent = new Robot(network);
			AgentWorld world = builder.createWorld();
			world.setRobot(agent);
			
			for(long i = 0; i < MAX_STEPS; i++) {
				world.step(i);
			}
			
			// as NEAT requires a maximisation problem, we take the worst possible case that could arise,
			// and subtract our actual score from it - creating a maximisation problem...
			score.setFitness(world.getWorstFitnessScore() - agent.getDistanceToGoal());
		}
	}
}
