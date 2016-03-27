package com.minecraftAi.ANNEngine;

import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.operations.FitnessFunction;
import org.neuroph.contrib.neat.gen.operations.OrganismFitnessScore;
import org.neuroph.core.NeuralNetwork;

import java.util.List;

/**
 * Created by Mickey on 27/03/2016.
 */
public abstract class AFitnessFunction implements FitnessFunction {

    @Override
    public void evaluate(List<OrganismFitnessScore> fitnesses) {
        for (OrganismFitnessScore ofs : fitnesses) {
            Organism o = ofs.getOrganism();
            NeuralNetwork net = ofs.getNeuralNetwork();
            double fitness = calculateNetworkFitness(net);

            ofs.setFitness(fitness);
        }
    }

    public abstract double calculateNetworkFitness(NeuralNetwork net);
}
