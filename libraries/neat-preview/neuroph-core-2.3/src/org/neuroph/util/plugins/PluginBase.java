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

package org.neuroph.util.plugins;

import java.io.Serializable;

import org.neuroph.core.NeuralNetwork;

/**
 * Base class for all neural network plugins.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class PluginBase implements Serializable {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */
	private static final long serialVersionUID = 1L;	
	
	/**
	 * Name for this plugin
	 */
	private String name;
	
	/**
	 * Reference to parent neural network
	 */
	private NeuralNetwork parentNetwork;

	/**
	 * Creates an instance of plugin for neural network
	 */	
	public PluginBase(String name) {
		this.name=name;
	}
	
	/**
	 * Returns the name of this plugin
	 * @return name of this plugin
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the parent network for this plugin
	 * @return parent network for this plugin
	 */
	public NeuralNetwork getParentNetwork() {
		return parentNetwork;
	}

	/**
	 * Sets the parent network for this plugin
	 * @param parentNetwork parent network for this plugin
	 */
	public void setParentNetwork(NeuralNetwork parentNetwork) {
		this.parentNetwork = parentNetwork;
	}

}
