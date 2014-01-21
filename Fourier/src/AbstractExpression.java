import java.util.*;

public abstract class AbstractExpression {
	public abstract double evaluate(Map<String,Double> varList);
	public double[] intervalEvaluation(Map<String,Double> varList, double lowerBound, double upperBound, int partitions, String varName) {
		Map<String,Double> newList = new HashMap<String,Double>(varList);
		double[] result = new double[partitions + 1];
		double intervalWidth = (upperBound - lowerBound) / partitions;
		for(int i = 0; i <= partitions; i++) {
			newList.put(varName, lowerBound + i * intervalWidth);
			result[i] = evaluate(newList);
		}
		return result;
 	}
	
	public abstract String toString();
}