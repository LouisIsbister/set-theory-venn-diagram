package code.binarytree;
import java.util.Set;

import code.operators.Operator;

public class BTNode {
	
	/**
	 * The operator of this node. 
	 */
	private Operator operator;

	/**
	 * The left child of this node.
	 */
	private BTNode left = null;

	/**
	 * The right child of this node.
	 */
	private BTNode right = null;
	
	/**
	 * Creats a new binary tree node with an operator.
	 * 
	 * @param operator, the operator/action to be performed upon sets
	 */
	public BTNode(Operator operator) {
		this.operator = operator;
	}
	
	/**
	 * default constructor
	 */
	public BTNode() {}
	
	/**
	 * Calls the evaluate method of its operator, the operator will then evaluate the two sets.
	 * 
	 * @return, the set of coordinates when two nodes are evaluated by an operator
	 * @throws Exception, if either the left or right node is null then throw an 
	 * IllegalArgumentException.
	 */
	public Set<Coordinate> evaluate() throws Exception{
		if(left == null || right == null) {
			throw new IllegalArgumentException(this.toString() + ": an argument is undefined, please reformat your experssion.");
		}
		
		return operator.evaluate(left, right);
	}
	
	// --- getters and setters --- //
	
	public BTNode left() {
		return left;
	}
	
	public void setLeft(BTNode left) {
		this.left = left;
	}
	
	public BTNode right() {
		return right;
	}
	
	public void setRight(BTNode right) {
		this.right = right;
	}
	
	public String toString() {
		return operator.toString();
	}
}
