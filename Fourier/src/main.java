class Fourier {
	public static void main(String[] args) {
		
		Graphing graph = new Graphing(1000);
		graph.setFourierDegree(10);
		//TODO: if interval is close to k*PI, set to k*PI...
		graph.setFourierInterval(Math.PI);
	}
}
