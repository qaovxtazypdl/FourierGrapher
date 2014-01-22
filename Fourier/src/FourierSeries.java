import java.util.*;

// Static class containing functions to evaluate Fourier series.
public class FourierSeries {

	/*
	 * fourierCosSpectrum
	 * 
	 * expr - Function to determine the cosine spectrum for.
	 * evalInterval - interval on the real X axis to evaluate the Fourier series on.
	 * degree - the degree of the Fourier cosine spectrum to compute up to
	 * varList - mapping of variables to values to replace during evaluation.
	 * 
	 * PRE: true
	 * POST: Returns the Fourier cosine coefficients for expr on the given interval, with degree 1 coefficient in index 0
	 */
	public static double[] fourierCosSpectrum(AbstractExpression expr, double evalInterval, int degree, Map<String,Double> varList) {
		AbstractExpression a_n_basis = new UnaryFn(new BinaryFn(new Number(Math.PI / evalInterval), new BinaryFn(new Variable("n"), new Variable("x"), "*"), "*"), "COS");
		AbstractExpression a_n_integral = new Integral(new BinaryFn(expr, a_n_basis, "*"), -evalInterval, evalInterval, 100, "x");
		AbstractExpression a_n = new BinaryFn(new Number(1 / evalInterval), a_n_integral, "*");
		return a_n.intervalEvaluation(varList, 1, degree, degree - 1, "n");
	}
	
	/*
	 * fourierSinSpectrum
	 * 
	 * expr - Function to determine the sine spectrum for.
	 * evalInterval - interval on the real X axis to evaluate the Fourier series on.
	 * degree - the degree of the Fourier sine spectrum to compute up to
	 * varList - mapping of variables to values to replace during evaluation.
	 * 
	 * PRE: true
	 * POST: Returns the Fourier cosine coefficients for expr on the given interval, with degree 1 coefficient in index 0
	 */
	public static double[] fourierSinSpectrum(AbstractExpression expr, double evalInterval, int degree, Map<String,Double> varList) {
		AbstractExpression b_n_basis = new UnaryFn(new BinaryFn(new Number(Math.PI / evalInterval), new BinaryFn(new Variable("n"), new Variable("x"), "*"), "*"), "SIN");
		AbstractExpression b_n_integral = new Integral(new BinaryFn(expr, b_n_basis, "*"), -evalInterval, evalInterval, 100, "x");
		AbstractExpression b_n = new BinaryFn(new Number(1 / evalInterval), b_n_integral, "*");
		return b_n.intervalEvaluation(varList, 1, degree, degree - 1, "n");
	}
	
	/*
	 * fourierConstant
	 * 
	 * expr - Function to determine the constant for.
	 * evalInterval - interval on the real X axis to evaluate the Fourier series on.
	 * varList - mapping of variables to values to replace during evaluation.
	 * 
	 * PRE: true
	 * POST: Returns the Fourier constant (degree 0) for the function on the given interval.
	 */
	public static double fourierConstant(AbstractExpression expr, double evalInterval, Map<String,Double> varList) {		
		AbstractExpression a_0_integral = new Integral(expr, -evalInterval, evalInterval, 100, "x");
		AbstractExpression a_0 = new BinaryFn(new Number(1 / (2*evalInterval)), a_0_integral, "*");
		return a_0.evaluate(varList);
	}
	
	/*
	 * fourierConstant
	 * 
	 * constant - the constant term of the Fourier series
	 * cosSpectrum - the cosine spectrum array, with degree 1 coefficient in index 0
	 * sinSpectrum - the sine spectrum array, with degree 1 coefficient in index 0
	 * evalInterval - interval on the real X axis to evaluate the Fourier series on.
	 * degree - the degree that the spectrums were calculated for.
	 * 
	 * PRE: true
	 * POST: Computes the Fourier series expression from the cosine and sine spectrums and returns the expression equal to said series.
	 */
	public static AbstractExpression fourierSeriesFromSpectrum(double constant, double[] cosSpectrum, double[] sinSpectrum, double evalInterval, int degree) {
		AbstractExpression fourierPartialSeries = new Number(constant);
		for(int i = 0; i < degree; i++) {
			AbstractExpression cosCoeff = new Number(cosSpectrum[i]);
			AbstractExpression cosBasis = new UnaryFn(new BinaryFn(new Number(Math.PI / evalInterval), new BinaryFn(new Number(i+1), new Variable("x"), "*"), "*"), "COS");
			AbstractExpression cosTerm = new BinaryFn(cosCoeff, cosBasis, "*");
			
			AbstractExpression sinCoeff = new Number(sinSpectrum[i]);
			AbstractExpression sinBasis = new UnaryFn(new BinaryFn(new Number(Math.PI / evalInterval), new BinaryFn(new Number(i+1), new Variable("x"), "*"), "*"), "SIN");
			AbstractExpression sinTerm = new BinaryFn(sinCoeff, sinBasis, "*");
			
			AbstractExpression fourierTerm = new BinaryFn(cosTerm, sinTerm, "+");
			fourierPartialSeries = new BinaryFn(fourierPartialSeries, fourierTerm, "+");
		}
		return fourierPartialSeries;
	}
	
	/*
	 * fourierSeries
	 * 
	 * expr - Function to determine the constant for.
	 * evalInterval - interval on the real X axis to evaluate the Fourier series on.
	 * degree - the degree of the Fourier series to compute up to.
	 * varList - mapping of variables to values to replace during evaluation.
	 */
	public static AbstractExpression fourierSeries(AbstractExpression expr, double evalInterval, int degree,  Map<String,Double> varList) {
		return fourierSeriesFromSpectrum(fourierConstant(expr, evalInterval, varList),
				fourierCosSpectrum(expr, evalInterval, degree, varList),
				fourierSinSpectrum(expr, evalInterval, degree, varList), evalInterval, degree);
	}
}