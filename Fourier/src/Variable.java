import java.util.*;

public class Variable extends AbstractExpression{
	private String varName;
	
	Variable(String varName) {
		this.varName = varName;
	}
	
	public double evaluate(Map<String,Double> varList) {
		//System.out.println(varList);
		return varList.get(varName);
	}
	
	public String toString() {
		return varName;
	}
}


