package stvd.operators;

import java.util.Set;
import java.util.stream.Collectors;

import src.stvd.tree.BTNode;
import src.stvd.util.*;

/**
 * Set intersect class, in mathmatical notaion (x ∩ y)
 * "Find all the coordinates in x that also in y"
 */
public class Intersect extends BTNode {

	/**
	 * Find the set intersect between of two nodes.
	 * returns all the values in left that also in
	 * right.
	 * 
	 * @return, the intersect of left and right nodes
	 * @throws InvalidExpressionException 
	 */
	@Override
	public Set<Coordinate> evaluate() throws InvalidExpressionException {
		if (left() == null || right() == null) {
			throw new InvalidExpressionException("Failed to Execute<br>" + toString() + " is missing an arg.");
		}
		
		Set<Coordinate> leftSet = left().evaluate();
		Set<Coordinate> rightSet = right().evaluate();
		return leftSet.stream()
				.filter(elem -> rightSet.contains(elem))
				.collect(Collectors.toSet());
	}

	public String toString() {
		return "\u2229";
	}
}
