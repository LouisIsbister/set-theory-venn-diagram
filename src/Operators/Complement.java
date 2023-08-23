package Operators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Tree.BTNode;
import Tree.Coordinate;

public class Complement implements Operator{
	
	@Override
	public Set<Coordinate> evaluate(BTNode left, BTNode right) throws Exception {
		Set<Coordinate> leftSet = left.evaluate();
		Set<Coordinate> rightSet = right.evaluate();
		
		List<Coordinate> coords = rightSet.stream()
				.filter(elem-> !leftSet.contains(elem))
				.toList();
		
		return new HashSet<>(coords);
	}
	
	public String toString() {
		return "complement";
	}
	
}