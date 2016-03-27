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

import org.neuroph.core.transfer.TransferFunction;

/**
 * Transfer function for Neuron in Interactive Activation Neural Network.
 * @see org.neuroph.contrib.IACNetwork
 * @see org.neuroph.contrib.IACNeuron
 * @author Zoran Sevarac <sevarac@gmail.com>
 * http://www.itee.uq.edu.au/~cogs2010/cmc/chapters/IAC/index2.html#Mechanism
 * If the activation of a unit is equal to max then the net believes the hypothesis completely. 
 * If it is equal to min then the net disbelieves the hypothesis completely.
 * The rest corresponds to an "I don't know state". The (max - a i) or (a i - min)
 * terms ensure that the activation remains between min and max and doesn't continue to either grow
 * or shrink without bound [2]. The -decay (ai - rest) part of the equation forces the activation to return
 * to the rest value in the absence of external input.
 */
public class IACFunction extends TransferFunction {
	private static final long serialVersionUID = 1L;
	
	double max = 1,
            min = -0.2,
            rest = -0.1,
            decay = 0.1;

    public double getOutput(double netInput, double output) {
        double delta = 0;
        
        if (netInput>0) 
            delta = (max - output)*netInput - decay*(output - rest);
        else
            delta = (output - min)*netInput - decay*(output - rest);

        output = output + delta;

        return output;
    }

    public double getOutput(double netInput) {

        throw new RuntimeException("Method getOutput(double netInput) not implemented for this type of TransferFunctions. Use getOutput(double netInput, double output)");
    }

}
