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

import java.util.Properties;

import org.neuroph.core.Neuron;
import org.neuroph.core.input.And;
import org.neuroph.core.input.Diference;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.input.Intensity;
import org.neuroph.core.input.Max;
import org.neuroph.core.input.Min;
import org.neuroph.core.input.Or;
import org.neuroph.core.input.Product;
import org.neuroph.core.input.Sum;
import org.neuroph.core.input.SumSqr;
import org.neuroph.core.input.SummingFunction;
import org.neuroph.core.input.WeightedInput;
import org.neuroph.core.input.WeightsFunction;
import org.neuroph.core.transfer.Gaussian;
import org.neuroph.core.transfer.Linear;
import org.neuroph.core.transfer.Ramp;
import org.neuroph.core.transfer.Sgn;
import org.neuroph.core.transfer.Sigmoid;
import org.neuroph.core.transfer.Step;
import org.neuroph.core.transfer.Tanh;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.core.transfer.Trapezoid;
import org.neuroph.nnet.comp.CompetitiveNeuron;
import org.neuroph.nnet.comp.DelayedNeuron;
import org.neuroph.nnet.comp.InputOutputNeuron;
import org.neuroph.nnet.comp.ThresholdNeuron;

/**
 * Provides methods to create instances of Neurons.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class NeuronFactory {

	/**
	 * Creates and returns neuron according to the given specification.
	 * 
	 * @param neuronProperties
	 *            specification of neuron properties
	 * @return new neuron with specified properties
	 */
	public static Neuron createNeuron(NeuronProperties neuronProperties) {
		WeightsFunction weightsFunction = createWeightsFunction(WeightsFunctionType.valueOf(neuronProperties
				.getWeightsFunction()));
		SummingFunction summingFunction = createSummingFunction(SummingFunctionType.valueOf(neuronProperties
				.getSummingFunction()));
		InputFunction inputFunction = new InputFunction(weightsFunction,
				summingFunction);
		TransferFunction transferFunction = createTransferFunction(neuronProperties
				.getTransferFunctionProperties());

		Neuron neuron;
		String neuronType = neuronProperties.get("neuronType").toString();

		if (neuronType.equals("InputOutputNeuron")) {
			neuron = new InputOutputNeuron(inputFunction, transferFunction);
			double bias = Double.parseDouble(neuronProperties.get("bias")
					.toString());
			((InputOutputNeuron) neuron).setBias(bias);
		} else if (neuronType.equals("ThresholdNeuron")) {
			neuron = new ThresholdNeuron(inputFunction, transferFunction);
			double thresh = Double.parseDouble(neuronProperties.get("thresh")
					.toString());
			((ThresholdNeuron) neuron).setThresh(thresh);
		} else if (neuronType.equals("DelayedNeuron")) {
			neuron = new DelayedNeuron(inputFunction, transferFunction);
		} else if (neuronType.equals("CompetitiveNeuron")) {
			neuron = new CompetitiveNeuron(inputFunction, transferFunction);
		} else {
			neuron = new Neuron(inputFunction, transferFunction);
		}

		return neuron;
	}

	/**
	 * Creates and returns transfer function
	 * 
	 * @param transferFunctionType
	 *            trensfer function type
	 * @return Returns transfer function
	 */
	private static TransferFunction createTransferFunction(Properties tfProperties) {
		TransferFunction transferFunction;

		TransferFunctionType  transferFunctionType = TransferFunctionType.valueOf(tfProperties
                               .getProperty("transferFunction"));		

		switch (transferFunctionType) {
		case LINEAR:
			transferFunction = new Linear(tfProperties);
			break;
		case STEP:
			transferFunction = new Step(tfProperties);
			break;
		case RAMP:
			transferFunction = new Ramp(tfProperties);
			break;
		case SIGMOID:
			transferFunction = new Sigmoid(tfProperties);
			break;
		case TANH:
			transferFunction = new Tanh(tfProperties);
			break;
		case TRAPEZOID:
			transferFunction = new Trapezoid(tfProperties);
			break;
		case GAUSSIAN:
			transferFunction = new Gaussian(tfProperties);
			break;
		case SGN:
			transferFunction = new Sgn();
			break;
		default:
			throw new RuntimeException("Unknown tranfer function type!");
		} // switch

		return transferFunction;
	}

	/**
	 * Kreira funkciju mreznog ulaza.
	 * 
	 * @param getWeightsFunction
	 *            Id funkcije mreznog ulaza.
	 * @return Vraca trazenu funkciju mreznog ulaza.
	 */
	private static WeightsFunction createWeightsFunction(WeightsFunctionType weightsFunctionType) {
		WeightsFunction weightsFunction;

		switch (weightsFunctionType) {
		case WEIGHTED_INPUT:
			weightsFunction = new WeightedInput();
			break;
		case DIFERENCE:
			weightsFunction = new Diference();
			break;
		default:
			throw new RuntimeException("Unknown weights function type: "
					+ weightsFunctionType);
		}
		return weightsFunction;
	}

	/**
	 * Kreira ulaznu vektorsku funkciju.
	 * 
	 * @param inputId
	 *            Id ulazne funkcije.
	 * @return Vraca trazenu ulaznu funkciju.
	 */
	private static SummingFunction createSummingFunction(SummingFunctionType summingFunctionType) {
		SummingFunction summingFunction;

		switch (summingFunctionType) {
		case SUM:
			summingFunction = new Sum();
			break;
		case SUMSQR:
			summingFunction = new SumSqr();
			break;
		case INTENSITY:
			summingFunction = new Intensity();
			break;
		case AND:
			summingFunction = new And();
			break;
		case OR:
			summingFunction = new Or();
			break;
		case MIN:
			summingFunction = new Min();
			break;
		case MAX:
			summingFunction = new Max();
			break;
		case PRODUCT:
			summingFunction = new Product();
			break;
		default:
			throw new RuntimeException("Unknown suming function type: "
					+ summingFunctionType);
		}

		return summingFunction;
	}

}
