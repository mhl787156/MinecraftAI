package org.neuroph.contrib.neat.experiment.xor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.operations.fitness.AbstractFitnessFunction;
import org.neuroph.core.NeuralNetwork;

/**
 * Implementation of the XOR fitness function that runs the four tests to determine
 * how well the <code>Organism</code> as implemented the XOR function.
 *  
 * @author Aidan Morgan
 */
public class XorFitnessFunction extends AbstractFitnessFunction {
	public static final double MAXIMUM_FITNESS = 16.0;

	@Override
	protected double evaluate(Organism o, NeuralNetwork nn) {
		nn.reset();
		
		double errorsum = runFitness(nn);
	    double val = Math.pow((4.0 - errorsum), 2.0);
	    
	    return val;
	}

	private double runFitness(NeuralNetwork net) {
		double[] out = new double[4];
		
		int count = 0;
		for(double[] d : getTestData()) {
			out[count]= runNetwork(net, d[0], d[1]);
			count++;
		}
		
		double errorsum = (Math.abs(out[0])+Math.abs(1.0-out[1])+Math.abs(1.0-out[2])+Math.abs(out[3]));
		return errorsum;
	}
	
	
	private List<double[]> getTestData() {
		ArrayList<double[]> al = new ArrayList<double[]>();
		al.add(new double[] {0.0,0.0,0.0});
		al.add(new double[] {1.0,0.0,1.0});
		al.add(new double[] {0.0,1.0,1.0});
		al.add(new double[] {1.0,1.0,0.0});

		// The original NEAT implementation doesn't deal with randomised inputs, so I won't either.  
//		Collections.shuffle(al);
		
		return al;
	}
	
	/**
	 * Performs a XOR test using the provided <code>NeuralNetwork</code>.
	 * @param nn the <code>NeuralNetwork</code> under test.
	 * @param organismId the id of the <code>Organism</code> which the <code>NeuralNetwork</code> is
	 * created from.
	 * @param one the first parameter to the XOR function.
	 * @param two the second parameter to the XOR function.
	 * @param expected the expected return value of the XOR function given the two parameters.
	 * @return the difference between the expected return value and the value returned by the
	 * <code>NeuralNetwork</code>
	 */
	public static double performTest(NeuralNetwork nn, double one, double two, double expected) {
		double output = runNetwork(nn, one, two);
		return Math.abs(expected - output);
	}
	
	public static double runNetwork(NeuralNetwork nn, double one, double two) {
		Vector<Double> input = new Vector<Double>(Arrays.asList(one, two));
		nn.setInput(input);
		nn.calculate();
		
		List<Double> output = nn.getOutput();

		return output.get(0);
	}
}
