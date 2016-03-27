package org.neuroph.contrib.neat.experiment.multiplication;

import java.util.Arrays;
import java.util.logging.Logger;

import org.neuroph.contrib.neat.gen.Evolver;
import org.neuroph.contrib.neat.gen.NeuronGene;
import org.neuroph.contrib.neat.gen.NeuronType;
import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.impl.SimpleNeatParameters;
import org.neuroph.contrib.neat.gen.persistence.PersistenceException;

public class MultiplicationExperiment {
	private static Logger s_log = Logger.getLogger(MultiplicationExperiment.class.getName());
	
	public static void main(String [] args) throws PersistenceException {
		SimpleNeatParameters params = new SimpleNeatParameters();
		params.setFitnessFunction(new MultiplicationFitnessFunction());
		params.setPopulationSize(150);
		// as NN's only approximate functions there is no chance that we will generate the maximum
		// fitness, so enforce a number of generations limit to stop us running forever.
		params.setMaximumGenerations(200);
		
		NeuronGene inputOne = new NeuronGene(NeuronType.INPUT, params);
		NeuronGene inputTwo = new NeuronGene(NeuronType.INPUT, params);
		NeuronGene output = new NeuronGene(NeuronType.OUTPUT, params);
		
		// create the evolver and run.
		Evolver e = Evolver.createNew(params, Arrays.asList(inputOne, inputTwo), Arrays.asList(output));
		Organism best = e.evolve();
		
		s_log.info("Winning organism is " + best.getInnovationId());
	}
}
