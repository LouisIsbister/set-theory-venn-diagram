package code.expressions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import code.controller.AppPanel;
import code.operators.*;

public class ExprEvaluate {

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
	 * expression from the user
	 */
	private String expression;

	/**
	 * Map of all the sets in the expression
	 */
	private Map<String, SetNode> setNodes = new HashMap<>();

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

	public ExprEvaluate(String expr) throws InvalidExpressionException {
		root = null;
		expression = StructuredExpr.restructureExpression(expr);

		parseExpression();
		propagateSetNodes();
	}

	/**
	 * @return, the root node of the binary tree
	 */
	public BTNode root() {
		return root;
	}

	/**
	 * Retrieves all the set nodes (leaf nodes)
	 * 
	 * @return, a collection of set nodes
	 */
	public ArrayList<SetNode> setNodes() {
		return new ArrayList<SetNode>(setNodes.values());
	}

	/**
	 * This method intialises the creation of the tree structure.
	 * It first calls the recursive method to build the binary tree, it then checks
	 * for formatting
	 * errors, these cases are when the user has provided too many arguments, not
	 * enough arguments,
	 * or invalid set identifiers.
	 * It then calls the propagateSetNodes method that fills the sets with
	 * pixels/data before
	 * returning the root of the binary tree.
	 * 
	 * @param expression, the expression that is to be evaluated
	 *                    @return, the root node of binary tree representation of
	 *                    the expression
	 */
	public BTNode parseExpression() throws InvalidExpressionException {
		Scanner scan = new Scanner(expression);

		scan.useDelimiter("([\\(\s])");

		// recursively create the binary tree
		root = parseTree(scan);

		// user provided too many or no arguments
		if (scan.hasNext())
			throw new InvalidExpressionException("'Invalid format: too many arguments were provided.'");
		if (setNodes.size() < 1)
			throw new InvalidExpressionException("'Invalid expression: no arguments were provided.'");
		
		scan.close();
		return root;
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
	 */
	private BTNode parseTree(Scanner scan) {
		if (!scan.hasNext())
			return null;

		String next = scan.next().trim().toLowerCase();
		// if the an invalid set id was given 
		if (next.length() > 1) {
			next = next.length() > 10 ? next.substring(0, 10) + "..." : next;
			throw new IllegalArgumentException(
					"CLIENT ERROR:" +
					"<br>'" + next + "'<br>Is not a valid set id.");
		}

		boolean isSetNode = next.matches("[a-z]");

		// if an operator has been found
		if (!isSetNode) {
			Operator op = parseOperator(next.charAt(0));
			BTNode node = new BTNode(op);

			node.setLeft(parseTree(scan));

			// if the operator is a complement then the right node must be the universal set
			if (op instanceof Complement)
				node.setRight(universalSet);
			else
				node.setRight(parseTree(scan));

			return node;
		} 
		else {
			if (!setNodes.containsKey(next))
				setNodes.put(next, new SetNode(next));

			return setNodes.get(next);
		}
	}

	/**
	 * returns and instance of the operator that corresponds to
	 * the given character.
	 * 
	 * @param c, the operator
	 *           @return, an instance of the operator
	 */
	private Operator parseOperator(char c) {
		switch (c) {
			case '\u222A':
				return new Union();
			case '\u2229':
				return new Intersect();
			case '\\':
				return new Difference();
			case '~':
				return new Complement();
			default:
				throw new IllegalArgumentException(
						"4XX CLIENT ERROR:" +
						"<br>'" + c + "' is not a valid operator.");
		}
	}

	/**
	 * This method determines and initializes the central coordinate of a set.
	 * Then, every pixel within the sphere that represents a set is added to the
	 * collection of pixels in that set.
	 * Each pixel represents a piece of data within the set.
	 */
	private void propagateSetNodes() {
		int NUMBER_OF_SETS = setNodes.size();
		int RADIUS = SetNode.DIAMETER / 2;

		// the angle difference of each circle from the center
		double ANGLE_OFFSET = 360 / NUMBER_OF_SETS;

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
