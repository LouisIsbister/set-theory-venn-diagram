package code.operators;

import java.util.Set;

import code.expressions.BTNode;
import code.util.Coordinate;

/**
 * Operator interface, each operator overrides
 * the evaluate method to perform their unique
 * operation on the sets.
 */
public interface Operator {
	public Set<Coordinate> evaluate(BTNode left, BTNode right) throws IllegalArgumentException;
}
