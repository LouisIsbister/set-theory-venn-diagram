package code.operators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import code.binarytree.BTNode;
import code.binarytree.Coordinate;

/**
 * Set complement class, in mathmatical notaion U\X
 * "Find all the coordinates in the universal set
 * that are not in X"
 */
public class Complement implements Operator {

	/**
	 * Returns all the values in the universal set
	 * excluding the those in the left set.
	 * Is represetned by U\A.
	 * 
	 * @param left,  left child node
	 * @param right, right child node
	 *               @return, the complement of the left node
	 */
	@Override
	public Set<Coordinate> evaluate(BTNode left, BTNode right) throws Exception {
		Set<Coordinate> leftSet = left.evaluate();
		Set<Coordinate> rightSet = right.evaluate();

		List<Coordinate> coords = rightSet.stream()
				.filter(elem -> !leftSet.contains(elem))
				.toList();

		return new HashSet<>(coords);
	}

	public String toString() {
		return "complement";
	}

}