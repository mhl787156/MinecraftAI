package com.minecraftAi.ANNEngine.Action;

import com.minecraftAi.ANNEngine.AFitnessFunction;
import com.minecraftAi.ANNEngine.AIANNEngine;
import org.neuroph.contrib.neat.gen.NeuronGene;
import org.neuroph.contrib.neat.gen.impl.SimpleNeatParameters;
import org.neuroph.core.NeuralNetwork;

import java.util.List;

/**
 * Created by Mickey on 27/03/2016.
 */
public abstract class ActionController extends AIANNEngine {
    protected SimpleNeatParameters params;
    protected List<NeuronGene> inputList;
    protected List<NeuronGene> outputList;

    protected ActionController(){
        setupNeatParamters();
        params.setFitnessFunction(new FlexiFitnessFunction());
        this.inputList = createInputNeuronGenes();
        this.outputList = createOutputNeuronGenes();
    }


    protected abstract void setupNeatParamters();
    protected abstract List<NeuronGene> createInputNeuronGenes();
    protected abstract List<NeuronGene> createOutputNeuronGenes();
    public abstract double calculateNeuralNetFitness(NeuralNetwork net);


    private class FlexiFitnessFunction extends AFitnessFunction {
        public double calculateNetworkFitness(NeuralNetwork net) {
            return calculateNeuralNetFitness(net);
        }
    }
}
