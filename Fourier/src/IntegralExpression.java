import java.util.*;

public class IntegralExpression extends AbstractExpression {
	private double lowerLimit, upperLimit;
	private AbstractExpression expArg;
	private String intVar;
	private int partitions;
	
	IntegralExpression(AbstractExpression expArg, double lowerLimit, double upperLimit, int partitions, String intVar) {
		this.expArg = expArg;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.intVar = intVar;
		this.partitions = partitions;
	}
	
	/* deprecated - simpson's rule
	public double evaluate(Map<String,Double> varList) {
		//disregard variables that matches the name of intVar
		double partitionWidth = (upperLimit - lowerLimit) / partitions;
		double[] partitionHeights = expArg.intervalEvaluation(varList, lowerLimit, upperLimit, partitions, intVar);
		double integralSum = 0;
		
		//calculating trapezoidal areas
		for (int i = 0; i < partitions; i++) {
			integralSum += ((partitionHeights[i] + partitionHeights[i+1]) / 2) * partitionWidth;
		}
		return integralSum;
	} */
	
	public double evaluate(Map<String,Double> varList) {
		//disregard variables that matches the name of intVar
		Map<String,Double> newList = new HashMap<String,Double>(varList);
		double partitionWidth = (upperLimit - lowerLimit) / partitions;
		double[] partitionHeights = expArg.intervalEvaluation(newList, lowerLimit, upperLimit, partitions, intVar);
		double integralSum = 0;
		
		//calculating areas using simpson's rule
		for (int i = 0; i < partitions; i++) {
			newList.put(intVar, (lowerLimit + (i * partitionWidth) + (partitionWidth / 2)));
			double simpsonProduct = partitionHeights[i] + partitionHeights[i+1] + 4 * expArg.evaluate(newList);
			integralSum += (partitionWidth / 6) * simpsonProduct;
		}
		
		return integralSum;
	}
	
	public String toString() {
		return "INTEGRAL_" + lowerLimit + "^" + upperLimit + "(" + expArg.toString() + " d" + intVar + ")";
	}
}