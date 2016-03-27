package com.minecraftAi.ANNEngine.Action.GatherWood;

import com.minecraftAi.ANNEngine.AFitnessFunction;
import com.minecraftAi.ANNEngine.Action.ActionController;
import org.neuroph.contrib.neat.gen.NeuronGene;
import org.neuroph.contrib.neat.gen.impl.SimpleNeatParameters;
import org.neuroph.core.NeuralNetwork;

import java.util.List;

/**
 * Created by Mickey on 26/03/2016.
 */
public class GatherWoodController extends ActionController{

    @Override
    protected void setupNeatParamters(){
        params = new SimpleNeatParameters();

        params.setPopulationSize(150);
        params.setMaximumFitness(1000);
        params.setMaximumGenerations(100);
    }

    @Override
    protected List<NeuronGene> createInputNeuronGenes() {
        return null;
    }

    @Override
    protected List<NeuronGene> createOutputNeuronGenes() {
        return null;
    }

    @Override
    public double calculateNeuralNetFitness(NeuralNetwork net) {
        return 0;
    }


}
