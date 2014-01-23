import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

// The graphing class - handles graphing of the functions.
public class Graphing extends JComponent implements ActionListener {
	// Default generated UID.
	private static final long serialVersionUID = -4789458729262303669L;
	
	// Relationship between degrees and integral interval accuracy for this graph.
	private static final int INTERVALS_FACTOR = 100;
	// Half of the length of each interval marker on the graph.
	private static final int MARKER_LENGTH = 6;
	// Maximum variables our varList map can hold.
	public static final int MAX_VARS = 10;
	// the main window for our application
	private JFrame frame; 
	// a single line text box
	private JTextField messageField;
	// a text display at the bottom of the frame, and its message string.
	private JLabel messageLabel;
	private String messageString;
	
	// Holding constants for now.
	Map<String,Double> varList;
	
	// Array of expressions to graph.
	AbstractExpression[] exprs;
	// Number of functions in exprs
	private int exprCount;
	
	// Graph dimension and max coordinate properties.
	private int minX, maxX;
	private int minY, maxY;
	private int windowWidth, windowHeight;
	
	// Graph refine amount
	private int intervals;
	
	// Fourier degrees.
	private int fourierDegree;
	
	// Fourier evaluation interval.
	private double fourierInterval;
	
	// Constructor
	Graphing(int windowWidth, int windowHeight, int minX, int maxX, int minY, int maxY, int intervals) {
		//INTERVALS_FACTOR = 100; TODO: do I want static constant, or class constant?
		this.fourierDegree = 5;
		this.fourierInterval = Math.PI;
		
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.intervals = intervals;
		
		this.varList = new HashMap<String,Double>(MAX_VARS);
		varList.put("PI", Math.PI);
		varList.put("E", Math.E);
		
		this.exprs = new AbstractExpression[MAX_VARS];
		this.exprCount = 0;
		
		// init the swing window
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		frame = new JFrame("Fourier Series Expansion Graph View."); 
		messageField = new JTextField(20);
		messageString = "No function plotted.";
		messageLabel = new JLabel(messageString);
		frame.getContentPane().setBackground(Color.white);
		// North (text field)
		JPanel northPanel = new JPanel();
		northPanel.add(new JLabel("Enter the function (of x) to graph:"));
		northPanel.add(messageField);
		messageField.addActionListener(this);
		frame.getContentPane().add(northPanel, "North");
		
		// South(Message field)
		JPanel southPanel = new JPanel();
		southPanel.add(messageLabel);
		frame.getContentPane().add(southPanel, "South");
	
		// Centre (Graph)
		frame.getContentPane().add(this);

		// change frame size, disallow the user from changing size & make it visible
		frame.setSize(windowWidth,windowHeight);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/*
	 * addGraph
	 * 
	 * expr - the function to graph.
	 * 
	 * PRE: expr is a valid function of x.
	 * POST: Adds an expression to graph as a function of x.
	 */
	public void addGraph(AbstractExpression expr) {
		exprs[exprCount++] = expr;
		repaint();
	}
	
	/*
	 * clearGraphs
	 * 
	 * PRE: true
	 * POST: Removes all graphs from the screen.
	 */
	public void clearGraphs() {
		exprCount = 0;
		repaint();
	}
	
	/*
	 * clearGraphs
	 * 
	 * PRE: true
	 * POST: Sets the new Fourier degree to graph up to.
	 */
	public void setFourierDegree(int degree) {
		fourierDegree = degree;
	}
	
	/*
	 * setFourierInterval
	 * 
	 * PRE: true
	 * POST: Sets the new Fourier interval of the function.
	 */
	public void setFourierInterval(int interval) {
		fourierInterval = interval;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		// the user pressed enter in the message box
		if (e.getSource() == messageField)
		{
			// read entered text, parse it, graph it and it's fourier transform.
			messageString = messageField.getText().trim();
			System.out.println(messageString);
			
			// Parsed expression.
			AbstractExpression expr = Parsing.toExpression(messageString);
			AbstractExpression fourier_expr = FourierSeries.fourierSeriesAndPrint(expr, fourierInterval, INTERVALS_FACTOR * fourierDegree, fourierDegree, varList); 
			
			// clear the field
			messageField.setText("");
			
			// place expression at bottom of frame
			messageLabel.setText(messageString);
			
			// graph and redraw.
			clearGraphs();
			addGraph(expr);
			addGraph(fourier_expr);
			repaint();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
     public void paint(Graphics g)
     {
    	 double scaleX = windowWidth / (maxX-minX);
    	 double scaleY = windowHeight / (maxY-minY);
    	 
    	 ///draw in the axis and labels. Bunch of math to scale correctly.
    	 g.setColor(Color.black);
    	 g.drawLine((int)((-1*minX)*scaleX), 0, (int)((-1*minX)*scaleX), windowHeight); //y axis
    	 g.drawLine(0,(int)(maxY * scaleY), windowWidth, (int)(maxY * scaleY)); //x axis

    	 //draw in the markers, with length 12.
    	 for (int i = minX; i<=maxX; i++) {
    		 g.drawLine((int)scaleX*(i-minX), (int)(maxY * scaleY) + MARKER_LENGTH,(int)scaleX*(i-minX), (int)(maxY * scaleY) - MARKER_LENGTH);
    	 }
    	 for (int i = minY; i<=maxY; i++) {
    		 g.drawLine((int)((-1*minX)*scaleX)-MARKER_LENGTH, (int)((maxY - i) * scaleY), (int)((-1*minX)*scaleX)+MARKER_LENGTH, (int)((maxY - i) * scaleY));
    	 }
    	 
    	 ///Graph the function by connecting plotted points by lines.
    	 for(int graphIndex = 0; graphIndex < exprCount; graphIndex++) {
        	 g.setColor(graphIndex > 0? Color.blue : Color.black);
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