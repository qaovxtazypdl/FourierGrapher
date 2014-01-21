import java.util.*;

public class UnaryExpression extends AbstractExpression {
	//supports SIN, COS, TAN, ASIN, ACOS, ATAN, SINH, COSH, TANH, ABS, LOG, SQRT, FLOOR, CEIL, H(Heaviside) 
	private String operator;
	private AbstractExpression expArg;
	
	UnaryExpression(AbstractExpression expArg, String operator) {
		this.expArg = expArg;
		this.operator = operator;
	}
	
	public double evaluate(Map<String,Double> varList) {
		double result = 0;
		double innerResult = expArg.evaluate(varList);
		
		if (operator.equalsIgnoreCase("SIN")) {
			result = Math.sin(innerResult);
		} else if (operator.equalsIgnoreCase("COS")) {
			result = Math.cos(innerResult);
		} else if (operator.equalsIgnoreCase("TAN")) {
			result = Math.tan(innerResult);
		} else if (operator.equalsIgnoreCase("ASIN")) {
			result = Math.asin(innerResult);
		} else if (operator.equalsIgnoreCase("ACOS")) {
			result = Math.acos(innerResult);
		} else if (operator.equalsIgnoreCase("ATAN")) {
			result = Math.atan(innerResult);
		} else if (operator.equalsIgnoreCase("SINH")) {
			result = Math.sinh(innerResult);
		} else if (operator.equalsIgnoreCase("COSH")) {
			result = Math.cosh(innerResult);
		} else if (operator.equalsIgnoreCase("TANH")) {
			result = Math.tanh(innerResult);
		} else if (operator.equalsIgnoreCase("ABS")) {
			result = Math.abs(innerResult);
		} else if (operator.equalsIgnoreCase("LOG")) {
			result = Math.log(innerResult);
		} else if (operator.equalsIgnoreCase("SQRT")) {
			result = Math.sqrt(innerResult);
		} else if (operator.equalsIgnoreCase("FLOOR")) {
			result = Math.floor(innerResult);
		} else if (operator.equalsIgnoreCase("CEIL")) {
			result = Math.ceil(innerResult);
		} else if (operator.equalsIgnoreCase("H")) {
			if (innerResult >= 0) {
				result = innerResult;
			} else {
				result = 0;
			}
			
		}
		return result;
	}
	
	public String toString() {
		return operator + "(" + expArg.toString() + ")";
	}
}