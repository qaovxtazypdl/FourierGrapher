import java.util.*;

// Class representing unary functions.
public class UnaryFn extends AbstractExpression {
	// supports SIN, COS, TAN, ASIN, ACOS, ATAN, SINH, COSH, TANH, ABS, LOG, SQRT, FLOOR, CEIL, H(Heaviside) 
	private Operator op;
	
	// Inner expression of the unary function.
	private AbstractExpression expArg;
	
	// Constructor
	UnaryFn(AbstractExpression expArg, Operator op) {
		this.expArg = expArg;
		this.op = op;
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractExpression#evaluate(java.util.Map)
	 */
	public double evaluate(Map<String,Double> varList) {
		double result = 0;
		double innerResult = expArg.evaluate(varList);
		
		switch (op) {
		case SIN:
			result = Math.sin(innerResult);
			break;
		case COS:
			result = Math.cos(innerResult);
			break;
		case TAN:
			result = Math.tan(innerResult);
			break;
		case ASIN:
			result = Math.asin(innerResult);
			break;
		case ACOS:
			result = Math.acos(innerResult);
			break;
		case ATAN:
			result = Math.atan(innerResult);
			break;
		case SINH:
			result = Math.sinh(innerResult);
			break;
		case COSH:
			result = Math.cosh(innerResult);
			break;
		case TANH:
			result = Math.tanh(innerResult);
			break;
		case ABS:
			result = Math.abs(innerResult);
			break;
		case LOG:
			result = Math.log(innerResult);
			break;
		case LOG10:
			result = Math.log10(innerResult);
			break;
		case SQRT:
			result = Math.sqrt(innerResult);
			break;
		case FLOOR:
			result = Math.floor(innerResult);
			break;
		case CEIL:
			result = Math.ceil(innerResult);
			break;
		case NEG:
			result = -1 * innerResult;
			break;
		case LPAREN: //the identity function.
			result = innerResult;
			break;
		case HEAVI:
			if (innerResult >= 0) {
				result = 1;
			} else {
				result = 0;
			}
			break;
		default:
			System.out.println("Invalid unary operator.");
			break;
		}
		
		
		return Double.isNaN(result)? 0 : result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractExpression#toString()
	 */
	public String toString() {
		if (op == Operator.LPAREN) {
			return expArg.toString();
		} else {
			return op.toString() + "(" + expArg.toString() + ")";
		}
		
	}
}