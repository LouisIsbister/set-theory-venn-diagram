package com.stvd.operators;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.stvd.controller.AppPanel;
import com.stvd.tree.BTNode;
import com.stvd.util.*;

public class Complement extends BTNode {

	/**
	 * The universal set represents all possible data and is constant.
	 */
	private static final HashSet<Coordinate> universalSet = new HashSet<>();

	static {
		for (int i = 0; i < AppPanel.WIDTH; i++) {
			for (int j = 0; j < AppPanel.HEIGHT; j++) {
				universalSet.add(new Coordinate(i, j));
			}
		}
	}

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
