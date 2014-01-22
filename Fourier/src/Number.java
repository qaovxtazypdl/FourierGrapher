import java.util.*;

// The Numerical expression class. Wrapper for a double.
public class Number extends AbstractExpression {
	private double num;
	
	Number(double num) {
		this.num = num;
	}
	
	public double evaluate(Map<String,Double> varList) {
		return num;
	}
	
	public String toString() {
		return "" + num;
	}
}