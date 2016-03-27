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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.neuroph.core.Neuron;
import org.neuroph.util.plugins.LabelsPlugin;
import org.neuroph.util.plugins.PluginBase;

/**
 * Provides image recognition specific properties like sampling resolution, and easy to
 * use image recognition interface for neural network.
 *
 * @author Jon Tait
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class ImageRecognitionPlugin extends PluginBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String IMG_REC_PLUGIN_NAME = "Image Recognition Plugin";

	/**
	 * Image sampling resolution (image dimensions)
	 */
	private Dimension samplingResolution;

        /**
         * Color mode used for recognition (full color or black and white)
         */
        private ColorMode colorMode;

	/**
	 * Constructor
	 * 
	 * @param samplingResolution
	 *            image sampling resolution (dimensions)
	 */
	public ImageRecognitionPlugin(Dimension samplingResolution) {
		super(IMG_REC_PLUGIN_NAME);
		this.samplingResolution = samplingResolution;
                this.colorMode = ColorMode.FULL_COLOR;
	}

	/**
	 * Constructor
	 *
	 * @param samplingResolution
	 *            image sampling resolution (dimensions)
         * @param colorMode recognition color mode 
	 */
	public ImageRecognitionPlugin(Dimension samplingResolution, ColorMode colorMode) {
		super(IMG_REC_PLUGIN_NAME);
		this.samplingResolution = samplingResolution;
                this.colorMode = colorMode;
	}

	/**
	 * Returns image sampling resolution (dimensions)
	 * 
	 * @return image sampling resolution (dimensions)
	 */
	public Dimension getSamplingResolution() {
		return samplingResolution;
	}

        /**
         * Returns color mode used for image recognition
         * @return color mode used for image recognition
         */
        public ColorMode getColorMode() {
            return this.colorMode;
        }

	/**
	 * Sets network input (image to recognize) from the specified BufferedImage
	 * object
	 * 
	 * @param img
	 *            image to recognize
	 */
	public void setInput(BufferedImage img) {
		FractionRgbData imgRgb = new FractionRgbData(ImageSampler
				.downSampleImage(samplingResolution, img));
		double input[];

		if (colorMode == ColorMode.FULL_COLOR)
			input = imgRgb.getFlattenedRgbValues();
		else if (colorMode == ColorMode.BLACK_AND_WHITE)
			input = FractionRgbData.convertRgbInputToBinaryBlackAndWhite(imgRgb
					.getFlattenedRgbValues());
		else
			throw new RuntimeException("Unknown color mode!");

		this.getParentNetwork().setInput(input);
	}

	/**
	 * Sets network input (image to recognize) from the specified File object
	 * 
	 * @param imgFile
	 *            file of the image to recognize
	 */
	public void setInput(File imgFile) throws IOException {
		BufferedImage img = ImageIO.read(imgFile);
		this.setInput(img);
	}

	/**
	 * Sets network input (image to recognize) from the specified URL object
	 * 
	 * @param imgURL
	 *            url of the image
	 */
	public void setInput(URL imgURL) throws IOException {
		BufferedImage img = ImageIO.read(imgURL);
		this.setInput(img);
	}

        public void processInput() {
                getParentNetwork().calculate();
        }

	/**
	 * Returns image recognition result as map with image labels as keys and
	 * recogition result as value
	 * 
	 * @return image recognition result
	 */
	public HashMap<String, Double> getOutput() {
		LabelsPlugin labelsPlugin = (LabelsPlugin) this.getParentNetwork()
				.getPlugin(LabelsPlugin.LABELS_PLUGIN_NAME);
		HashMap<String, Double> networkOutput = new HashMap<String, Double>();

		for (Neuron neuron : this.getParentNetwork().getOutputNeurons()) {
			String neuronLabel = labelsPlugin.getLabel(neuron);
			networkOutput.put(neuronLabel, neuron.getOutput());
		}

		return networkOutput;
	}


	/**
	 * This method performs the image recognition for specified image.
	 * Returns image recognition result as map with image labels as keys and
	 * recogition result as value
	 *
	 * @return image recognition result
	 */
        public HashMap<String, Double> recognizeImage(BufferedImage img) {
		setInput(img);
		processInput();
                return getOutput();
        }

	/**
	 * This method performs the image recognition for specified image file.
	 * Returns image recognition result as map with image labels as keys and
	 * recogition result as value
	 *
	 * @return image recognition result
	 */
        public HashMap<String, Double> recognizeImage(File imgFile)  throws IOException {
		setInput(imgFile);
		processInput();
                return getOutput();
        }

	/**
	 * This method performs the image recognition for specified image URL.
	 * Returns image recognition result as map with image labels as keys and
	 * recogition result as value
	 *
	 * @return image recognition result
	 */
        public HashMap<String, Double> recognizeImage(URL imgURL)  throws IOException {
		setInput(imgURL);
		processInput();
                return getOutput();
        }

	/**
	 * Returns one or more image labels with the maximum output - recognized
	 * images
	 * 
	 * @return one or more image labels with the maximum output
	 */
	public HashMap<String, Neuron> getMaxOutput() {
		HashMap<String, Neuron> maxOutput = new HashMap<String, Neuron>();
		Neuron maxNeuron = this.getParentNetwork().getOutputNeurons()
				.elementAt(0);

		for (Neuron neuron : this.getParentNetwork().getOutputNeurons()) {
			if (neuron.getOutput() > maxNeuron.getOutput())
				maxNeuron = neuron;
		}

		LabelsPlugin labels = (LabelsPlugin) this.getParentNetwork().getPlugin(
				LabelsPlugin.LABELS_PLUGIN_NAME);

		maxOutput.put(labels.getLabel(maxNeuron), maxNeuron);

		for (Neuron neuron : this.getParentNetwork().getOutputNeurons()) {
			if (neuron.getOutput() == maxNeuron.getOutput()) {
				maxOutput.put(labels.getLabel(neuron), neuron);
			}
		}

		return maxOutput;
	}

}