package org.neuroph.contrib.neat.gen.util;

import java.util.ArrayList;
import java.util.List;

import org.neuroph.contrib.neat.gen.ConnectionGene;
import org.neuroph.contrib.neat.gen.Gene;
import org.neuroph.contrib.neat.gen.NeuronGene;

public class GeneHelper {

	public static List<Gene> copyConnections(List<ConnectionGene> connections) {
		List<Gene> con = new ArrayList<Gene>();

		for (ConnectionGene g : connections) {
			con.add(g.copy());
		}

		return con;
	}

	public static List<Gene> copyNeurons(List<NeuronGene> arrayList) {
		List<Gene> neu = new ArrayList<Gene>();

		for (NeuronGene ng : arrayList) {
			neu.add(ng.copy());
		}

		return neu;
	}

}
