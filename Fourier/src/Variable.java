import java.util.*;

// Class representing a single variable.
public class Variable extends AbstractExpression{
	// Name of the variable
	private String varName;
	
	// Constructor
	Variable(String varName) {
		this.varName = varName;
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractExpression#evaluate(java.util.Map)
	 */
	public double evaluate(Map<String,Double> varList) {
		//System.out.println(varList);
		return varList.get(varName);
	}
	
	/*
	 * (non-Javadoc)
	 * @see AbstractExpression#toString()
	 */
	public String toString() {
		return varName;
	}
}


