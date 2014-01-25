import java.util.*;

// Static class containing functions to format an input string and use shunting yard algorithm to parse into
// an AbstractExpression.

public class Parsing {
	
	/*
	 * tokenize
	 * 
	 * expression - the string to tokenize into a list of strings.
	 * 
	 * PRE: function is a valid infix representation of an expression.
	 * POST: returns a list of tokens of the string.
	 */
	private static String[] tokenize(String expression) {
		// tokens are all the keys in operator.operatormap, brackets, numbers, variables and binary operators.
		// tokenize everything with spaces. However, we will need preliminary formatting to put in
		// spaces before and after brackets and binary ops to ensure this succeeds.
		
		// spacing out the string...
		char[] spacers = {'^', '*', '+', '-', '/', '(', ')', '{', '}'};
		for(int i = 0; i < spacers.length; i++) {
			expression = expression.replace("" + spacers[i], " " + spacers[i] + " ");
		}
		return expression.split(" ");
	}
	
	/*
	 * toExpression
	 * 
	 * expression - the string to convert to an AbstractExpression
	 * 
	 * PRE: function is a valid infix representation of an expression.
	 * POST: Returns the AbstractExpression representing the function given.
	 */
	public static AbstractExpression toExpression(String expression) {
		// Implementing the shunting yard algorithm, modified to accommodate unary and trinary operators.
		Stack<String> inputStack = new Stack<String>();
		Stack<Operator> opStack = new Stack<Operator>(); 
		Stack<AbstractExpression> expressionStack = new Stack<AbstractExpression>(); 
		
		// tokenize and push everything into input stack.
		String[] tokens = tokenize(expression);
		for (int i = tokens.length - 1; i >= 0; i--) {
			if (!tokens[i].isEmpty()) {
				inputStack.push(tokens[i].trim());
			}
		}
		
		String token = "";
		while (!inputStack.empty()) {
			token = inputStack.pop();
			if (Operator.isOp(token)) {
				Operator currentOp = Operator.getOp(token);
				if (currentOp.arity == 4 || (currentOp.arity == 1 && currentOp != Operator.LPAREN)) {
					inputStack.pop();
				}
				int curPrecedence = currentOp.precedence;
				if (opStack.empty()) {
					// if the stack is empty, just push it on.
					opStack.push(currentOp);
				} else {
					if(currentOp.precedence > 1) {
						while (!opStack.empty() && (opStack.peek().precedence >=  curPrecedence)) {
							//pop ops off the stack and handle.
							handleOperator(expressionStack, opStack.pop());
						}
						opStack.push(currentOp);
					} else if (currentOp.precedence == 0) {
						// pop until next precedence 1 op
						while (!opStack.empty()) {
							//pop ops off the stack until unary is found.
							Operator op = opStack.pop();
							handleOperator(expressionStack, op);
							// we are done when we reach open bracket, unary fn, or integral/summation
							if(op.arity == 1 || op.arity == 4) {
								break;
							}
						}
					} else {
						// precedence was 1. directly push.
						opStack.push(currentOp);
					}
				}
			} else {
				// if it is not a operator.
				// try to parse as double, and store as variable into expression stack if failed.

				AbstractExpression expr;
				try {
					expr = new Number(Double.parseDouble(token));
				} catch (NumberFormatException ex) {
					expr = new Variable(token);
				}
				expressionStack.push(expr);
			}
		}

		while (!opStack.empty()) {
			// might be still operators in stack. pop all these off and handle.
			handleOperator(expressionStack, opStack.pop());
		}
		
		//after op is empty and in is empty, pop off the top of expression stack and return.
		AbstractExpression returnExp = expressionStack.pop();
		return returnExp;
	}
	
	private static void handleOperator(Stack<AbstractExpression> expressionStack, Operator op) {
		//handle unary, binary, quaternary operators, as well as brackets.
		Map<String,Double> varList = new HashMap<String,Double>(5);
		varList.put("PI", Math.PI);
		varList.put("E", Math.E);

		if (op.arity == 1) {
			AbstractExpression unaryExp = new UnaryExpr(expressionStack.pop(), op);
			expressionStack.push(unaryExp);
		} else if (op.arity == 2) {
			AbstractExpression rightExpr = expressionStack.pop();
			AbstractExpression leftExpr = expressionStack.pop();
			AbstractExpression unaryExp = new BinaryExpr(leftExpr, rightExpr, op);
			expressionStack.push(unaryExp);
		} else if (op.arity == 4) {
			AbstractExpression varName = expressionStack.pop();
			AbstractExpression expr = expressionStack.pop();
			AbstractExpression upperBound = expressionStack.pop();
			AbstractExpression lowerBound = expressionStack.pop();

			if (op == Operator.INT) {
				AbstractExpression intExpr = new Integral(expr, lowerBound, upperBound, 30, varName.toString());//TODO:integral cosntant
				expressionStack.push(intExpr);
			} else if (op == Operator.SUM) {
				AbstractExpression sumExpr = new Summation(expr, lowerBound, upperBound, varName.toString());
				expressionStack.push(sumExpr);
			}
		} else if (op.arity == 0) {
			// do nothing
		}
	}
}