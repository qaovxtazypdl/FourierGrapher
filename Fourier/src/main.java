import java.util.*;

class Fourier {
	public static void main(String[] args) {
		Graphing graph = new Graphing(700, 700, -12, 12, -12, 12, 1000);
		
		//AbstractExpression a = new UnaryExpression(new Variable("x"), "H");
		AbstractExpression b = new BinaryExpression(new Variable("PI"), new Variable("x"), "^");
		
		Map<String,Double> varList = new HashMap<String,Double>(5);
		varList.put("PI", Math.PI);
		varList.put("E", Math.E);

		

		graph.addGraph(b);
		AbstractExpression b_f = graph.fourierSeries(b, Math.PI, 10, varList);
		graph.addGraph(b_f);
		System.out.println(b_f.toString());
		/*
		double[] coss = graph.fourierCosSpectrum(b, Math.PI, 10, varList);
		double[] sins = graph.fourierSinSpectrum(b, Math.PI, 10, varList);
		double cons = graph.fourierConstant(b, Math.PI, varList);
		for(int i = 0; i < 10; i++) {
			System.out.println(coss[i]);
		}
		for(int i = 0; i < 10; i++) {
			System.out.println(sins[i]);
		}
		System.out.println(cons);

		
		graph.addGraph(a);
		AbstractExpression a_f = graph.fourierSeries(a, Math.PI, 20, varList);
		graph.addGraph(a_f);
		double[] coss2 = graph.fourierCosSpectrum(a, 1, 20, varList);
		double[] sins2 = graph.fourierSinSpectrum(a, 1, 20, varList);
		double cons2 = graph.fourierConstant(a, 1, varList);
		for(int i = 0; i < 20; i++) {
			System.out.println(coss2[i]);
		}
		for(int i = 0; i < 20; i++) {
			System.out.println(sins2[i]);
		}
		System.out.println(cons2);
				*/
		
		
		


		//System.out.println(b_f.toString());
		//Scanner sc = new Scanner(System.in);
		//System.out.println(a.evaluate(arrayList));
		//System.out.println(a.evaluateSimpson(arrayList));
		
		//System.out.println(b.evaluate(arrayList));
		//parse here
		//sc.close();
	}
}