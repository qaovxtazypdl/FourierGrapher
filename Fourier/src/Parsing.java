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
		// Implementing the shunting yard algorithm, modifying to accommodate unaries and trinaries.
		Stack<String> inputStack = new Stack<String>(); //<type>
		
		String[] tokens = tokenize(expression);
		for (int i = tokens.length - 1; i >= 0; i--) {
			if (!tokens[i].isEmpty()) {
				inputStack.push(tokens[i].trim());
			}
		}
		
		
		
		
		
		
		
		
		
		/// shunting yard implementation here...
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//Test TODO: remove this
		AbstractExpression b = new UnaryFn(new UnaryFn(new Variable("x"), Operator.SIN), Operator.ABS);
		//AbstractExpression b = new UnaryFn(new Variable("x"), Operator.COS);
		//AbstractExpression b = new BinaryFn(new Variable("x"), new Number(0.34), Operator.POW);
		return b;
	}
}