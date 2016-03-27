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

import java.util.Enumeration;
import java.util.Properties;

/**
 * Represents properties of a neuron.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class NeuronProperties extends Properties {
	private static final long serialVersionUID = 1L;
	

	public NeuronProperties() {
		this.setProperty("weightsFunction", WeightsFunctionType.WEIGHTED_INPUT);
		this.setProperty("summingFunction", SummingFunctionType.SUM);
		this.setProperty("transferFunction", TransferFunctionType.LINEAR);
		this.setProperty("neuronType", "Neuron");
	}

	public NeuronProperties(TransferFunctionType transferFunctionType) {
		this.setProperty("weightsFunction", WeightsFunctionType.WEIGHTED_INPUT);
		this.setProperty("summingFunction", SummingFunctionType.SUM);
		this.setProperty("transferFunction", transferFunctionType);
		this.setProperty("neuronType", "Neuron");
	}

	public NeuronProperties(WeightsFunctionType weightsFunctionType, SummingFunctionType summingFunctionType,
			TransferFunctionType transferFunctionType) {
		this.setProperty("weightsFunction", weightsFunctionType);
		this.setProperty("summingFunction", summingFunctionType);
		this.setProperty("transferFunction", transferFunctionType);
		this.setProperty("neuronType", "Neuron");
	}

	public void setProperty(String key, Object value) {
		this.setProperty(key, value);
	}

	public void setProperty(String key, Double value) {
		this.setProperty(key, value.toString());
	}
	
	public void setProperty(String key, TransferFunctionType value) {
		this.setProperty(key, value.toString());
	}

	public void setProperty(String key, WeightsFunctionType value) {
		this.setProperty(key, value.toString());
	}
	
	public void setProperty(String key, SummingFunctionType value) {
		this.setProperty(key, value.toString());
	}	

	public String getWeightsFunction() {
		return this.getProperty("weightsFunction").toString();
	}

	public String getSummingFunction() {
		return this.getProperty("summingFunction").toString();
	}

	public Properties getTransferFunctionProperties() {
		Properties tfProperties = new Properties();
		Enumeration<?> en = this.propertyNames();
		while (en.hasMoreElements()) {
			String name = en.nextElement().toString();
			if (name.contains("transferFunction")) {
				tfProperties.setProperty(name, this.getProperty(name));
			}
		}
		return tfProperties;
	}

}