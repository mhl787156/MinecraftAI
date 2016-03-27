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

package org.neuroph.contrib;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.plugins.LabelsPlugin;


/**
 *<pre>
 * Interactive Activation Controller neural network.
 * Still under development, at the moment contains hard-coded Jets and Sharks example.
 * See http://www.itee.uq.edu.au/~cogs2010/cmc/chapters/IAC/ for more info
 *</pre>
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class IACNetwork extends NeuralNetwork {
	private static final long serialVersionUID = 1L;

/**
Name	Gang	Age	Education	Marital Status	Occupation
Art     Jets	40's	J.H.	Single          Pusher
Rick	Sharks	30's	H.S.	Divorced        Burglar
Sam     Jets	20's	College	Single          Bookie
Ralph	Jets	30's	J.H.	Single          Pusher
Lance	Jets	20's	J.H.	Married         Burglar
 *
 */

    /**
     * Constructs a new Interactive Activation Neural Network
     */
    public IACNetwork() {
        this.createNetwork();
    }

    // creates jets and sharks IAC example
    private void createNetwork() {
        // napraviti elegantni api za kreiranje vise layera NeuralNetwork.createLayers(6); // creates 6 empty layers
        this.addLayer(new Layer());  // Name
        this.addLayer(new Layer());  // Gang
        this.addLayer(new Layer());  // Age
        this.addLayer(new Layer());  // Education
        this.addLayer(new Layer());  // Marital Status
        this.addLayer(new Layer());  // Occupation
        
        this.addLayer(new Layer());  // Associations

        LabelsPlugin labels = (LabelsPlugin)this.getPlugin("LabelsPlugin");

        Layer layer = this.getLayerAt(0);
        
        IACNeuron neuron = new IACNeuron();
        labels.setLabel(neuron, "Art");
        layer.addNeuron(neuron);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "Rick");
        layer.addNeuron(neuron);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "Sam");
        layer.addNeuron(neuron);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "Ralph");
        layer.addNeuron(neuron);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "Lance");
        layer.addNeuron(neuron);

        ConnectionFactory.fullConnect(layer, -0.3);
        //----------------------------------------------

        layer = this.getLayerAt(1);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "Jets");
        layer.addNeuron(neuron);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "Sharks");
        layer.addNeuron(neuron);

        ConnectionFactory.fullConnect(layer, -0.3);

        //----------------------------------------------

        layer = this.getLayerAt(2);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "20's");
        layer.addNeuron(neuron);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "30's");
        layer.addNeuron(neuron);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "40's");
        layer.addNeuron(neuron);

        ConnectionFactory.fullConnect(layer, -0.3);
        
        //----------------------------------------------

        layer = this.getLayerAt(3);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "J.H.");
        layer.addNeuron(neuron);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "H.S.");
        layer.addNeuron(neuron);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "College");
        layer.addNeuron(neuron);

        ConnectionFactory.fullConnect(layer, -0.3);

        //----------------------------------------------

        layer = this.getLayerAt(4);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "Single");
        layer.addNeuron(neuron);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "Married");
        layer.addNeuron(neuron);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "Divorced");
        layer.addNeuron(neuron);

        ConnectionFactory.fullConnect(layer, -0.3);

        //----------------------------------------------

        layer = this.getLayerAt(5);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "Pusher");
        layer.addNeuron(neuron);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "Burglar");
        layer.addNeuron(neuron);

        neuron = new IACNeuron();
        labels.setLabel(neuron, "Bookie");
        layer.addNeuron(neuron);

        ConnectionFactory.fullConnect(layer, -0.3);

        //----------------------------------------------

        layer = this.getLayerAt(6);

        neuron = new IACNeuron(); // Art     Jets	40's	J.H.	Single     Pusher
        layer.addNeuron(neuron);
        this.createConnection(neuron, this.getLayerAt(0).getNeuronAt(0), 0.1);  // art
        this.createConnection(neuron, this.getLayerAt(1).getNeuronAt(0), 0.1);  // jets
        this.createConnection(neuron, this.getLayerAt(2).getNeuronAt(2), 0.1);  // 40'a
        this.createConnection(neuron, this.getLayerAt(3).getNeuronAt(0), 0.1);  // J.H.
        this.createConnection(neuron, this.getLayerAt(4).getNeuronAt(0), 0.1);  // Single
        this.createConnection(neuron, this.getLayerAt(5).getNeuronAt(0), 0.1);  // Pusher

        this.createConnection(this.getLayerAt(0).getNeuronAt(0), neuron, 0.1);  // art
        this.createConnection(this.getLayerAt(1).getNeuronAt(0), neuron, 0.1);  // jets
        this.createConnection(this.getLayerAt(2).getNeuronAt(2), neuron, 0.1);  // 40'a
        this.createConnection(this.getLayerAt(3).getNeuronAt(0), neuron, 0.1);  // J.H.
        this.createConnection(this.getLayerAt(4).getNeuronAt(0), neuron, 0.1);  // Single
        this.createConnection(this.getLayerAt(5).getNeuronAt(0), neuron, 0.1);  // Pusher


        neuron = new IACNeuron(); // Rick	Sharks	30's	H.S.	Divorced   Burglar
        layer.addNeuron(neuron);
        this.createConnection(neuron, this.getLayerAt(0).getNeuronAt(1), 0.1);  // Rick
        this.createConnection(neuron, this.getLayerAt(1).getNeuronAt(1), 0.1);  // Sharks
        this.createConnection(neuron, this.getLayerAt(2).getNeuronAt(1), 0.1);  // 30'a
        this.createConnection(neuron, this.getLayerAt(3).getNeuronAt(1), 0.1);  // H.S.
        this.createConnection(neuron, this.getLayerAt(4).getNeuronAt(2), 0.1);  // Divorced
        this.createConnection(neuron, this.getLayerAt(5).getNeuronAt(1), 0.1);  // Burglar

        this.createConnection(this.getLayerAt(0).getNeuronAt(1), neuron, 0.1);  // Rick
        this.createConnection(this.getLayerAt(1).getNeuronAt(1), neuron, 0.1);  // Sharks
        this.createConnection(this.getLayerAt(2).getNeuronAt(1), neuron, 0.1);  // 30'a
        this.createConnection(this.getLayerAt(3).getNeuronAt(1), neuron, 0.1);  // H.S.
        this.createConnection(this.getLayerAt(4).getNeuronAt(2), neuron, 0.1);  // Divorced
        this.createConnection(this.getLayerAt(5).getNeuronAt(1), neuron, 0.1);  // Burglar

        neuron = new IACNeuron(); // Sam     Jets	20's	College	Single     Bookie
        layer.addNeuron(neuron);
        this.createConnection(neuron, this.getLayerAt(0).getNeuronAt(2), 0.1);  // Sam
        this.createConnection(neuron, this.getLayerAt(1).getNeuronAt(0), 0.1);  // Jets
        this.createConnection(neuron, this.getLayerAt(2).getNeuronAt(0), 0.1);  // 20's
        this.createConnection(neuron, this.getLayerAt(3).getNeuronAt(2), 0.1);  // College
        this.createConnection(neuron, this.getLayerAt(4).getNeuronAt(0), 0.1);  // Single
        this.createConnection(neuron, this.getLayerAt(5).getNeuronAt(2), 0.1);  // Bookie

        this.createConnection(this.getLayerAt(0).getNeuronAt(2), neuron, 0.1);  // Sam
        this.createConnection(this.getLayerAt(1).getNeuronAt(0), neuron, 0.1);  // Jets
        this.createConnection(this.getLayerAt(2).getNeuronAt(0), neuron, 0.1);  // 20's
        this.createConnection(this.getLayerAt(3).getNeuronAt(2), neuron, 0.1);  // College
        this.createConnection(this.getLayerAt(4).getNeuronAt(0), neuron, 0.1);  // Single
        this.createConnection(this.getLayerAt(5).getNeuronAt(2), neuron, 0.1);  // Bookie

        neuron = new IACNeuron(); // Ralph	Jets	30's	J.H.	Single     Pusher
        layer.addNeuron(neuron);
        this.createConnection(neuron, this.getLayerAt(0).getNeuronAt(3), 0.1);  // Ralph
        this.createConnection(neuron, this.getLayerAt(1).getNeuronAt(0), 0.1);  // Jets
        this.createConnection(neuron, this.getLayerAt(2).getNeuronAt(1), 0.1);  // 30's
        this.createConnection(neuron, this.getLayerAt(3).getNeuronAt(0), 0.1);  // J.H.
        this.createConnection(neuron, this.getLayerAt(4).getNeuronAt(0), 0.1);  // Single
        this.createConnection(neuron, this.getLayerAt(5).getNeuronAt(0), 0.1);  // Pusher

        this.createConnection(this.getLayerAt(0).getNeuronAt(3), neuron, 0.1);  // Ralph
        this.createConnection(this.getLayerAt(1).getNeuronAt(0), neuron, 0.1);  // Jets
        this.createConnection(this.getLayerAt(2).getNeuronAt(1), neuron, 0.1);  // 30's
        this.createConnection(this.getLayerAt(3).getNeuronAt(0), neuron, 0.1);  // J.H.
        this.createConnection(this.getLayerAt(4).getNeuronAt(0), neuron, 0.1);  // Single
        this.createConnection(this.getLayerAt(5).getNeuronAt(0), neuron, 0.1);  // Pusher

        neuron = new IACNeuron(); // Lance	Jets	20's	J.H.	Married    Burglar
        layer.addNeuron(neuron);
        this.createConnection(neuron, this.getLayerAt(0).getNeuronAt(4), 0.1);  // Lance
        this.createConnection(neuron, this.getLayerAt(1).getNeuronAt(0), 0.1);  // Jets
        this.createConnection(neuron, this.getLayerAt(2).getNeuronAt(0), 0.1);  // 20's
        this.createConnection(neuron, this.getLayerAt(3).getNeuronAt(0), 0.1);  // J.H.
        this.createConnection(neuron, this.getLayerAt(4).getNeuronAt(1), 0.1);  // Married
        this.createConnection(neuron, this.getLayerAt(5).getNeuronAt(1), 0.1);  // Burglar

        this.createConnection(this.getLayerAt(0).getNeuronAt(4), neuron, 0.1);  // Lance
        this.createConnection(this.getLayerAt(1).getNeuronAt(0), neuron, 0.1);  // Jets
        this.createConnection(this.getLayerAt(2).getNeuronAt(0), neuron, 0.1);  // 20's
        this.createConnection(this.getLayerAt(3).getNeuronAt(0), neuron, 0.1);  // J.H.
        this.createConnection(this.getLayerAt(4).getNeuronAt(1), neuron, 0.1);  // Married
        this.createConnection(this.getLayerAt(5).getNeuronAt(1), neuron, 0.1);  // Burglar

        ConnectionFactory.fullConnect(layer, -0.3);

        //----------------------------------------------

        labels.setLabel(this, "IAC Test");
        this.setInputNeurons(this.getLayerAt(0).getNeurons()); // da i ovo setuje input

    }

}
