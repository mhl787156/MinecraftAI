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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.neuroph.core.NeuralNetwork;

/**
 * This sample shows how to use the image recognition neural network in your applications.
 * IMPORTANT NOTE: specify filenames for neural network and test image, or you'll get IOException
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class ImageRecognitionSample {

    

    public static void main(String[] args) {
          // load trained neural network saved with easyNeurons (specify existing neural network file here)
          NeuralNetwork nnet = NeuralNetwork.load("MyImageRecognition.nnet");
          // get the image recognition plugin from neural network
          ImageRecognitionPlugin imageRecognition = (ImageRecognitionPlugin)nnet.getPlugin(ImageRecognitionPlugin.IMG_REC_PLUGIN_NAME);

          try {
                // image recognition is done here
                HashMap<String, Double> output = imageRecognition.recognizeImage(new File("someImage.jpg")); // specify some existing image file here
                System.out.println(output.toString());
          } catch(IOException ioe) {
              ioe.printStackTrace();
          }
    }
}
