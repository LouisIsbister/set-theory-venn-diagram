package com.stvd.operators;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.stvd.controller.AppPanel;
import com.stvd.tree.BTNode;
import com.stvd.util.*;

public class Complement extends BTNode {

	/**
	 * The universal set represents all the display pixels
	 */
	private static final Set<Coordinate> universalSet = 
		IntStream.range(0, AppPanel.WIDTH * AppPanel.HEIGHT).boxed()
				.map(e -> new Coordinate(e % AppPanel.WIDTH, e / AppPanel.HEIGHT))
				.collect(Collectors.toSet());

	/**
	 * Returns all the values in the universal set
	 * excluding the those in the left set.
	 * 
	 * @return the complement of the left node
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
