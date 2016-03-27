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

/**
 * Contains transfer functions types and labels.
 */
public enum TransferFunctionType {
	LINEAR("Linear"),
	RAMP("Ramp"),
	STEP("Step"),
	SIGMOID("Sigmoid"),
	TANH("Tanh"),
	GAUSSIAN("Gaussian"),
	TRAPEZOID("Trapezoid"),
	SGN("Sgn");

	private String typeLabel;
	
	private TransferFunctionType(String typeLabel) {
		this.typeLabel = typeLabel;
	}
	
	public String getTypeLabel() {
		return typeLabel;
	}
}
