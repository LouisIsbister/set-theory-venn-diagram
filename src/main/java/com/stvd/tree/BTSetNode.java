package com.stvd.tree;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.stvd.util.Coordinate;

public class BTSetNode extends BTNode {

	/**
	 * Set's id
	 */
	private String identifier;

	/**
	 * The collection of all pixels in the set node, represents the "data" of this
	 * set.
	 */
	private Set<Coordinate> pixels;

	/**
	 * The coordinate of the center of the circle that graphically represents this
	 * set.
	 */
	private Coordinate center;

	/**
	 * The coordinate for the identifier to be graphically displayed.
	 */
	private Coordinate toStringPosition;

	/**
	 * The diameter of the circle that graphically represents a set.
	 */
	public static final int DIAMETER = 250;

	public BTSetNode(String identifier) {
		this.identifier = identifier;
		pixels = new HashSet<>();
	}

	@Override
	public Set<Coordinate> evaluate() {
		return Collections.unmodifiableSet(pixels);
	}

	public void addPixel(Coordinate coord) {
		pixels.add(coord);
	}

	public void setCenter(Coordinate center) {
		this.center = center;
	}

	public Coordinate center() {
		return center;
	}

	public void setStringPosition(Coordinate strPosition) {
		toStringPosition = strPosition;
	}

	public Coordinate stringPosition() {
		return toStringPosition;
	}

	public String toString() {
		return identifier;
	}
}
