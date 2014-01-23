import java.util.*;

class Fourier {
	// Everything here is temporary and for demo/testing purposes for now.
	public static void main(String[] args) {
		Graphing graph = new Graphing(700, 700, -12, 12, -12, 12, 5000);
		
		//AbstractExpression b = new UnaryFn(new Variable("x"), "");
		//AbstractExpression b = new UnaryFn(new UnaryFn(new Variable("x"), Operator.FLOOR), Operator.HEAVI);
		AbstractExpression b = new BinaryFn(new Variable("x"), new Number(0.34), Operator.POW);
		
		Map<String,Double> varList = new HashMap<String,Double>(5);
		varList.put("PI", Math.PI);
		varList.put("E", Math.E);

		

		graph.addGraph(b);
		AbstractExpression b_f = FourierSeries.fourierSeries(b, 6, 10000, 25, varList);
		graph.addGraph(b_f);
	}
}