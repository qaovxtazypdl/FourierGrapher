import java.util.*;

// Class representing the integrals.
public class Integral extends AbstractExpression {
	// Bounds of integration.
	private AbstractExpression lowerLimit, upperLimit;
	
	// Expression to integrate
	private AbstractExpression expArg;
	
	// Variable to integrate w.r.t.
	private String intVar;
	
	// Number of partitions to use in approximating the integral.
	private int partitions;
	
	// Constructor
	Integral(AbstractExpression expArg, AbstractExpression lowerLimit, AbstractExpression upperLimit, int partitions, String intVar) {
		this.expArg = expArg;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.intVar = intVar;
		this.partitions = partitions;
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractExpression#evaluate(java.util.Map)
	 */
	public double evaluate(Map<String,Double> varList) {
		// Simpson's rule implementation of integral approximation.
		// Make a copy of the list, since we are changing it.
		Map<String,Double> newList = new HashMap<String,Double>(varList);
		double lower = lowerLimit.evaluate(varList);
		double upper = upperLimit.evaluate(varList);
		double partitionWidth = (upper - lower) / partitions;
		double[] partitionHeights = expArg.intervalEvaluation(newList, lower, upper, partitions, intVar);
		double integralSum = 0;
		for (int i = 0; i < partitions; i++) {
			newList.put(intVar, (lower + (i * partitionWidth) + (partitionWidth / 2)));
			double simpsonProduct = partitionHeights[i] + partitionHeights[i+1] + 4 * expArg.evaluate(newList);
			integralSum += (partitionWidth / 6) * simpsonProduct;
		}
		
		return integralSum;
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractExpression#toString()
	 */
	public String toString() {
		return "INTEGRAL_" + lowerLimit + "^" + upperLimit + "(" + expArg.toString() + " d" + intVar + ")";
	}
}