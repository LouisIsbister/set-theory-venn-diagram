package code.operators;
import java.util.HashSet;
import java.util.Set;

import code.binarytree.BTNode;
import code.binarytree.Coordinate;

/**
 * Set union class, in mathmatical notaion (x U y)
 * "Find all the coordinates in x that are also in y"
 */
public class Union implements Operator{

	/**
	 * Find the set union between of two nodes. 
	 * returns all the values in left that are also in 
	 * right. 
	 * 
	 * @param left, left child node
	 * @param right, right child node
	 * @return, the union of left and right nodes
	 */
	@Override
	public Set<Coordinate> evaluate(BTNode left, BTNode right) throws Exception {
		Set<Coordinate> allCoords = new HashSet<>();
		allCoords.addAll(left.evaluate());
		allCoords.addAll(right.evaluate());
		return allCoords;
	}
	
	public String toString() {
		return "union";
	}
}