import java.util.*;

// Class representing the binary functions.
public class BinaryFn extends AbstractExpression {
	// Supports operators + - * / ^
	private String operator;
	/* Expressions in the binary function, left and right of the operator. */
	private AbstractExpression expRight;
	private AbstractExpression expLeft;
	
	// Constructor
	BinaryFn(AbstractExpression expLeft, AbstractExpression expRight, String operator) {
		this.expRight = expRight;
		this.expLeft = expLeft;
		this.operator = operator;
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractExpression#evaluate(java.util.Map)
	 */
	public double evaluate(Map<String,Double> varList) {
		double result = 0;
		double leftResult = expLeft.evaluate(varList);
		double rightResult = expRight.evaluate(varList);
		
		if (operator.equalsIgnoreCase("+")) {
			result = leftResult + rightResult;
		} else if (operator.equalsIgnoreCase("-")) {
			result = leftResult - rightResult;
		} else if (operator.equalsIgnoreCase("*")) {
			result = leftResult * rightResult;
		} else if (operator.equalsIgnoreCase("/")) {
			result = leftResult / rightResult;
		} else if (operator.equalsIgnoreCase("^")) {
			result = Math.pow(leftResult, rightResult);
		}
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractExpression#toString()
	 */
	public String toString() {
		return "(" + expLeft.toString() + operator + expRight.toString() + ")";
	}
}