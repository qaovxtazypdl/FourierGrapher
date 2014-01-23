import java.util.*;

// Static class containing functions to evaluate Fourier series.
public class FourierSeries {

	/*
	 * fourierCosSpectrum
	 * 
	 * expr - Function to determine the cosine spectrum for.
	 * evalInterval - interval on the real X axis, [-EvalInt, EvalInt] to evaluate the Fourier series on.
	 * degree - the degree of the Fourier cosine spectrum to compute up to
	 * varList - mapping of variables to values to replace during evaluation.
	 * 
	 * PRE: expr is a function of x.
	 * POST: Returns the Fourier cosine coefficients for expr on the given interval, with degree 1 coefficient in index 0
	 */
	public static double[] fourierCosSpectrum(AbstractExpression expr, double evalInterval, int integralInterval, int degree, Map<String,Double> varList) {
		AbstractExpression lowerLimit = new Number(-evalInterval);
		AbstractExpression upperLimit = new Number(evalInterval);
		AbstractExpression a_n_basis = new UnaryFn(new BinaryFn(new Number(Math.PI / evalInterval), new BinaryFn(new Variable("n"), new Variable("x"), Operator.MULT), Operator.MULT), Operator.COS);
		AbstractExpression a_n_integral = new Integral(new BinaryFn(expr, a_n_basis, Operator.MULT), lowerLimit, upperLimit, integralInterval, "x");
		AbstractExpression a_n = new BinaryFn(new Number(1 / evalInterval), a_n_integral, Operator.MULT);
		return a_n.intervalEvaluation(varList, 1, degree, degree - 1, "n");
	}
	
	/*
	 * fourierSinSpectrum
	 * 
	 * expr - Function to determine the sine spectrum for.
	 * evalInterval - interval on the real X axis, [-EvalInt, EvalInt] to evaluate the Fourier series on.
	 * degree - the degree of the Fourier sine spectrum to compute up to
	 * varList - mapping of variables to values to replace during evaluation.
	 * 
	 * PRE: expr is a function of x.
	 * POST: Returns the Fourier cosine coefficients for expr on the given interval, with degree 1 coefficient in index 0
	 */
	public static double[] fourierSinSpectrum(AbstractExpression expr, double evalInterval, int integralInterval, int degree, Map<String,Double> varList) {
		AbstractExpression lowerLimit = new Number(-evalInterval);
		AbstractExpression upperLimit = new Number(evalInterval);
		AbstractExpression b_n_basis = new UnaryFn(new BinaryFn(new Number(Math.PI / evalInterval), new BinaryFn(new Variable("n"), new Variable("x"), Operator.MULT), Operator.MULT), Operator.SIN);
		AbstractExpression b_n_integral = new Integral(new BinaryFn(expr, b_n_basis, Operator.MULT), lowerLimit, upperLimit, integralInterval, "x");
		AbstractExpression b_n = new BinaryFn(new Number(1 / evalInterval), b_n_integral, Operator.MULT);
		return b_n.intervalEvaluation(varList, 1, degree, degree - 1, "n");
	}
	
	/*
	 * fourierConstant
	 * 
	 * expr - Function to determine the constant for.
	 * evalInterval - interval on the real X axis, [-EvalInt, EvalInt] to evaluate the Fourier series on.
	 * varList - mapping of variables to values to replace during evaluation.
	 * 
	 * PRE: expr is a function of x.
	 * POST: Returns the Fourier constant (degree 0) for the function on the given interval.
	 */
	public static double fourierConstant(AbstractExpression expr, double evalInterval, int integralInterval, Map<String,Double> varList) {	
		AbstractExpression lowerLimit = new Number(-evalInterval);
		AbstractExpression upperLimit = new Number(evalInterval);
		AbstractExpression a_0_integral = new Integral(expr, lowerLimit, upperLimit, integralInterval, "x");
		AbstractExpression a_0 = new BinaryFn(new Number(1 / (2*evalInterval)), a_0_integral, Operator.MULT);
		return a_0.evaluate(varList);
	}
	
	/*
	 * fourierSeriesFromSpectrum
	 * 
	 * constant - the constant term of the Fourier series
	 * cosSpectrum - the cosine spectrum array, with degree 1 coefficient in index 0
	 * sinSpectrum - the sine spectrum array, with degree 1 coefficient in index 0
	 * evalInterval - interval on the real X axis, [-EvalInt, EvalInt] to evaluate the Fourier series on.
	 * degree - the degree that the spectrums were calculated for.
	 * 
	 * PRE: expr is a function of x.
	 * POST: Computes the Fourier series expression from the cosine and sine spectrums and returns the expression equal to said series.
	 */
	public static AbstractExpression fourierSeriesFromSpectrum(double constant, double[] cosSpectrum, double[] sinSpectrum, double evalInterval, int degree) {
		AbstractExpression fourierPartialSeries = new Number(constant);
		for(int i = 0; i < degree; i++) {
			AbstractExpression cosCoeff = new Number(cosSpectrum[i]);
			AbstractExpression cosBasis = new UnaryFn(new BinaryFn(new Number(Math.PI / evalInterval), new BinaryFn(new Number(i+1), new Variable("x"), Operator.MULT), Operator.MULT), Operator.COS);
			AbstractExpression cosTerm = new BinaryFn(cosCoeff, cosBasis, Operator.MULT);
			
			AbstractExpression sinCoeff = new Number(sinSpectrum[i]);
			AbstractExpression sinBasis = new UnaryFn(new BinaryFn(new Number(Math.PI / evalInterval), new BinaryFn(new Number(i+1), new Variable("x"), Operator.MULT), Operator.MULT), Operator.SIN);
			AbstractExpression sinTerm = new BinaryFn(sinCoeff, sinBasis, Operator.MULT);
			
			AbstractExpression fourierTerm = new BinaryFn(cosTerm, sinTerm, Operator.ADD);
			fourierPartialSeries = new BinaryFn(fourierPartialSeries, fourierTerm, Operator.ADD);
		}
		return fourierPartialSeries;
	}
	
	/*
	 * fourierSeries
	 * 
	 * expr - Function to determine the constant for.
	 * evalInterval - interval on the real X axis, [-EvalInt, EvalInt] to evaluate the Fourier series on.
	 * degree - the degree of the Fourier series to compute up to.
	 * varList - mapping of variables to values to replace during evaluation.
	 * 
	 * PRE: expr is a function of x.
	 * POST: Computes and returns the Fourier series expression for given function.
	 */
	public static AbstractExpression fourierSeries(AbstractExpression expr, double evalInterval, int integralInterval, int degree,  Map<String,Double> varList) {
		return fourierSeriesFromSpectrum(fourierConstant(expr, evalInterval, integralInterval, varList),
				fourierCosSpectrum(expr, evalInterval, integralInterval, degree, varList),
				fourierSinSpectrum(expr, evalInterval, integralInterval, degree, varList), evalInterval, degree);
	}
	
	/*
	 * fourierSeriesAndPrint
	 * 
	 * expr - Function to determine the constant for.
	 * evalInterval - interval on the real X axis, [-EvalInt, EvalInt] to evaluate the Fourier series on.
	 * degree - the degree of the Fourier series to compute up to.
	 * varList - mapping of variables to values to replace during evaluation.
	 * 
	 * PRE: expr is a function of x.
	 * POST: Computes and returns the Fourier series expression for given function. Prints the series.
	 */
	public static AbstractExpression fourierSeriesAndPrint(AbstractExpression expr, double evalInterval, int integralInterval, int degree,  Map<String,Double> varList) {
		double constant = fourierConstant(expr, evalInterval, integralInterval, varList);
		double[] cosSpec = fourierCosSpectrum(expr, evalInterval, integralInterval, degree, varList);
		double[] sinSpec = fourierSinSpectrum(expr, evalInterval, integralInterval, degree, varList);
		String degreeLength = "" + (int)(Math.log10(degree) + 1);
		
		System.out.println("The Fourier series for the function given up to degree n=" + degree + " is:");
		System.out.printf("%.10f\n", constant);
		String positive;
		double spectrumValue = 0;
		
		for (int i = 0; i < cosSpec.length; i++) {
			spectrumValue = cosSpec[i];
			positive = spectrumValue >= 0? "+" : "";
			if (AbstractExpression.doubleEquals(spectrumValue, 0)) {
				if (spectrumValue <= 0) {
					spectrumValue *= -1;
				}
				positive = " ";
			}
			
			System.out.printf("+ [" + positive + "%.10f * cos((%-" + degreeLength + "d * PI * x) / " + evalInterval + ")]\n", spectrumValue, i + 1); 
		}
		for (int i = 0; i < sinSpec.length; i++) {
			spectrumValue = sinSpec[i];
			positive = spectrumValue >= 0? "+" : "";
			if (AbstractExpression.doubleEquals(spectrumValue, 0)) {
				if (spectrumValue <= 0) {
					spectrumValue *= -1;
				}
				positive = " ";
			}
			System.out.printf("+ [" + positive + "%.10f * sin((%-" + degreeLength + "d * PI * x) / " + evalInterval + ")]\n", spectrumValue, i + 1);
		}
		System.out.println("=============================================================");
		return fourierSeriesFromSpectrum(constant, cosSpec, sinSpec, evalInterval, degree);
	}
}