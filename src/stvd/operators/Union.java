package stvd.operators;

import java.util.HashSet;
import java.util.Set;

import stvd.tree.BTNode;
import stvd.util.Coordinate;
import stvd.util.InvalidExpressionException;

/**
 * Set union class, in mathmatical notaion (x U y)
 * "Find all the coordinates in x that are also in y"
 */
public class Union extends BTNode {

	/**
	 * Find the set union between of two nodes.
	 * returns all the values in left that are also in
	 * right.
	 * 
	 * @return, the union of left and right nodes
	 * @throws InvalidExpressionException 
	 */
	@Override
	public Set<Coordinate> evaluate() throws InvalidExpressionException {
		if (left() == null || right() == null) {
			throw new InvalidExpressionException("Failed to Execute<br>" + toString() + " is missing an arg.");
		}

		Set<Coordinate> allCoords = new HashSet<>();
		allCoords.addAll(left().evaluate());
		allCoords.addAll(right().evaluate());
		return allCoords;
	}

	public String toString() {
		return "\u222A";
	}
}