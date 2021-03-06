import java.util.*;

// Abstract base class for the types of expressions.
public abstract class AbstractExpression {
	public static final double DIFFERENTIAL= 1E-12;
	
	/*
	 * evaluate
	 * 
	 * varList - mapping from variable names to values to substitute.
	 * 
	 * PRE: The caller expression is valid and evaluatable for the variables given in varList
	 * POST: Returns the value of the expression.
	 */
	public abstract double evaluate(Map<String,Double> varList);
	
	/* intervalEvaluation
	 * 
	 * varList - mapping from variable names to values to substitute.
	 * lowerBound, upperBound - bounds of evaluation.
	 * partitions - how many parts to evenly split the interval into.
	 * varName - the variable to variate on the interval.
	 * 
	 * PRE: none
	 * POST: returns an array of double representing the function evaluated at the beginning of each partition, as well
	 * 		 plus at the upper bound of the interval. 
	 */
	public double[] intervalEvaluation(Map<String,Double> varList, double lowerBound, double upperBound, int partitions, String varName) {
		Map<String,Double> newList = new HashMap<String,Double>(varList);
		partitions = (partitions < 1)? 1 : partitions;
		double[] results = new double[partitions + 1];
		double intervalWidth = (upperBound - lowerBound) / partitions;
		double value;
		for(int i = 0; i <= partitions; i++) {
			newList.put(varName, lowerBound + i * intervalWidth);
			value = evaluate(newList);
			results[i] = AbstractExpression.doubleEquals(value, 0)? 0 : value;
		}
		return results;
 	}
	
	/* doubleEquals
	 * 
	 * d1, d2 - the doubles to compare.
	 * 
	 * PRE: none
	 * POST: returns true if the two doubles are within a differential.
	 */
	public static boolean doubleEquals(double d1, double d2) {
		return Math.abs(d1-d2) <= DIFFERENTIAL;
 	}
	
	
	
	/* toString
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();
}