package Tree;

import java.util.Set;

import Operators.*;

public class BTNode {
	
	/**
	 * Operator field, can be intersect or union for the moment 
	 */
	private Operator operator;
	private BTNode left = null;
	private BTNode right = null;
	
	/**
	 * creating a new node with only an operator
	 * @param operator, the action to be performed upon the sets
	 */
	public BTNode(Operator operator) {
		this.operator = operator;
	}
	
	/**
	 * default constructor
	 */
	public BTNode() {}
	
	/**
	 * @return, the set of coordinates when two nodes are evaluated by an operator
	 * @throws Exception 
	 */
	public Set<Coordinate> evaluate() throws Exception{
		if(left == null || right == null) {
			throw new Exception(this.toString() + ": an argument is null, please reformat your experssion");
		}
		
		return this.operator.evaluate(left, right);
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
