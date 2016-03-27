package edu.uwa.aidan.robot;

import java.io.File;

import javax.swing.JFrame;

import org.neuroph.contrib.neat.gen.Generation;
import org.neuroph.contrib.neat.gen.Innovations;
import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.Specie;
import org.neuroph.contrib.neat.gen.persistence.Persistence;
import org.neuroph.contrib.neat.gen.persistence.PersistenceException;

import edu.uwa.aidan.robot.nnet.RobotFitnessFunction;
import edu.uwa.aidan.robot.ui.ReplayRobotPanel;
import edu.uwa.aidan.robot.world.WorldBuilder;
import edu.uwa.aidan.robot.world.xml.XmlWorldBuilder;

public class ReplayRobot {
	public static void main(String [] args) throws Exception {
		NeatParametersBuilder.getReplayParams();
		
		NeatParameters params = NeatParametersBuilder.neatParameters;
		long organismId = NeatParametersBuilder.organismId; 
		
		Organism o = loadOrganism(params, organismId);
		
		if(o == null) {
			throw new IllegalArgumentException("Could not load Organism with id " + organismId + ".");
		}

		WorldBuilder builder = ((RobotFitnessFunction)params.getFitnessFunction()).getWorldBuilder();
		
		ReplayRobotPanel panel = new ReplayRobotPanel(builder.createWorld(), params.getNeuralNetworkBuilder().createNeuralNetwork(o));
		
		JFrame frame = new JFrame("Replay " + o.getInnovationId());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.setSize(640, 480);
		frame.setVisible(true);
	}

	private static Organism loadOrganism(NeatParameters params, Long valueOf) throws PersistenceException {
		Persistence p = params.getPersistence();
		
		long numberOfGenerations = p.getGenerationCount();
		
		Innovations innovations = p.loadInnovations();
		
		for(long l = 1; l < numberOfGenerations; l++) {
			Generation g = p.loadGeneration(l, innovations);
			
			for(Specie s : g.getSpecies()) {
				for(Organism o : s.getOrganisms()) {
					if(o.getInnovationId() == valueOf) {
						return o;
					}
				}
			}
		}
		
		return null;
	}
}
