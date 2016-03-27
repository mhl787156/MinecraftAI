package org.neuroph.contrib.neat.gen.util;

import java.util.ArrayList;
import java.util.List;

import org.neuroph.contrib.neat.gen.Specie;

public class SpecieHelper {
	public static List<Specie> copy(List<Specie> species) {
		List<Specie> ret = new ArrayList<Specie>();

		for (Specie s : species) {
			ret.add(s.copy());
		}

		return ret;
	}
}
