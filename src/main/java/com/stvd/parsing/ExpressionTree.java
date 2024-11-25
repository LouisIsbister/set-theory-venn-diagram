package com.stvd.parsing;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.stvd.controller.AppPanel;
import com.stvd.nodes.*;
import com.stvd.util.*;

public class ExpressionTree {

    public final String EXPR_STRING; 

    /**
     * root node of the binary tree
     */
    private BTNode root;

    /**
     * expression in Polish Notation form
     */
    private Queue<String> expression;

    /**
     * Map of all the sets in the expression
     */
    private Map<String, BTSetNode> setNodes = new HashMap<>();

    public ExpressionTree(String expr) throws ParserFailureException {
        EXPR_STRING = expr;
        expression = Parser.parse(expr);
        root = generateTree();
        propagateSetNodes();
    }

    /**
     * @return the resulting set of data when executing the expression
     */
    public Set<Coordinate> execute() {
        return root.evaluate();
    }

    /**
     * @return, the collection of set nodes
     */
    public Collection<BTSetNode> setNodes() {
        return setNodes.values();
    }

    /**
     * This method recursively builds the binary tree of the expression.
     * It retrieves the next element from the queue and checks whether it
     * is an operator. If so, create a new parent node and set its left and right
     * children.
     * Otherwise, find or create a new set node (leaf) and return it.
     * 
     * @return, the root node of the binary tree
     */
    private BTNode generateTree() {
        if (expression.isEmpty()) {
            return null;
        }

        String next = expression.poll();
        if (ExpressionValidator.isOperator(next)) {
            BTNode node = Parser.parseOperator(next);
            node.setLeft(generateTree());

            // if the operator is not a complement then set the right node
            if (!(node instanceof Complement)) {
                node.setRight(generateTree());
            }
            return node;
        } else {    // found a set/leaf node
            next = next.toLowerCase();
            if (!setNodes.containsKey(next)) {
                setNodes.put(next, new BTSetNode(next));
            }
            return setNodes.get(next);
        }
    }

    /**
     * Given two expression trees, check if they are equivalent, i.e. they 
     * contain the same data points/coordinates when evaulated
     * 
     * @param tree1
     * @param tree2
     * @return whether the trees are equivalent
     */
    public static boolean areEqual(ExpressionTree tree1, ExpressionTree tree2) {
        Set<Coordinate> t1Coords = tree1.execute();
        Set<Coordinate> t2Coords = tree2.execute();
        return t1Coords.containsAll(t2Coords) && t2Coords.containsAll(t1Coords);
    }

    /**
     * This method determines and initializes the central coordinate of a set.
     * Then, every pixel within the sphere that represents a set is added to the
     * collection of pixels in that set.
     * Each pixel represents a piece of data within the set.
     */
    private void propagateSetNodes() {
        final int START_X = AppPanel.WIDTH / 2;
        final int START_Y = AppPanel.HEIGHT / 2;

        // the angle difference of each circle from the center
        final double ANGLE_OFFSET = 360 / setNodes.size();
        final int RADIUS = BTSetNode.DIAMETER / 2;

        int setCount = 0;
        for (BTSetNode setNode : setNodes.values()) {
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
