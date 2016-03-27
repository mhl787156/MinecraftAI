package org.neuroph.contrib.neat.experiment.multiplication;

import java.util.List;

import org.neuroph.contrib.neat.gen.operations.FitnessFunction;
import org.neuroph.contrib.neat.gen.operations.OrganismFitnessScore;
import org.neuroph.core.NeuralNetwork;

public class MultiplicationFitnessFunction implements FitnessFunction {

	public void evaluate(List<OrganismFitnessScore> fitnesses) {
		for(OrganismFitnessScore ofs : fitnesses) {
			ofs.setFitness(runNetwork(ofs.getNeuralNetwork()));
		}
	}

	public static double runNetwork(NeuralNetwork nn) {
		double fitness = 0.0;
		
		for(double i = -1.0; i <= 1.0; i+= 0.1) {
			for(double j = -1.0; j <= 1.0; j+= 0.1) {
				nn.setInput(i, j);
				nn.calculate();
				
				double fromNetwork = (nn.getOutputAsArray()[0] * 2.0) - 1.0;
				double actual = i * j;
				
				
				// Compare against the expected output
				double error = Math.pow(Math.abs(actual-fromNetwork),0.3);
				fitness += 1.0 - Math.min(1.0, error);
			}
		}
		
		return fitness;
	}
}
