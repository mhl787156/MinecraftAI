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

package org.neuroph.contrib.imgrec;

import java.awt.Dimension;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.VectorParser;
import org.neuroph.util.plugins.LabelsPlugin;
import org.neuroph.util.plugins.PluginBase;

/**
 * Provides methods to create neural network and training set for image recognition.
 * This class is mostly based on the code from tileclassification utility by Jon Tait
 * @author Jon Tait
 * @author Zoran Sevarac
 */
public class ImageRecognitionHelper {
		
	/**
         * Creates and returns new neural network for image recognition.
	 * Assumes that all of the FractionRgbData objects in the given map have identical 
	 * length arrays in them so that the input layer of the neural network can be 
	 * created here.
	 *
         * @param label neural network label
         * @param samplingResolution sampling resolution (image size)
	 * @param imageLabels image labels
         * @param layersNeuronsCount neuron counts in hidden layers
	 * @param transferFunctionType type of transfer function to use for neurons in network
         * @param colorMode color mode
	 */
	public static NeuralNetwork createNewNeuralNetwork(String label, Dimension samplingResolution, ColorMode colorMode, List<String> imageLabels,  Vector<Integer> layersNeuronsCount, TransferFunctionType transferFunctionType) {

                int numberOfInputNeurons;
                if (colorMode == ColorMode.FULL_COLOR)
                    numberOfInputNeurons = 3 * samplingResolution.width * samplingResolution.height;
                else
                    numberOfInputNeurons = samplingResolution.width * samplingResolution.height;

                int numberOfOuputNeurons = imageLabels.size();

		layersNeuronsCount.add(0, numberOfInputNeurons);
		layersNeuronsCount.add(numberOfOuputNeurons);
		
		System.out.println("Neuron layer size counts vector = " + layersNeuronsCount);
		
		NeuralNetwork neuralNetwork = new MultiLayerPerceptron(layersNeuronsCount, transferFunctionType);
   		LabelsPlugin labelsPlugin = (LabelsPlugin) neuralNetwork.getPlugin(LabelsPlugin.LABELS_PLUGIN_NAME);
		labelsPlugin.setLabel(neuralNetwork, label);
		PluginBase imageRecognitionPlugin = new ImageRecognitionPlugin(samplingResolution, colorMode);
		neuralNetwork.addPlugin(imageRecognitionPlugin);

		assignLabelsToOutputNeurons(neuralNetwork, imageLabels);
                neuralNetwork.setLearningRule(new MomentumBackpropagation());

            return neuralNetwork;
	}

        /**
         * Assign labels to output neurons
         * @param neuralNetwork neural network
         * @param imageLabels image labels
         */
	private static void assignLabelsToOutputNeurons(NeuralNetwork neuralNetwork, List<String> imageLabels) {
		Vector<Neuron> outputNeurons = neuralNetwork.getOutputNeurons();
		LabelsPlugin labelsPlugin = (LabelsPlugin) neuralNetwork.getPlugin(LabelsPlugin.LABELS_PLUGIN_NAME);
		
		for(int i=0; i<outputNeurons.size(); i++) {
			Neuron neuron = outputNeurons.get(i);
			String label = imageLabels.get(i);
			labelsPlugin.setLabel(neuron, label);
		}
	}

        /**
         * Creates training set for the specified image labels and rgb data
         * @param imageLabels image labels
         * @param rgbDataMap map collection of rgb data
         * @return training set for the specified image data
         */
	public static TrainingSet createTrainingSet(List<String> imageLabels, Map<String, FractionRgbData> rgbDataMap)
	{	
		TrainingSet trainingSet = new TrainingSet();

		for (Entry<String, FractionRgbData> entry : rgbDataMap.entrySet()) {
			double[] input = entry.getValue().getFlattenedRgbValues();
			double[] response = createResponse(entry.getKey(), imageLabels);
			trainingSet.addElement(new SupervisedTrainingElement(
					VectorParser.convertToVector(input), 
					VectorParser.convertToVector(response)));
		}

                return trainingSet;
	}

        /**
         * Creates binary black and white training set for the specified image labels and rgb data
         * @param imageLabels image labels
         * @param rgbDataMap map collection of rgb data
         * @return binary black and white training set for the specified image data
         */
        public static TrainingSet createBlackAndWhiteTrainingSet(List<String> imageLabels, Map<String, FractionRgbData> rgbDataMap)
	{
		TrainingSet trainingSet = new TrainingSet();

		for (Entry<String, FractionRgbData> entry : rgbDataMap.entrySet()) {
			double[] inputRGB = entry.getValue().getFlattenedRgbValues();
                        double[] inputBW = FractionRgbData.convertRgbInputToBinaryBlackAndWhite(inputRGB);
                        double[] response = createResponse(entry.getKey(), imageLabels);
			trainingSet.addElement(new SupervisedTrainingElement(
					VectorParser.convertToVector(inputBW),
					VectorParser.convertToVector(response)));
		}

            return trainingSet;
	}

        /**
         * Creates network output vector (response) for the specified image data
         * @param inputLabel label of the input image
         * @param imageLabels labels used for output neurons
         * @return network response for the specified input
         */
	private static double[] createResponse(String inputLabel, List<String> imageLabels)
	{
		double[] response = new double[imageLabels.size()];
		int i=0;
		for(String imageLabel : imageLabels) {
			if(inputLabel.startsWith(imageLabel)) {
				response[i] = 1d;
			} else {
				response[i] = 0d;
			}
			i++;
		}
		return response;
	}
}