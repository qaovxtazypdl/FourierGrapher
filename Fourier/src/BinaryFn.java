import java.util.*;

// Class representing the binary functions.
public class BinaryFn extends AbstractExpression {
	// Supports operators + - * / ^
	private Operator op;
	/* Expressions in the binary function, left and right of the operator. */
	private AbstractExpression expRight;
	private AbstractExpression expLeft;
	
	// Constructor
	BinaryFn(AbstractExpression expLeft, AbstractExpression expRight, Operator op) {
		this.expRight = expRight;
		this.expLeft = expLeft;
		this.op = op;
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractExpression#evaluate(java.util.Map)
	 */
	public double evaluate(Map<String,Double> varList) {
		double result = 0;
		double leftResult = expLeft.evaluate(varList);
		double rightResult = expRight.evaluate(varList);
		
		switch (op) {
		case ADD:
			result = leftResult + rightResult;
			break;              
		case SUB:
			result = leftResult - rightResult;
            break;                 
		case MULT: 
			result = leftResult * rightResult;
			break;		
		case DIV:
			result = leftResult / rightResult;
			break;
		case POW:
			result = Math.pow(leftResult, rightResult);
			break;     
		default:
			System.out.println("Invalid binary operator.");
			break;
		}
		
		return Double.isNaN(result)? 0 : result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractExpression#toString()
	 */
	public String toString() {
		return "(" + expLeft.toString() + op.toString() + expRight.toString() + ")";
	}
}