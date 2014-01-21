import java.util.Map;

public class SummationExpression extends AbstractExpression {
	private int lowerLimit, upperLimit;
	private AbstractExpression expArg;
	private String intVar;
	
	SummationExpression(AbstractExpression expArg, int lowerLimit, int upperLimit, String intVar) {
		this.expArg = expArg;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.intVar = intVar;
	}
	
	public double evaluate(Map<String,Double> varList) {
		//disregard variables that matches the name of intVar
		int partitions = upperLimit - lowerLimit;
		double[] partitionHeights = expArg.intervalEvaluation(varList, lowerLimit, upperLimit, partitions, intVar);
		double sum = 0;
		
		//calculating trapezoidal areas
		for (int i = 0; i <= partitions; i++) {
			sum += partitionHeights[i];
		}
		return sum;
	}
	
	public String toString() {
		return "SIGMA{" + intVar + "}_" + lowerLimit + "^" + upperLimit + "(" + expArg.toString() + ")";
	}
}