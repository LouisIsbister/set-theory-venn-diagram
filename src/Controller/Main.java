package Controller;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import Tree.BTNode;
import Tree.BuildTree;
import Tree.Coordinate;
import Tree.SetNode;

/**
 * @author Louis Isbister
 * 
 * (C ∩ D) U (A \ ((B U C) ∩ (D U E)))
 * 
 * The following are a series of valid equations:
	# "union(intersect(A B) intersect(C D))"
	# "union(A intersect(B C))"
	# "intersect(A difference(B C))"
	# "difference(intersect(C union(A B)) intersect(A B))"
	# "union(intersect(C D) difference(A intersect(union(B C) union(D E))))"
	# "complement(A)
	# "complement(difference(B C))"
	# "union(intersect(A complement(union(B C))) intersect(B C))"
	# 
	# Invalid cases:
	# "complement(A difference(B C))"
	# "union(intersect(A C D))"
 */

public class Main {
	public static void main(String args[]) {
		BTNode root = BuildTree.createTree("union(intersect(union(B C) A) intersect(B C))");
				
		if(root == null) {
			System.out.println("NullPointer in Main: Parsing error, please check your format.");
			return;
		}
		
		Set<Coordinate> highlightCoords = new HashSet<>();
		try {
			highlightCoords = root.evaluate();
		} catch(Exception err) {
			System.out.println("----- ERROR -----");
			System.out.println("Error message: " + err.getMessage());
			return;
		}
		
		Collection<SetNode> nodes = BuildTree.setNodes();
				
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
