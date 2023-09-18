package code.controller;
/**
 * 
 * @author Louis Isbister
 * 
 */
public class Main {

	/**
	 * Some valid expressions to test:
	 * "union(intersect(A B) intersect(C D))"
	 * "union(A intersect(B C))"
	 * "intersect(A difference(B C))"
	 * "difference(intersect(C union(A B)) intersect(A B))"
	 * "union(intersect(C D) difference(A intersect(union(B C) union(D E))))"
	 * "complement(A)"
	 * "complement(difference(B C))"
	 * "union(intersect(A complement(union(B C))) intersect(B C))"
	 * "difference(A intersect(A union(B C)))"
	 */

	public static void main(String args[]) {
		new Frame();
	}
}
