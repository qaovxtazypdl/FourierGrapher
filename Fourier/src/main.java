import java.util.*;
class Fourier {
	public static void main(String[] args) {
		System.out.println("Enter degree of Fourier series to evaluate to.");
		Scanner sc = new Scanner(System.in);
		int degree = sc.nextInt();
		
		System.out.println("Enter Fourier series evaluation interval.");
		double interval = sc.nextDouble();
		// x and y min and max... for now
		int graphXY = (int)(interval * 3);
		
		Graphing graph = new Graphing(700, 700, -graphXY, graphXY, -graphXY, graphXY, 1000);
		graph.setFourierDegree(degree);
		//TODO: if interval is close to k*PI, set to k*PI...
		graph.setFourierInterval(interval);
		sc.close();
	}
}
