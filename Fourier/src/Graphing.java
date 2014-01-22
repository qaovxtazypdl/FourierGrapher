import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

// The graphing class - handles graphing of the functions.
public class Graphing extends JComponent implements ActionListener {
	// Maximum variables our varList map can hold.
	public static final int MAX_VARS = 5;
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
	
	// Constructor
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
		this.exprCount = 0;
		
		// init the swing window
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		frame = new JFrame("Fourier Series Expansion Graph View."); 
		messageField = new JTextField(20);
		messageString = "f(x) = ";
		messageLabel = new JLabel(messageString);
		frame.getContentPane().setBackground(Color.white);
		// North (text field)
		JPanel northPanel = new JPanel();
		northPanel.add(new JLabel("Enter the function to graph:"));
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
	 * Adds an expression to graph as a function of x.
	 */
	public void addGraph(AbstractExpression expr) {
		exprs[exprCount++] = expr;
		repaint();
	}
	
	/*
	 * Removes all graphs from the screen.
	 */
	public void clearGraphs() {
		exprCount = 0;
		repaint();
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
			// read entered text
			messageString = messageField.getText();
			//parse s into an AbstractExpression, set messageLabel text to toString() of the expr.
			//expr = parse(s)
			// clear the field
			messageField.setText("");
			// place message at bottom of frame
			messageLabel.setText("f(x) = " + messageString);
			// place message in list of messages on left of screen
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