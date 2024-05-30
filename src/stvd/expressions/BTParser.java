package stvd.expressions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import stvd.controller.AppPanel;
import stvd.operators.*;
import stvd.util.Coordinate;
import stvd.util.InvalidExpressionException;

public class BTParser {

	/**
	 * The central x and y coordinates of the Venn diagram
	 */
	public static final int START_X = AppPanel.WIDTH / 2;
	public static final int START_Y = AppPanel.HEIGHT / 2;

	/**
	 * root node of the binary tree
	 */
	private BTNode root;

	/**
	 * characters in the expression
	 */
	private Queue<Character> expression;

	/**
	 * Map of all the sets in the expression
	 */
	private Map<Character, SetNode> setNodes = new HashMap<>();

	/**
	 * The universal set represents all the available data and is constant.
	 */
	private static final SetNode universalSet = new SetNode("Universal data") {
		@Override
		public Set<Coordinate> evaluate() {
			HashSet<Coordinate> allCoords = new HashSet<>();
			for (int i = 0; i < 2 * START_Y; i++) {
				for (int j = 0; j < 2 * START_X; j++) {
					allCoords.add(new Coordinate(i, j));
				}
			}

			return allCoords;
		}
	};

	public BTParser(String expr) throws InvalidExpressionException {
		expression = ExpressionParser.parse(expr.trim());

		buildExpressionTree();
		propagateSetNodes();
	}

	/**
	 * @return, the root node of the binary tree
	 */
	public BTNode root() {
		return root;
	}

	/**
	 * @return, the collection of set nodes
	 */
	public ArrayList<SetNode> setNodes() {
		return new ArrayList<>(setNodes.values());
	}

	/**
	 * This method intialises the creation of the tree structure.
	 * It first calls the recursive method to build the binary tree. It
	 * then checks for formatting errors where too many arguments were
	 * provided or if there are invalid set identifiers.
	 * 
	 * @throws InvalidExpressionException
	 */
	private void buildExpressionTree() throws InvalidExpressionException {
		root = parseTree();

		// user provided too many or no arguments
		if (!expression.isEmpty()) {
			String ret = "Invalid expression: too many arguments were provided.<br>"
					+ "Remaining unrecogised args: " + expression;
			throw new InvalidExpressionException(ret);
		}

		if (setNodes.size() < 1) {
			throw new InvalidExpressionException("Invalid expression: no set/data was provided.");
		}
	}

	/**
	 * Check whether a given character is an operator
	 * 
	 * @param c, the provided character
	 * @return
	 */
	protected static boolean charIsOperator(Character c) {
		String value = String.valueOf(c);
		return value.matches("[\u222A|\u2229|\\\\|~]");
	}

	/**
	 * This method recursively builds the binary tree of the expression.
	 * It retrieves the next element from the scanner and checks whether it
	 * is an operator. If so, create a new parent node and set its left and right
	 * children.
	 * Otherwise, find or create a new set node (leaf) and return it.
	 * 
	 * @param scan, the scanner that traverses the expression
	 *              @return, the root node of the binary tree
	 * @throws InvalidExpressionException
	 */
	private BTNode parseTree() throws InvalidExpressionException {
		if (expression.isEmpty())
			return null;

		Character next = expression.poll();

		if (BTParser.charIsOperator(next)) {
			Operator op = parseOperator(next);
			BTNode node = new BTNode(op);
			node.setLeft(parseTree());

			// if the operator is a complement then the right node must be the universal set
			if (op instanceof Complement) {
				node.setRight(universalSet);
			} else {
				node.setRight(parseTree());
			}

			return node;
		} else {
			next = Character.toLowerCase(next);
			if (!setNodes.containsKey(next)) {
				setNodes.put(next, new SetNode(next + ""));
			}
			return setNodes.get(next);
		}
	}

	/**
	 * returns and instance of the operator that corresponds to
	 * the given character.
	 * 
	 * @param c, the operator
	 *           @return, an instance of the operator
	 * @throws InvalidExpressionException
	 */
	private Operator parseOperator(char c) throws InvalidExpressionException {
		return switch (c) {
			case '\u222A' -> new Union();
			case '\u2229' -> new Intersect();
			case '\\' -> new Difference();
			case '~' -> new Complement();
			default -> throw new InvalidExpressionException("'" + c + "' is an invalid operator.");
		};
	}

	/**
	 * This method determines and initializes the central coordinate of a set.
	 * Then, every pixel within the sphere that represents a set is added to the
	 * collection of pixels in that set.
	 * Each pixel represents a piece of data within the set.
	 */
	private void propagateSetNodes() {
		final int NUMBER_OF_SETS = setNodes.size();
		final int RADIUS = SetNode.DIAMETER / 2;

		// the angle difference of each circle from the center
		final double ANGLE_OFFSET = 360 / NUMBER_OF_SETS;

		int setCount = 0;
		for (SetNode setNode : setNodes.values()) {
			// get the angle of the sets circle
			double angle = (setCount * ANGLE_OFFSET) - 90;
			double angleInRadians = angle * (Math.PI / 180);

			// get the center coordinates of this sets spherical representation
			int xCoord = START_X + (int) (Math.cos(angleInRadians) * RADIUS / 2);
			int yCoord = START_Y + (int) (Math.sin(angleInRadians) * RADIUS / 2);

			Coordinate coord = new Coordinate(xCoord, yCoord);
			setNode.setCenter(coord);

			// ---- get the position for the sets identifying string on the panel
			int strX = xCoord + (int) (Math.cos(angleInRadians) * RADIUS / 1.25);
			int strY = yCoord + (int) (Math.sin(angleInRadians) * RADIUS / 1.25);
			Coordinate stringCoord = new Coordinate(strX, strY);
			setNode.setStringPosition(stringCoord);
			// ----

			// p = {(x,y) : (x-mX)^2 + (y-mY)^2 <= r^2}
			for (int x = xCoord - RADIUS; x < xCoord + RADIUS; x++) {
				for (int y = yCoord - RADIUS; y < yCoord + RADIUS; y++) {
					int xDifference = x - setNode.center().x(); // x-mX
					int yDifference = y - setNode.center().y(); // y-mY

					int value = xDifference * xDifference + yDifference * yDifference;
					int radiusSquared = RADIUS * RADIUS;

					// (x-mX)^2 + (y-mY)^2 <= r^2
					if (value <= radiusSquared) {
						Coordinate c = new Coordinate(x, y);
						setNode.addPixel(c);
					}
				}
			}
			setCount++;
		}
	}
}
