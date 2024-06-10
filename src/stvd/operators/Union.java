package stvd.operators;

import java.util.HashSet;
import java.util.Set;

import stvd.tree.BTNode;
import stvd.util.Coordinate;

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
	 */
	@Override
	public Set<Coordinate> evaluate() throws IllegalArgumentException {
		Set<Coordinate> allCoords = new HashSet<>();
		allCoords.addAll(left().evaluate());
		allCoords.addAll(right().evaluate());
		return allCoords;
	}

	public String toString() {
		return "union";
	}
}