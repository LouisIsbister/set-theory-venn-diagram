package Operators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Tree.BTNode;
import Tree.Coordinate;
/**
 * Set difference class, will be use in the for A\B
 * "Find all the coordinates in A and not B
 * 
 * @author Louis Isbister
 */

public class Difference implements Operator{

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
