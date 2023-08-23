import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * 
 * @author Louis Isbister
 * 
 */
public class Main {

	/**
	 * Valid expressions to test:
	 * "union(intersect(A B) intersect(C D))"
	 * "union(A intersect(B C))"
	 * "intersect(A difference(B C))"
	 * "difference(intersect(C union(A B)) intersect(A B))"
	 * "union(intersect(C D) difference(A intersect(union(B C) union(D E))))"
	 * "complement(A)"
	 * "complement(difference(B C))"
	 * "union(intersect(A complement(union(B C))) intersect(B C))"
	 */
		

	public static void main(String args[]) {
		BTNode root = new BTNode();
		BuildTree tree = new BuildTree();
		Set<Coordinate> highlightCoords = new HashSet<>();

		// ENTER your expression here, a default expression has been given:
		String expression = "union(intersect(C D) difference(A intersect(union(B C) union(D E))))";
		
		try {
			root = tree.createTree(expression);
			highlightCoords = root.evaluate();
		} catch(Exception err) {
			System.out.println("----- ERROR -----");
			System.out.println("Occurred when parsing and evaluating the expression.\nPlease check your format.");
			System.out.println("Error message: " + err.getMessage());
			return;
		}
		
		Collection<SetNode> nodes = tree.setNodes();
				
		new Frame(highlightCoords, nodes);
	}
		
	/**
	 * DEBUGGING METHOD: 
	 * Performs a breadth first traversal of the binary tree, printing the parent node 
	 * and its children.
	 * @param root, the root node
	 */
	public static void bfs(BTNode root) {
		Queue<BTNode> queue = new ArrayDeque<BTNode>();
		queue.offer(root);

		while(!queue.isEmpty()) {
			BTNode node = queue.poll();
			System.out.println("Parent: " + node + " -> left: " + node.left()+ ", right: " + node.right());
			
			if(node.left() != null) {
				queue.add(node.left());
			}
			if(node.right() != null){
				queue.add(node.right());
			}
		}
	}
}
