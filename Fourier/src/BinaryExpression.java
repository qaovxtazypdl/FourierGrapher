import java.util.*;

public class BinaryExpression extends AbstractExpression {
	//supports + - * / ^
	private String operator;
	private AbstractExpression expRight;
	private AbstractExpression expLeft;
	
	BinaryExpression(AbstractExpression expLeft, AbstractExpression expRight, String operator) {
		this.expRight = expRight;
		this.expLeft = expLeft;
		this.operator = operator;
	}
	
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
	
	public String toString() {
		return "(" + expLeft.toString() + operator + expRight.toString() + ")";
	}
}