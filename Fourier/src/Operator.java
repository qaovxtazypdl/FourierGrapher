import java.util.*;

public enum Operator {
    // Unary
    SIN(1, "SIN", 1), COS(1, "COS", 1), TAN(1, "TAN", 1), ASIN(1, "ASIN", 1), ACOS(1, "ACOS", 1),
    ATAN(1, "ATAN", 1), SINH(1, "SINH", 1), COSH(1, "COSH", 1), TANH(1, "TANH", 1), NEG(1, "NEG", 1),
    ABS(1, "ABS", 1), LOG(1, "LOG", 1), LOG10(1, "LOG", 1), SQRT(1, "SQRT", 1), FLOOR(1, "FLOOR", 1), CEIL(1, "CEIL", 1), HEAVI(1, "HEAVI", 1),
    LPAREN(1, "ID", 1), //LPAREN, open parenthesis, is implemented as the identity function f(x)=x.
    // Binary
    ADD(2, "+", 2), SUB(2, "-", 2), MULT(3, "*", 2), DIV(3, "/", 2), POW(4, "^", 2),
    // Quaternary
    INT(1, "INT", 4), SUM(1, "SUM", 4),
    // RPAREN closes off unary functions
    RPAREN(0, ")", 0),;

    
    // Mapping from string operator name to the Enum type.
	private static Map<String,Operator> operatorMap = new HashMap<String,Operator>();
	static {
		operatorMap.put("sin", SIN);
		operatorMap.put("cos", COS);
		operatorMap.put("tan", TAN);
		operatorMap.put("asin", ASIN);
		operatorMap.put("acos", ACOS);
		operatorMap.put("atan", ATAN);
		operatorMap.put("sinh", SINH);
		operatorMap.put("cosh", COSH);
		operatorMap.put("tanh", TANH);
		operatorMap.put("neg", NEG);
		operatorMap.put("abs", ABS);
		operatorMap.put("log", LOG10);
		operatorMap.put("ln", LOG);
		operatorMap.put("sqrt", SQRT);
		operatorMap.put("floor", FLOOR);
		operatorMap.put("ceil", CEIL);
		operatorMap.put("H", HEAVI);
		operatorMap.put("+", ADD);
		operatorMap.put("-", SUB);
		operatorMap.put("*", MULT);
		operatorMap.put("/", DIV);
		operatorMap.put("^", POW);
		operatorMap.put("INT", INT);
		operatorMap.put("SUM", SUM);
		operatorMap.put(")", RPAREN);
		operatorMap.put("(", LPAREN);
	}
	
    // operator precedence. Higher is higher prec.
	// 0: Closing bracket.
    // 1: Unary functions. SIN, COS... etc, open bracket(identity function, unary)
    // 2: add, sub
    // 3: mult, div
    // 4: expt
    public final int precedence;
    
    // string representation of the operator.
    public final String stringRep;
    
    // arity of the operator
    public final int arity;
    
    // Constructor
    Operator(int precedence, String stringRep, int arity) {
    	this.precedence = precedence;
    	this.stringRep = stringRep;
    	this.arity = arity;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
	public String toString() {
		return stringRep;
	}
	
	/*
	 * getOp
	 * 
	 * op - Operator name in string.
	 * 
	 * PRE: op is a valid operator.
	 * POST: returns the Enum type corresponding to the string operator given.
	 */
	public static Operator getOp(String op) {
		return operatorMap.get(op.toLowerCase());
	}
	
	/*
	 * isOp
	 * 
	 * op - Operator name in string.
	 * 
	 * PRE: true
	 * POST: returns true if op represents an Operator.
	 */
	public static boolean isOp(String op) {
		return operatorMap.containsKey(op.toLowerCase());
	}
}