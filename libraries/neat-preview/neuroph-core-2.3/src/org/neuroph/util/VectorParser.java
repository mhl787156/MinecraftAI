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

package org.neuroph.util;

import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Provides methods to parse strings as Integer or Double vectors.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class VectorParser {

	/**
	 * This method parses input String and returns Integer vector
	 * @param str input String
	 * @return Integer vector
	 */
	static public Vector<Integer> parseInteger(String str) {
		StringTokenizer tok = new StringTokenizer(str);
		Vector<Integer> ret = new Vector<Integer>();
		while (tok.hasMoreTokens()) {
			Integer d = new Integer(tok.nextToken());
			ret.add(d);
		}
		return ret;
	}

	/**
	 * This method parses input String and returns Double vector
	 * @param str input String
	 * @return Double vector
	 */	
	static public Vector<Double> parseDouble(String str) {
		StringTokenizer tok = new StringTokenizer(str);
		Vector<Double> ret = new Vector<Double>();
		while (tok.hasMoreTokens()) {
			Double d = new Double(tok.nextToken());
			ret.add(d);
		}
		return ret;
	}

	public static Vector<Double> convertToVector(double[] array) {
		Vector<Double> vector = new Vector<Double>(array.length);
		
		for(double val : array) {
			vector.add(val);
		}
		
		return vector;
	}
	
	public static double[] convertToArray(Vector<Double> vector) {
		double[] array = new double[vector.size()];
		
		int i = 0;
		for(double d : vector) {
			array[i++] = d;
		}
		
		return array;
	}
}