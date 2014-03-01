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
	// Panels
	private JPanel northPanel, southPanel, leftPanel;
	// a single line text box
	private JTextField messageField;
	// a text display at the bottom of the frame, and its message string.
	private JLabel messageLabel, errorLabel;
	private String functionString;
	
	// settings fields.
	private JTextField graphDegree, graphInterval, graphXMin, graphXMax, graphYMin, graphYMax;
	private JPanel graphDegreePanel, graphIntervalPanel, graphXMinPanel, graphXMaxPanel, graphYMinPanel, graphYMaxPanel;
	
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
	Graphing(int intervals) {
		this.fourierDegree = 10;
		this.fourierInterval = Math.PI;
		
		this.minX = -10;
		this.maxX = 10;
		this.minY = -10;
		this.maxY = 10;
		this.intervals = intervals;
		
		this.varList = new HashMap<String,Double>(MAX_VARS);
		varList.put("PI", Math.PI);
		varList.put("E", Math.E);
		
		this.exprs = new AbstractExpression[MAX_VARS];
		this.exprCount = 0;
		
		// init the swing window
		this.windowWidth = 700;
		this.windowHeight = 700;
		
		frame = new JFrame("Fourier Series Expansion Graph View."); 
		messageField = new JTextField(20);
		functionString = "No function plotted.";
		messageLabel = new JLabel(functionString);
		errorLabel = new JLabel("No errors yet.");
		frame.getContentPane().setBackground(Color.white);
		// North (text field)
		northPanel = new JPanel();
		northPanel.add(new JLabel("Enter the function (of x) to graph:"));
		northPanel.add(messageField);
		messageField.addActionListener(this);

		
		// Left (settings)
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		
		//private JTextField width, height, graphDegree, graphInterval, graphXMin, graphXMax, graphYMin, graphYMax;
		//private JPanel widthPanel, heightPanel, graphDegreePanel, graphIntervalPanel, graphXMinPanel, graphXMaxPanel, graphYMinPanel, graphYMaxPanel;
		
		graphDegree = new JTextField(4);
		graphInterval = new JTextField(4);
		graphXMin = new JTextField(4);
		graphXMax = new JTextField(4);
		graphYMin = new JTextField(4);
		graphYMax = new JTextField(4);
		
		graphXMin.setText("" + minX);
		graphXMax.setText("" + maxX);
		graphYMin.setText("" + minY);
		graphYMax.setText("" + maxY);
		
		graphDegreePanel = new JPanel();
		graphIntervalPanel = new JPanel();
		graphXMinPanel = new JPanel();
		graphXMaxPanel = new JPanel();
		graphYMinPanel = new JPanel();
		graphYMaxPanel = new JPanel();
		
		graphDegreePanel.add(new JLabel("Degree"));
		graphIntervalPanel.add(new JLabel("Interval"));
		graphXMinPanel.add(new JLabel("X min"));
		graphXMaxPanel.add(new JLabel("X max"));
		graphYMinPanel.add(new JLabel("Y min"));
		graphYMaxPanel.add(new JLabel("Y max"));

		graphDegreePanel.add(graphDegree);
		graphIntervalPanel.add(graphInterval);
		graphXMinPanel.add(graphXMin);
		graphXMaxPanel.add(graphXMax);
		graphYMinPanel.add(graphYMin);
		graphYMaxPanel.add(graphYMax);
		
		graphDegree.addActionListener(this);
		graphInterval.addActionListener(this);
		graphXMin.addActionListener(this);
		graphXMax.addActionListener(this);
		graphYMin.addActionListener(this);
		graphYMax.addActionListener(this);

		leftPanel.add(graphDegreePanel);
		leftPanel.add(graphIntervalPanel);
		leftPanel.add(graphXMinPanel);
		leftPanel.add(graphXMaxPanel);
		leftPanel.add(graphYMinPanel);
		leftPanel.add(graphYMaxPanel);

		
		// South(Message field)
		southPanel = new JPanel();
	    southPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 5));
		southPanel.add(messageLabel);
		southPanel.add(errorLabel);

		// add to the frame.
		frame.setSize(windowWidth,windowHeight);
		frame.getContentPane().add(northPanel, BorderLayout.NORTH);
		frame.getContentPane().add(leftPanel, BorderLayout.EAST);
		frame.getContentPane().add(southPanel, BorderLayout.SOUTH);
		frame.getContentPane().add(this, BorderLayout.CENTER);
		
		// set visibility and closing operation.
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
		graphDegree.setText("" + degree);
	}
	
	/*
	 * setFourierInterval
	 * 
	 * PRE: true
	 * POST: Sets the new Fourier interval of the function.
	 */
	public void setFourierInterval(double interval) {
		fourierInterval = interval;
		graphInterval.setText("" + interval);
	}
	
	private void updateDegreeAndInterval() {
		for (int i = 1; i < exprCount; i+=2) {
			exprs[i] = FourierSeries.fourierSeriesAndPrint(exprs[i-1], fourierInterval, INTERVALS_FACTOR * fourierDegree, fourierDegree, varList); 
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		// the user pressed enter in the message box
		if (e.getSource() == messageField) {
			// read entered text, parse it, graph it and it's fourier transform.
			functionString = messageField.getText().trim();
			System.out.println("f(x) = " + functionString);
			
			// Parse expression.
			AbstractExpression expr = Parsing.toExpression(functionString);
			AbstractExpression fourier_expr = FourierSeries.fourierSeriesAndPrint(expr, fourierInterval, INTERVALS_FACTOR * fourierDegree, fourierDegree, varList); 
			clearGraphs();
			addGraph(expr);
			addGraph(fourier_expr);
			repaint();
			
			// place expression at bottom of frame
			messageLabel.setText("f(x) = " + functionString);
		} else if (e.getSource() == graphDegree) {
			String message  = graphDegree.getText().trim();
		    try { 
		    	int deg = Integer.parseInt(message);
		    	if (deg >= 0 && deg <= 100) {
				    setFourierDegree(deg);
				    updateDegreeAndInterval();
		    	} else if (deg < 0) {
		    		graphDegree.setText("" + fourierDegree);
		    		errorLabel.setText("ERROR - Degree is too low (< 0).");
		    	} else {
		    		graphDegree.setText("" + fourierDegree);
		    		errorLabel.setText("ERROR - Degree is too high (> 100).");
		    	}
		    	repaint();
		    } catch(NumberFormatException ex) { 
	    		graphDegree.setText("" + fourierDegree);
	    		errorLabel.setText("ERROR - Invalid degree integer.");
		    }
		} else if (e.getSource() == graphInterval) {
			String message  = graphInterval.getText().trim();
		    try { 
		    	Double interval = Double.parseDouble(message);
		    	if (interval > 0) {
				    setFourierInterval(interval);
				    updateDegreeAndInterval();
		    	} else {
		    		graphInterval.setText("" + fourierInterval);
		    		errorLabel.setText("ERROR - Interval is too low (< 0).");
		    	} 
		    	repaint();
		    } catch(NumberFormatException ex) { 
		    	graphInterval.setText("" + fourierInterval);
	    		errorLabel.setText("ERROR - Invalid interval double.");
		    }
		} else if (e.getSource() == graphXMin) {
			String message  = ((JTextField) e.getSource()).getText().trim();
		    try { 
		    	int interval = Integer.parseInt(message);
			    if (interval < maxX) {
			    	minX = interval;
			    } else {
			    	graphDegree.setText("" + minX);
			    	errorLabel.setText("ERROR - minX was >= maxX");
			    }
			    repaint();
		    } catch (NumberFormatException ex) { 
		    	graphDegree.setText("" + minX);
	    		errorLabel.setText("ERROR - Invalid X min integer.");
		    }
		} else if ( e.getSource() == graphXMax ) {
			String message  = ((JTextField) e.getSource()).getText().trim();
		    try { 
		    	int interval = Integer.parseInt(message);
			    if (interval > minX) {
			    	maxX = interval;
			   	} else {
			   		graphDegree.setText("" + maxX);
			   		errorLabel.setText("ERROR - maxX was <= minX");
			   	}
			    repaint();
		    } catch (NumberFormatException ex) { 
		    	graphDegree.setText("" + maxX);
	    		errorLabel.setText("ERROR - Invalid X max integer.");
		    }
		} else if (e.getSource() == graphYMin) {
			String message  = ((JTextField) e.getSource()).getText().trim();
		    try { 
		    	int interval = Integer.parseInt(message);
			    if (interval < maxY) {
			    	minY = interval;
			    } else {
			    	graphDegree.setText("" + minY);
					errorLabel.setText("ERROR - minY was >= maxY");
			    }
			    repaint();
		    } catch (NumberFormatException ex) { 
		    	graphDegree.setText("" + minY);
	    		errorLabel.setText("ERROR - Invalid Y min integer.");
		    }
		} else if (e.getSource() == graphYMax) {
			String message  = ((JTextField) e.getSource()).getText().trim();
		    try { 
		    	int interval = Integer.parseInt(message);
			    if (interval > minY) {
			   		maxY = interval;
			   	} else {
			   		graphDegree.setText("" + maxY);
			   		errorLabel.setText("ERROR - maxY was <= minY");
			   	}
			    repaint();
		    } catch (NumberFormatException ex) { 
		    	graphDegree.setText("" + maxY);
	    		errorLabel.setText("ERROR - Invalid Y max integer.");
		    }
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
     public void paint(Graphics g)
     {
    	 double scaleY = (frame.getHeight() - northPanel.getHeight() - southPanel.getHeight()) / (maxY-minY);
    	 double scaleX = (frame.getWidth() - leftPanel.getWidth()) / (maxX-minX);
    	 
    	 ///draw in the axis and labels. Bunch of math to scale correctly.
    	 g.setColor(Color.black);
    	 g.drawLine((int)((-1*minX)*scaleX), 0, (int)((-1*minX)*scaleX), frame.getHeight()); //y axis
    	 g.drawLine(0,(int)(maxY * scaleY), frame.getWidth(), (int)(maxY * scaleY)); //x axis

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