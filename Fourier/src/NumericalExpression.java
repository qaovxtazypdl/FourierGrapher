import java.util.*;

public class NumericalExpression extends AbstractExpression {
	private double num;
	
	NumericalExpression(double num) {
		this.num = num;
	}
	
	public double evaluate(Map<String,Double> varList) {
		return num;
	}
	
	public String toString() {
		return "" + num;
	}
}