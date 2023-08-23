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
	public static void main(String args[]) {
		BTNode root = new BTNode();
		BuildTree tree = new BuildTree();
		Set<Coordinate> highlightCoords = new HashSet<>();

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
	public static void dfs(BTNode root) {
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
