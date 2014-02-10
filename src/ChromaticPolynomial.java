import java.math.BigInteger;
import java.util.ArrayList;


public class ChromaticPolynomial {
	public static Graph g;
	public static ArrayList<Graph> finalGraphList;
	public static BigInteger length;
	
	public static void main (String args[]) {
		
		if (args.length == 0) {
			StdOut.println("No graphs to process!");
		} else {
			for (int i = 0; i < args.length; i++) {
				length = BigInteger.valueOf(args.length);
				BigPolynomial chromaticPoly = computeChromaticPolynomial (args[i]);
				StdOut.println("P(x) = " + chromaticPoly);
			}
		}
	}
	
	public static BigPolynomial computeChromaticPolynomial(String graphFile) {
		ArrayList<Graph> graphList = new ArrayList<Graph>();
		In graphIn = new In(graphFile);
		Graph origGraph = new Graph(graphIn);
		g = origGraph;
		StdOut.println(origGraph);
		
		graphList.add(origGraph);
		
		removeAndCondense(graphList);
		//StdOut.println("I am out of removeAndCondense");
		/*for (Graph g : graphList) {
			StdOut.println(g);
		}*/
		
		BigPolynomial result = toPolynomialForm(graphList);
	
		return result;
	}
	
	public static void removeAndCondense(ArrayList<Graph> gList) {
		ArrayList<Graph> tempList = new ArrayList<Graph>();
		boolean hasAllNullGraphs = true;
		for (Graph g : gList) {
			if (g.E() > 0) {
				int[] edge = g.getEdge(g);
				//StdOut.println("Got Edge (" + edge[0] + " --> " + edge[1] + ")" );
				g.removeEdge(edge[0], edge[1]);
				Graph condensedGraph = g.condense(edge[0], edge[1]);
				tempList.add(condensedGraph);	
				hasAllNullGraphs = false;
			}
		}
		
		for (Graph n : tempList) {
			gList.add(n);
		}
		
		if (hasAllNullGraphs == false) {
			removeAndCondense(gList);
		} else {
			StdOut.println("Finished");
		}
			
	}
	
	public static BigPolynomial toPolynomialForm(ArrayList<Graph> gList) {
		ArrayList<BigPolynomial> bigPolyList = new ArrayList<BigPolynomial>();
		BigPolynomial result = new BigPolynomial();
		for (Graph g : gList) {
			BigInteger coef = BigInteger.valueOf(1);
			int deg = g.V();
			//StdOut.println("Degree: " + deg);
			BigPolynomial poly = new BigPolynomial(coef, deg);
			//StdOut.println(poly);
			bigPolyList.add(poly);
		}
		
		for (BigPolynomial p : bigPolyList) {
			//StdOut.println("Degree of g: " + g.V());
			if (p.degree() == g.V() || (g.V() - p.degree())%2 == 0) {
				result = result.plus(p);
				//StdOut.println(result);
			}else {
				result = result.minus(p);
			}
		}
		
		//Subtract from highest degree and lowest degree to get final answer
		BigPolynomial degreeZero = new BigPolynomial(BigInteger.valueOf(1), 0);
		BigInteger newCoeff = BigInteger.valueOf(gList.size()/2).add(length);
		//StdOut.println(newCoeff);
		BigPolynomial highestDegree = new BigPolynomial(newCoeff, g.V());
		result = result.minus(degreeZero).minus(highestDegree);
		return result;
	}
}
