import java.util.*;

public enum Operator {
    // Unary
    SIN(1, "SIN"), COS(1, "COS"), TAN(1, "TAN"), ASIN(1, "ASIN"), ACOS(1, "ACOS"),
    ATAN(1, "ATAN"), SINH(1, "SINH"), COSH(1, "COSH"), TANH(1, "TANH"),
    ABS(1, "ABS"), LOG(1, "LOG"), SQRT(1, "SQRT"), FLOOR(1, "FLOOR"), CEIL(1, "CEIL"), HEAVI(1, "HEAVI"),
    // Binary
    ADD(2, "+"), SUB(2, "-"), MULT(3, "*"), DIV(3, "/"), POW(4, "^"),
    // Trinary
    INT(1, "INT"), SUM(1, "SUM");
    
    // Mapping from string operator name to the Enum type.
	private static Map<String,Operator> operatorMap = new HashMap<String,Operator>();
	static {
		operatorMap.put("SIN", SIN);
		operatorMap.put("COS", COS);
		operatorMap.put("TAN", TAN);
		operatorMap.put("ASIN", ASIN);
		operatorMap.put("ACOS", ACOS);
		operatorMap.put("ATAN", ATAN);
		operatorMap.put("SINH", SINH);
		operatorMap.put("COSH", COSH);
		operatorMap.put("TANH", TANH);
		operatorMap.put("ABS", ABS);
		operatorMap.put("LOG", LOG);
		operatorMap.put("SQRT", SQRT);
		operatorMap.put("FLOOR", FLOOR);
		operatorMap.put("CEIL", CEIL);
		operatorMap.put("HEAVI", HEAVI);
		operatorMap.put("+", ADD);
		operatorMap.put("-", SUB);
		operatorMap.put("*", MULT);
		operatorMap.put("/", DIV);
		operatorMap.put("^", POW);
		operatorMap.put("INT", INT);
		operatorMap.put("SUM", SUM);
	}
	
    // operator precedence. Higher is higher prec.
    // 1: int, sum {} {} {} - 3 values in curly braces. 
    // 1: unaries - ()
    // 2: add, sub
    // 3: mult, div
    // 4: expt
    private int precedence;
    
    // string representation of the operator.
    private String stringRep;
    
    // Constructor
    Operator(int precedence, String stringRep) {
    	this.precedence = precedence;
    	this.stringRep = stringRep;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
	public String toString() {
		return stringRep;
	}
	
	/* 
	 * getPrecedence
	 * 
	 * PRE: true
	 * POST: returns the precedence value of the calling operator.
	 */
	public int getPrecedence() {
		return precedence;
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
		return operatorMap.get(op);
	}
}