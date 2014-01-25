class Fourier {
	public static void main(String[] args) {
		Graphing graph = new Graphing(700, 700, -12, 12, -12, 12, 1000);
		graph.setFourierDegree(10);
		graph.setFourierInterval(Math.PI);
	}
}

//INT((1)(2)(INT((2)(5)(x^y)(y)))(x))