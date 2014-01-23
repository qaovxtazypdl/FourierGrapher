import java.util.*;

// Abstract base class for the types of expressions.
public abstract class AbstractExpression {
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
		double[] results = new double[partitions + 1];
		double intervalWidth = (upperBound - lowerBound) / partitions;
		for(int i = 0; i <= partitions; i++) {
			newList.put(varName, lowerBound + i * intervalWidth);
			results[i] = evaluate(newList);
		}
		return results;
 	}
	
	/* toString
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();
}