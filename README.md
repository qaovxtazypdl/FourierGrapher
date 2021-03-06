FourierGrapher
==============

Determines and graphs the fourier series approximation of a function to varying degrees.

![Screenshot](http://i.imgur.com/ZJZCuNS.png)

Usage
======
Follow prompts to specify graph properties. This will be integrated in the GUI later.
Enter an infix notation function of x parenthesized where necessary into the prompt box and hit enter.
E.g. 1+x, 1+x^4-x^3+sin(cos(x))/2
Supported functions include:

Binary: + - / * ^
Unary: sin() cos()tan() asin() acos() atan() sinh() cosh() tanh() neg() abs() log() ln() sqrt() floor() ceil() H() (()

Notes: 
unary operators must have parentheses around its argument. Negation must be done using neg(), not -.
H() is the heaviside function. (() is the identity operator.

Background
=====
This originally started as a swing function grapher that was completed in June 2013.
On taking calculus 4 and learning about Fourier series, I found it interesting to graph the fourier partial series for a function
and observe its behaviour. I decided to try coding my own Fourier calculator and grapher as an exercise, since I already have a grapher. 

It turned out that I did not like (...more like I could not read) the old grapher code, so I ended up implementing everything except for the JFrames from scratch, with proper documentation, OOP, cohesion and coupling, and parsing methods.

Function 
=====
Input: function in infix notation into a textbox in Swing. 

GUI: Graphs the input function and the Fourier series to some term Tn.

Output: Print the resulting series in a orderly manner to System.out


Technical overview
======
- Expression abstract base class with abstract recursive evaluate implemented for each type of expression subclass (binary, unary, integral, number, variable, summation). Integrals are approximated using Simpson's rule.
- FourierSeries, Parsing static classes to provide related methods without bundling with unrelated classes. FourierSeries handles most of the calculations to determine the series coefficients. Parsing implements a modified shunting yard algorithm (for compatibility with unary and 4-ary operators as well as binary).
- Graphing class handles all the graphing and input from the swing applet. Coded to allow for possible improvements/expansion in the future. 
- Operator class enumerates all accepted operators and their precedences, as well as their arity. Mostly as a helper to Parsing. All non-binary operators are precedence 1, and closing bracket is precedence 0, and both 0 and 1 precedence are handled in special cases within Parsing. Note that here, left bracket - "(" is implemented as an unary (identity) operator (x = x. 
- Main class: will take in command line arguments and set graph settings.


Known issues
=========
- Jump discontinuities, infinite discontinuities in graphed functions can cause inaccuracy in integration and graphing.
- What integration approximation interval would be the best for both efficiency and accuracy?
- Must use neg(x) function to represent negative numbers in input. 



Possible expansions in the future
==========
Error checking on inputs

Command line options for graph customization
OR
Graph interactivity

Taylor series / Taylor polynomials

Graph only mode (no series)

Calculator mode(Integrals, Summations, differentiation in n-variables!)
This is just disabling the graph and calculating fourier to degree 0.

