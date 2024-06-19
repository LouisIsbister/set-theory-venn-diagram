package stvd.operators;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import stvd.expressionparser.ExpressionTree;
import stvd.tree.*;
import stvd.util.Coordinate;
import stvd.util.InvalidExpressionException;

/**
 * Set complement class, in mathmatical notaion U\X
 * "Find all the coordinates in the universal set
 * that are not in X"
 */
public class Complement extends BTNode {

	/**
	 * The universal set represents all possible data and is constant.
	 */
	private static final HashSet<Coordinate> universalSet = new HashSet<>();

	static {
		for (int i = 0; i < 2 * ExpressionTree.START_Y; i++) {
			for (int j = 0; j < 2 * ExpressionTree.START_X; j++) {
				universalSet.add(new Coordinate(i, j));
			}
		}
	}

	/**
	 * Returns all the values in the universal set
	 * excluding the those in the left set.
	 * Is represetned by U\A.
	 * 
	 * @return, the complement of the left node
	 * @throws InvalidExpressionException 
	 */
	@Override
	public Set<Coordinate> evaluate() throws InvalidExpressionException {
		if (left() == null) {
			throw new InvalidExpressionException("Failed to Execute<br>" + toString() + " is missing an arg.");
		}

		Set<Coordinate> leftSet = left().evaluate();
		return universalSet.stream()
				.filter(e -> !leftSet.contains(e))
				.collect(Collectors.toSet());
	}

	public String toString() {
		return "~";
	}

}