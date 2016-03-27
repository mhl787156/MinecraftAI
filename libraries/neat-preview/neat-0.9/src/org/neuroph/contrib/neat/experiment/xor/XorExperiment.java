package org.neuroph.contrib.neat.experiment.xor;

import java.util.Arrays;
import java.util.logging.Logger;

import org.neuroph.contrib.neat.gen.Evolver;
import org.neuroph.contrib.neat.gen.NeuronGene;
import org.neuroph.contrib.neat.gen.NeuronType;
import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.impl.SimpleNeatParameters;
import org.neuroph.contrib.neat.gen.operations.selector.NaturalSelectionOrganismSelector;
import org.neuroph.contrib.neat.gen.operations.speciator.DynamicThresholdSpeciator;
import org.neuroph.contrib.neat.gen.persistence.PersistenceException;
import org.neuroph.core.NeuralNetwork;

/**
 * An implementation of the basic XOR experiment provided by most neural network libraries
 * to demonstrate how to use the NEAT-contrib package for Neuroph.
 * 
 * @author Aidan Morgan
 */
public class XorExperiment {
	/**
	 * I dont think Neuroph has a logging framework defined, so use JDK logging.
	 */
	private static Logger s_log = Logger.getLogger(XorExperiment.class.getName());
	
	public static void main(String [] args) throws PersistenceException {
		SimpleNeatParameters params = new SimpleNeatParameters();
		params.setFitnessFunction(new XorFitnessFunction());
		params.setPopulationSize(300);
		params.setMaximumFitness(XorFitnessFunction.MAXIMUM_FITNESS);
		// as NN's only approximate functions there is no chance that we will generate the maximum
		// fitness, so enforce a number of generations limit to stop us running forever.
		params.setMaximumGenerations(1000);
		
		NaturalSelectionOrganismSelector selector = new NaturalSelectionOrganismSelector();
		selector.setKillUnproductiveSpecies(true);
		params.setOrganismSelector(selector);
		
		DynamicThresholdSpeciator speciator = new DynamicThresholdSpeciator();
		speciator.setMaxSpecies(4);
		params.setSpeciator(speciator);
		
		NeuronGene inputOne = new NeuronGene(NeuronType.INPUT, params);
		NeuronGene inputTwo = new NeuronGene(NeuronType.INPUT, params);
		NeuronGene output = new NeuronGene(NeuronType.OUTPUT, params);
		
		// create the evolver and run.
		Evolver e = Evolver.createNew(params, Arrays.asList(inputOne, inputTwo), Arrays.asList(output));
		Organism best = e.evolve();
		
		s_log.info("Winning organism is " + best.getInnovationId());
		
		NeuralNetwork nn = params.getNeuralNetworkBuilder().createNeuralNetwork(best); 
		s_log.info("Test 1: [0,0] = " + runNetwork(nn, 0.0, 0.0));
		s_log.info("Test 2: [1,0] = " + runNetwork(nn, 1.0, 0.0));
		s_log.info("Test 3: [0,1] = " + runNetwork(nn, 0.0, 1.0));
		s_log.info("Test 4: [1,1] = " + runNetwork(nn, 1.0, 1.0));
	}
	
	private static double runNetwork(NeuralNetwork winner, double one, double two) {
		return XorFitnessFunction.runNetwork(winner, one, two);
	}
}
