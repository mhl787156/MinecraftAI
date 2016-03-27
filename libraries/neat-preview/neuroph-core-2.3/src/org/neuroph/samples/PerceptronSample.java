/***
 * Neuroph  http://neuroph.sourceforge.net
 * Copyright by Neuroph Project (C) 2008
 *
 * This file is part of Neuroph framework.
 *
 * Neuroph is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Neuroph is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Neuroph. If not, see <http://www.gnu.org/licenses/>.
 */

package org.neuroph.samples;

import java.util.Vector;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.Perceptron;

/**
 * This sample shows how to create, train, save and load simple Perceptron neural network
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class PerceptronSample {

    /**
     * Runs this sample
     */
    public static void main(String args[]) {

        // create training set (logical AND function)
        TrainingSet trainingSet = new TrainingSet();
        trainingSet.addElement(new SupervisedTrainingElement(new double[]{0, 0}, new double[]{0}));
        trainingSet.addElement(new SupervisedTrainingElement(new double[]{0, 1}, new double[]{0}));
        trainingSet.addElement(new SupervisedTrainingElement(new double[]{1, 0}, new double[]{0}));
        trainingSet.addElement(new SupervisedTrainingElement(new double[]{1, 1}, new double[]{1}));

        // create perceptron neural network
        NeuralNetwork myPerceptron = new Perceptron(2, 1);
        
        // learn the training set
        myPerceptron.learnInSameThread(trainingSet);

        // test perceptron
        System.out.println("Testing trained perceptron");
        testNeuralNetwork(myPerceptron, trainingSet);

        // save trained perceptron
        myPerceptron.save("mySamplePerceptron.nnet");

        // load saved neural network
        NeuralNetwork loadedPerceptron = NeuralNetwork.load("mySamplePerceptron.nnet");
        
        // test loaded neural network
        System.out.println("Testing loaded perceptron");
        testNeuralNetwork(loadedPerceptron, trainingSet);
    }

    /**
     * Prints network output for the each element from the specified training set.
     * @param neuralNet neural network
     * @param trainingSet training set
     */
    public static void testNeuralNetwork(NeuralNetwork neuralNet, TrainingSet trainingSet) {

        for(TrainingElement trainingElement : trainingSet.trainingElements()) {
            neuralNet.setInput(trainingElement.getInput());
            neuralNet.calculate();
            Vector<Double> networkOutput = neuralNet.getOutput();

            System.out.print("Input: " + trainingElement.getInput());
            System.out.println(" Output: " + networkOutput);
        }
    }
}
