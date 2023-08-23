package Operators;

import java.util.HashSet;
import java.util.Set;

import Tree.BTNode;
import Tree.Coordinate;

public class Union implements Operator{
	
	@Override
	/**
	 * Method that evaluates a union operator, return all the elements in the two sets.
	 * 
	 * @param left, the left node of the operator
	 * @param right, the right node of the operator
	 * @return, all the coordinates of the left and right nodes
	 */
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