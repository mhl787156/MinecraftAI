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

import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.Hopfield;

/**
 * This sample shows how to create and train Hopfield neural network
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class HopfieldSample {

    /**
     * Runs this sample
     */
    public static void main(String args[]) {

        // create training set (H and T letter in 3x3 grid)
        TrainingSet trainingSet = new TrainingSet();
        trainingSet.addElement(new TrainingElement(new double[]{1, 0, 1, 
                                                                1, 1, 1,
                                                                1, 0, 1})); // H letter
        
        trainingSet.addElement(new TrainingElement(new double[]{1, 1, 1,
                                                                0, 1, 0,
                                                                0, 1, 0})); // T letter
  
        // create hopfield network
        Hopfield myHopfield = new Hopfield(9);
        // learn the training set
        myHopfield.learnInSameThread(trainingSet);

        // test hopfield network
        System.out.println("Testing network");

        // add one more 'incomplete' H pattern for testing - it will be recognized as H
        trainingSet.addElement(new TrainingElement(new double[]{1, 0, 0,
                                                                1, 0, 1,
                                                                1, 0, 1}));


        // print network output for the each element from the specified training set.
        for(TrainingElement trainingElement : trainingSet.trainingElements()) {
            myHopfield.setInput(trainingElement.getInput());
            myHopfield.calculate();
            myHopfield.calculate();   
            Vector<Double> networkOutput = myHopfield.getOutput();

            System.out.print("Input: " + trainingElement.getInput());
            System.out.println(" Output: " + networkOutput);
        }

    }

}