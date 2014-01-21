import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Graphing extends JComponent implements ActionListener {
	public static final int MAX_VARS = 5;

	private JFrame frame = new JFrame("Fourier Series Expansion Graph View."); // the main window for our application
	private JTextField messageField = new JTextField(20); // a single line display
	private JLabel messageLabel = new JLabel("y = x"); // a non-editable display
	private String s = "x";
	
	Map<String,Double> varList;
	AbstractExpression[] exprs;
	private int minX, maxX;
	private int minY, maxY;
	private int windowWidth, windowHeight;
	private int intervals;
	private int exprCount;
	
	Graphing(int windowWidth, int windowHeight, int minX, int maxX, int minY, int maxY, int intervals) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.intervals = intervals;
		
		this.varList = new HashMap<String,Double>(MAX_VARS);
		varList.put("PI", Math.PI);
		varList.put("E", Math.E);
		
		this.exprs = new AbstractExpression[10];
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.exprCount = 0;
		
		frame.getContentPane().setBackground(Color.white);
		// Top
		JPanel northPanel = new JPanel();
		northPanel.add(new JLabel("Enter the function to graph:"));
		northPanel.add(messageField);
		messageField.addActionListener(this);
		frame.getContentPane().add(northPanel, "North");
		
		// Bottom
		JPanel southPanel = new JPanel();
		southPanel.add(messageLabel);
		frame.getContentPane().add(southPanel, "South");
	
		// Centre
		frame.getContentPane().add(this);

		// change frame size, disallow the user from changing size & make it visible
		frame.setSize(windowWidth,windowHeight);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/** Deprecated due to inefficiency
	public AbstractExpression fourierSeries(AbstractExpression expr, double evalInterval, int degree) {
		//a_0
		AbstractExpression intervalFactor = new BinaryExpression(new NumericalExpression(1), new NumericalExpression(evalInterval), "/");
		AbstractExpression a_0_factor = new BinaryExpression(new NumericalExpression(0.5), intervalFactor, "*");
		AbstractExpression a_0_integral = new IntegralExpression(expr, -evalInterval, evalInterval, 100, "x");
		AbstractExpression a_0 = new BinaryExpression(a_0_factor, a_0_integral, "*");
		
		//expression for the a_n's
		AbstractExpression a_n_basis = new UnaryExpression(new BinaryExpression(new Variable("n"), new Variable("x"), "*"), "COS");
		AbstractExpression a_n_integral = new IntegralExpression(new BinaryExpression(expr, a_n_basis, "*"), -evalInterval, evalInterval, 100, "x");
		AbstractExpression a_n = new BinaryExpression(a_n_integral, a_n_basis, "*");
		
		//expression for the b_n's
		AbstractExpression b_n_basis = new UnaryExpression(new BinaryExpression(new Variable("n"), new Variable("x"), "*"), "SIN");
		AbstractExpression b_n_integral = new IntegralExpression(new BinaryExpression(expr, b_n_basis, "*"), -evalInterval, evalInterval, 100, "x");
		AbstractExpression b_n = new BinaryExpression(b_n_integral, b_n_basis, "*");
		
		//expression for the unscaled Fourier partial series.
		AbstractExpression fourier_terms = new SummationExpression(new BinaryExpression(a_n, b_n, "+"), 1, degree, "n");
		
		//expression for the scaled Fourier partial series.
		AbstractExpression fourier_partial = new BinaryExpression(a_0, new BinaryExpression(intervalFactor, fourier_terms, "*"), "+");
		
		return fourier_partial;
	}**/
	
	public double[] fourierCosSpectrum(AbstractExpression expr, double evalInterval, int degree, Map<String,Double> varList) {
		AbstractExpression a_n_basis = new UnaryExpression(new BinaryExpression(new NumericalExpression(Math.PI / evalInterval), new BinaryExpression(new Variable("n"), new Variable("x"), "*"), "*"), "COS");
		AbstractExpression a_n_integral = new IntegralExpression(new BinaryExpression(expr, a_n_basis, "*"), -evalInterval, evalInterval, 100, "x");
		AbstractExpression a_n = new BinaryExpression(new NumericalExpression(1 / evalInterval), a_n_integral, "*");
		return a_n.intervalEvaluation(varList, 1, degree, degree - 1, "n");
	}
	
	public double[] fourierSinSpectrum(AbstractExpression expr, double evalInterval, int degree, Map<String,Double> varList) {
		AbstractExpression b_n_basis = new UnaryExpression(new BinaryExpression(new NumericalExpression(Math.PI / evalInterval), new BinaryExpression(new Variable("n"), new Variable("x"), "*"), "*"), "SIN");
		AbstractExpression b_n_integral = new IntegralExpression(new BinaryExpression(expr, b_n_basis, "*"), -evalInterval, evalInterval, 100, "x");
		AbstractExpression b_n = new BinaryExpression(new NumericalExpression(1 / evalInterval), b_n_integral, "*");
		return b_n.intervalEvaluation(varList, 1, degree, degree - 1, "n");
	}
	
	public double fourierConstant(AbstractExpression expr, double evalInterval, Map<String,Double> varList) {		
		AbstractExpression a_0_integral = new IntegralExpression(expr, -evalInterval, evalInterval, 100, "x");
		AbstractExpression a_0 = new BinaryExpression(new NumericalExpression(1 / (2*evalInterval)), a_0_integral, "*");
		return a_0.evaluate(varList);
	}
	
	public AbstractExpression fourierSeriesFromSpectrum(double constant, double[] cosSpectrum, double[] sinSpectrum, double evalInterval, int degree) {
		AbstractExpression fourierPartialSeries = new NumericalExpression(constant);
		for(int i = 0; i < degree; i++) {
			AbstractExpression cosCoeff = new NumericalExpression(cosSpectrum[i]);
			AbstractExpression cosBasis = new UnaryExpression(new BinaryExpression(new NumericalExpression(Math.PI / evalInterval), new BinaryExpression(new NumericalExpression(i+1), new Variable("x"), "*"), "*"), "COS");
			AbstractExpression cosTerm = new BinaryExpression(cosCoeff, cosBasis, "*");
			
			AbstractExpression sinCoeff = new NumericalExpression(sinSpectrum[i]);
			AbstractExpression sinBasis = new UnaryExpression(new BinaryExpression(new NumericalExpression(Math.PI / evalInterval), new BinaryExpression(new NumericalExpression(i+1), new Variable("x"), "*"), "*"), "SIN");
			AbstractExpression sinTerm = new BinaryExpression(sinCoeff, sinBasis, "*");
			
			AbstractExpression fourierTerm = new BinaryExpression(cosTerm, sinTerm, "+");
			fourierPartialSeries = new BinaryExpression(fourierPartialSeries, fourierTerm, "+");
		}
		return fourierPartialSeries;
	}
	
	public AbstractExpression fourierSeries(AbstractExpression expr, double evalInterval, int degree,  Map<String,Double> varList) {
		return fourierSeriesFromSpectrum(fourierConstant(expr, evalInterval, varList),
				fourierCosSpectrum(expr, evalInterval, degree, varList),
				fourierSinSpectrum(expr, evalInterval, degree, varList), evalInterval, degree);
	}
	
	public void addGraph(AbstractExpression expr) {
		exprs[exprCount++] = expr;
		repaint();
	}
	
	public void clearGraphs() {
		exprCount = 0;
		repaint();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// the user pressed enter in the message box
		if (e.getSource() == messageField)
		{
			// read entered text
			s = messageField.getText();
			//parse s into an AbstractExpression, set messageLabel text to toString() of the expr.
			//expr = parse(s)
			// clear the field
			messageField.setText("");
			// place message at bottom of frame
			messageLabel.setText("f(x) = " + s);
			// place message in list of messages on left of screen
			repaint();
		}
	}
	
     public void paint(Graphics g)
     {
    	 double scaleX = windowWidth / (maxX-minX);
    	 double scaleY = windowHeight / (maxY-minY);
    	 
    	 ///draw in the axis and labels. Bunch of math to scale correctly.
    	 g.setColor(Color.black);
    	 g.drawLine((int)((-1*minX)*scaleX), 0, (int)((-1*minX)*scaleX),700); //y axis
    	 g.drawLine(0,(int)(maxY * scaleY), 700, (int)(maxY * scaleY)); //x axis
    	 
    	 for (int i = minX; i<=maxX; i++) {
    		 g.drawLine((int)scaleX*(i-minX), (int)(maxY * scaleY) + 6,(int)scaleX*(i-minX), (int)(maxY * scaleY) - 6);
    	 }
    	 for (int i = minY; i<=maxY; i++) {
    		 g.drawLine((int)((-1*minX)*scaleX)-6, (int)((maxY - i) * scaleY), (int)((-1*minX)*scaleX)+6, (int)((maxY - i) * scaleY));
    	 }
    	 
    	 ///Graph the function by connecting plotted points by lines.
    	 for(int graphIndex = 0; graphIndex < exprCount; graphIndex++) {
    		 double[] intervalHeights = exprs[graphIndex].intervalEvaluation(varList, minX, maxX, intervals, "x");
    		 for(int i = 0; i < intervals; i++)
    		 {
    			 double intervalWidth = (maxX - minX) / (double)intervals;
    			 double y1 = intervalHeights[i];
    			 double y2 = intervalHeights[i + 1];
    			 double x1 = minX + (intervalWidth * i);
    			 double x2 = minX + (intervalWidth * (i + 1));
    		 
    			 g.drawLine((int) (scaleX * (x1 - minX)), (int)((maxY - y1) * scaleY),
    					 (int) (scaleX * (x2 - minX)), (int)((maxY - y2) * scaleY));
    		 }
    	 }
	
     }
}