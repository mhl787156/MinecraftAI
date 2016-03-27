package edu.uwa.aidan.robot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.neuroph.contrib.neat.gen.Evolver;
import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.NeuronGene;
import org.neuroph.contrib.neat.gen.NeuronType;
import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.impl.SimpleNeatParameters;
import org.neuroph.contrib.neat.gen.operations.MutationOperation;
import org.neuroph.contrib.neat.gen.operations.ReproductionOperation;
import org.neuroph.contrib.neat.gen.operations.mutation.AddConnectionMutationOperation;
import org.neuroph.contrib.neat.gen.operations.mutation.AddNeuronMutationOperation;
import org.neuroph.contrib.neat.gen.operations.mutation.WeightMutationOperation;
import org.neuroph.contrib.neat.gen.operations.reproduction.AbstractReproductionOperation;
import org.neuroph.contrib.neat.gen.operations.selector.NaturalSelectionOrganismSelector;
import org.neuroph.contrib.neat.gen.operations.speciator.DynamicThresholdSpeciator;
import org.neuroph.contrib.neat.gen.persistence.PersistenceException;
import org.neuroph.contrib.neat.gen.persistence.impl.DirectoryOutputPersistence;
import org.neuroph.contrib.neat.gen.persistence.impl.serialize.JavaSerializationDelegate;

import edu.uwa.aidan.robot.nnet.RobotFitnessFunction;
import edu.uwa.aidan.robot.world.WorldBuilder;
import edu.uwa.aidan.robot.world.xml.XmlWorldBuilder;


public class EvolveRobot {
	public static final String OUTPUT_DIRECTORY = "evolve-robot";
	private WorldBuilder worldBuilder;
	
	public static void main(String [] args) {
		EvolveRobot er = new EvolveRobot();
		er.evolve();
	}
	

	public EvolveRobot() {
	}
	
	
	private void evolve() {
		try {
			NeatParametersBuilder.getTrainParams();
			
			NeatParameters params = NeatParametersBuilder.neatParameters;

			Evolver ev = Evolver.createNew(params, createInputLayer(params),
					createOutputLayer(params));
			Organism o = ev.evolve();
		} catch (PersistenceException e) {
			throw new IllegalStateException("PersistenceException thrown evolving the robot behaviour.", e);
		}
	}
	
	
	public List<NeuronGene> createInputLayer(NeatParameters params) {
		List<NeuronGene> inputs = new ArrayList<NeuronGene>();

		for (int i = 0; i < 12; i++) {
			NeuronGene ng = new NeuronGene(NeuronType.INPUT, params);
			inputs.add(ng);
		}

		return inputs;
	}

	public List<NeuronGene> createOutputLayer(NeatParameters params) {
		List<NeuronGene> outputs = new ArrayList<NeuronGene>();

		for (int i = 0; i < 2; i++) {
			NeuronGene ng = new NeuronGene(NeuronType.OUTPUT, params);
			outputs.add(ng);
		}

		return outputs;
	}	
	
}
