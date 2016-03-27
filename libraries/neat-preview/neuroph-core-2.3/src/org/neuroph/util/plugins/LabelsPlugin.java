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

import java.util.HashMap;
import java.util.Map;

/**
 * Provides labeling of all neural network components
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class LabelsPlugin extends PluginBase {

	public static final String LABELS_PLUGIN_NAME = "LabelsPlugin";

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */
	private static final long serialVersionUID = 3L;

	/**
	 * Collection of labels for the neural network components
	 */
	private Map<Object, String> labels = new HashMap<Object, String>();

	/**
	 * Field for neural network label
	 * This field is required to solve the java bug described at
	 * http://bugs.sun.com/view_bug.do?bug_id=4957674
	 */
	private String neuralNetworkLabel = new String();

	public LabelsPlugin() {
		super(LABELS_PLUGIN_NAME);
	}

	/**
	 * Returns label for the specified object
	 * @param object object for which label should be returned
	 * @return label for the specified object
	 */
	public String getLabel(Object object) {
		if (object != getParentNetwork()) {
			return labels.get(object);
		} else {
			return neuralNetworkLabel;
		}
	}

	/**
	 * Sets label for the specified object
	 * @param object object to set label
	 * @param label label to set
	 */
	public void setLabel(Object object, String label) {
		if (object == getParentNetwork()) {
			neuralNetworkLabel = label;
		} else {
			labels.put(object, label);
		}
	}
}
