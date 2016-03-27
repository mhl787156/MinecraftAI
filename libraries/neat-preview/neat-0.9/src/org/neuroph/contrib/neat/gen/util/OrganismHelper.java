package org.neuroph.contrib.neat.gen.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.Specie;

public class OrganismHelper {
	public static Set<Specie> getSpecies(List<Organism> organisms) {
		Set<Specie> ret = new HashSet<Specie>();

		for (Organism o : organisms) {
			ret.add(o.getSpecie());
		}

		return ret;
	}

	// public static Organism getOrganismWithBestFitness(List<Organism>
	// organisms) {
	// Organism best = null;
	//		
	// for(Organism o : organisms) {
	// if(o.hasFitnessSet()) {
	// if(best == null) {
	// best = o;
	// }
	// else if(best.getFitness() < o.getFitness()) {
	// best = o;
	// }
	// }
	// }
	//		
	// return best;
	// }

	// public static double getBestFitness(List<Organism> organisms) {
	// return getOrganismWithBestFitness(organisms).getFitness();
	// }

	public static List<Organism> getOrganisms(List<Specie> species) {
		List<Organism> ret = new ArrayList<Organism>();

		for (Specie s : species) {
			ret.addAll(s.getOrganisms());
		}

		return ret;
	}

	public static List<Organism> copy(NeatParameters params,
			List<Organism> organisms) {
		List<Organism> org = new ArrayList<Organism>();

		for (Organism o : organisms) {
			org.add(o.copy());
		}

		return org;
	}
}
