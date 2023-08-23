package Operators;

import Tree.BTNode;
import Tree.Coordinate;
import java.util.Set;

public interface Operator {
	public Set<Coordinate> evaluate(BTNode left, BTNode right) throws Exception;
}
