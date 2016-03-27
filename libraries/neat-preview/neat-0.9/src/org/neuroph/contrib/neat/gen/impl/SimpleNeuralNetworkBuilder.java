package org.neuroph.contrib.neat.gen.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.neuroph.contrib.neat.gen.ConnectionGene;
import org.neuroph.contrib.neat.gen.NeuralNetworkBuilder;
import org.neuroph.contrib.neat.gen.NeuronGene;
import org.neuroph.contrib.neat.gen.NeuronType;
import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.transfer.Sigmoid;

/**
 * Implementation of the <code>NeuralNetworkBuilder</code> that creates a 
 * <code>NeuralNetwork</code> from the provided <code>Organism</code>.
 * 
 * @author Aidan Morgan
 */
public class SimpleNeuralNetworkBuilder implements NeuralNetworkBuilder {

	/**
	 * @inheritDoc
	 */
	public NeuralNetwork createNeuralNetwork(Organism o) {
		NeuralNetwork nn = new NeuralNetwork();
		Map<Long, Neuron> neuronMap = new HashMap<Long, Neuron>();

		Vector<Neuron> inputs = new Vector<Neuron>();
		Layer inputLayer = new Layer();
		for (NeuronGene ng : o.getNeurons(NeuronType.INPUT)) {
			if (ng.isEnabled()) {
				Neuron n = new Neuron(new InputFunction(), new Sigmoid(ng
						.getActivationResponse()));
				inputs.add(n);
				inputLayer.addNeuron(n);
				neuronMap.put(ng.getInnovationId(), n);
			}
		}
		nn.setInputNeurons(inputs);
		nn.addLayer(inputLayer);

		Layer hiddLayer = new Layer();
		for (NeuronGene ng : o.getNeurons(NeuronType.HIDDEN)) {
			if (ng.isEnabled()) {
				Neuron n = new Neuron(new InputFunction(), new Sigmoid(ng
						.getActivationResponse()));
				hiddLayer.addNeuron(n);
				neuronMap.put(ng.getInnovationId(), n);
			}
		}
		nn.addLayer(hiddLayer);

		Vector<Neuron> outputs = new Vector<Neuron>();
		Layer outputLayer = new Layer();
		for (NeuronGene ng : o.getNeurons(NeuronType.OUTPUT)) {
			Neuron n = new Neuron(new InputFunction(), new Sigmoid(ng
					.getActivationResponse()));
			outputs.add(n);
			outputLayer.addNeuron(n);
			neuronMap.put(ng.getInnovationId(), n);
		}
		nn.setOutputNeurons(outputs);
		nn.addLayer(outputLayer);

		for (ConnectionGene g : o.getConnections()) {
			if (g.isEnabled()) {
				nn.createConnection(neuronMap.get(g.getOriginId()), neuronMap
						.get(g.getEndpointId()), g.getWeight());
			}
		}

		return nn;
	}

}
