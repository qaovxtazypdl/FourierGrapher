import java.util.Map;

// Class representing summation notation expressions
public class Summation extends AbstractExpression {
	// Bounds of the summation
	private AbstractExpression lowerLimit, upperLimit;
	
	// Expression to sum
	private AbstractExpression expArg;
	
	// Variable to sum w.r.t.
	private String intVar;
	
	// Constructor
	Summation(AbstractExpression expArg, AbstractExpression lowerLimit, AbstractExpression upperLimit, String intVar) {
		this.expArg = expArg;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.intVar = intVar;
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractExpression#evaluate(java.util.Map)
	 */
	public double evaluate(Map<String,Double> varList) {
		int lower = (int) lowerLimit.evaluate(varList);
		int upper = (int) upperLimit.evaluate(varList);
		int partitions = upper - lower;
		double[] partitionHeights = expArg.intervalEvaluation(varList, lower, upper, partitions, intVar);
		double sum = 0;
		
		for (int i = 0; i <= partitions; i++) {
			sum += partitionHeights[i];
		}
		return sum;
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractExpression#toString()
	 */
	public String toString() {
		return "SIGMA{" + intVar + "}_" + lowerLimit + "^" + upperLimit + "(" + expArg.toString() + ")";
	}
}