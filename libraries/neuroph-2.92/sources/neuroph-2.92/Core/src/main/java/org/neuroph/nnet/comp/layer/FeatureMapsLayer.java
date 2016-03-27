/**
 * Copyright 2013 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neuroph.nnet.comp.layer;

import org.neuroph.nnet.comp.Kernel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.util.NeuronProperties;

/**
 * This class represents an array of feature maps which are 2 dimensional layers
 * (Layer2D instances) and it is base class for Convolution and Pooling layers,
 * which are used in ConvolutionalNetwork
 *
 * @author Boris Fulurija
 * @author Zoran Sevarac
 * @see ConvolutionalLayer
 * @see PoolingLayer
 * @see org.neuroph.nnet.ConvolutionalNetwork
 */
public class FeatureMapsLayer extends Layer {

    static final ForkJoinPool mainPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());


    private static final long serialVersionUID = -6706741997689639209L;

    /**
     * Kernel used for all 2D layers (feature maps)
     */
    protected Kernel kernel;

    /**
     * Dimensions for all 2D layers (feature maps)
     */
    protected Layer2D.Dimensions mapDimensions;

    public List<Layer2D> getFeatureMaps() {
        return featureMaps;
    }

    /**
     * Collection of feature maps
     */
    //private Layer2D[] featureMaps; //
    private List<Layer2D> featureMaps;

    /**
     * Creates a new empty feature maps layer with specified kernel
     *
     * @param kernel kernel to use for all feature maps
     */
    public FeatureMapsLayer(Kernel kernel) {
        this.kernel = kernel;
        this.featureMaps = new ArrayList<>();
    }

    /**
     * Creates a new empty feature maps layer with specified kernel and
     * feature map dimensions.
     *
     * @param kernel        kernel used for all feature maps in this layer
     * @param mapDimensions mapDimensions of feature maps in this layer
     */
    public FeatureMapsLayer(Kernel kernel, Layer2D.Dimensions mapDimensions) {
        this.kernel = kernel;
        this.mapDimensions = mapDimensions;
        this.featureMaps = new ArrayList<>();
    }

    /**
     * Creates new feature maps layer with specified kernel and feature maps.
     * Also creates feature maps and neurons in feature maps;
     *
     * @param kernel        kernel used for all feature maps in this layer
     * @param mapDimensions mapDimensions of feature maps in this layer
     * @param mapCount      number of feature maps
     * @param neuronProp    properties for neurons in feature maps
     */
    public FeatureMapsLayer(Kernel kernel, Layer2D.Dimensions mapDimensions, int mapCount, NeuronProperties neuronProp) {
        this.kernel = kernel;
        this.mapDimensions = mapDimensions;
        this.featureMaps = new ArrayList<>();
        createFeatureMaps(mapCount, this.mapDimensions, neuronProp);
    }


    /**
     * Adds a feature map (2d layer) to this feature map layer
     * @param featureMap feature map to add
     */
    public void addFeatureMap(Layer2D featureMap) {
        if (featureMap == null) {
            throw new IllegalArgumentException("FeatureMap cant be null!");
        }

        featureMaps.add(featureMap);
        neurons.addAll(Arrays.asList(featureMap.getNeurons()));

    }

    /**
     * Creates and adds specified number of feature maps to this layer
     *
     * @param mapCount         number of feature maps to create
     * @param dimensions       feature map dimensions
     * @param neuronProperties properties of neurons in feature maps
     */
    protected final void createFeatureMaps(int mapCount, Layer2D.Dimensions dimensions, NeuronProperties neuronProperties) {
        for (int i = 0; i < mapCount; i++) {
            addFeatureMap(new Layer2D(dimensions, neuronProperties));
        }
    }

    /**
     * Returns feature map (Layer2D) at specified index
     *
     * @param index index of feature map
     * @return feature map (Layer2D instance) at specified index
     */
    public Layer2D getFeatureMap(int index) {
        return featureMaps.get(index);
    }

    /**
     * Returns number of feature maps in this layer
     *
     * @return number of feature maps in this layer
     */
    public int getNumberOfMaps() {
        return featureMaps.size();
    }

    /**
     * Returns neuron instance at specified (x, y) position at specified feature map layer
     *
     * @param x        neuron's x position
     * @param y        neuron's y position
     * @param mapIndex feature map index
     * @return neuron at specified (x, y, map) position
     */
    public Neuron getNeuronAt(int x, int y, int mapIndex) {
        Layer2D map = featureMaps.get(mapIndex);
        return map.getNeuronAt(x, y);
    }

    /**
     * Returns total number of neurons in all feature maps
     *
     * @return total number of neurons in all feature maps
     */
    @Override
    public int getNeuronsCount() {
        int neuronCount = 0;
        for (Layer2D map : featureMaps)
            neuronCount += map.getNeuronsCount();
        return neuronCount;
    }

    /**
     * Calculates this layer (all feature maps)
     */
    @Override
    public void calculate() {
        mainPool.invokeAll(featureMaps);
    }

    /**
     * Returns kernel used by all feature maps in this layer
     *
     * @return kernel used by all feature maps in this layer
     */
    public Kernel getKernel() {
        return kernel;
    }

    /**
     * Returns dimensions of feature maps in this layer
     *
     * @return dimensions of feature maps in this layer
     */
    public Layer2D.Dimensions getMapDimensions() {
        return mapDimensions;
    }


    /**
     * Creates connections between two feature maps. It does nothing here,
     * connectivity patterns are defined by subclasses...
     * Maybe it should be even removed from here...
     *
     * @param fromMap
     * @param toMap
     */
    public void connectMaps(Layer2D fromMap, Layer2D toMap) {
    }


}
