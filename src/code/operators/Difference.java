package code.operators;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import code.binarytree.BTNode;
import code.binarytree.Coordinate;

/**
 * Set difference class, in mathmatical notaion x\y
 * "Find all the coordinates in x that are not in y"
 */
public class Difference implements Operator{
	
	/**
	 * Find the set difference between of two nodes. 
	 * returns all the values in left that are not 
	 * in right. 
	 * 
	 * @param left, left child node
	 * @param right, right child node
	 * @return, the difference between left and right nodes
	 */
	@Override
	public Set<Coordinate> evaluate(BTNode left, BTNode right) throws Exception {
		Set<Coordinate> leftSet = left.evaluate();
		Set<Coordinate> rightSet = right.evaluate();

		List<Coordinate> list = leftSet.stream()
				.filter(elem -> !rightSet.contains(elem))
				.toList();
		
		return new HashSet<>(list);
	}
	
	public String toString() {
		return "difference";
	}
}
