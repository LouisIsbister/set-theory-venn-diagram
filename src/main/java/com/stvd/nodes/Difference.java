package com.stvd.nodes;

import java.util.Set;
import java.util.stream.Collectors;

import com.stvd.util.*;

public class Difference extends BTNode {

    /**
     * Returns all the values in left that are not
     * in right.
     * 
     * @return the difference between left and right nodes
     * @throws InvalidExpressionException
     */
    @Override
    public Set<Coordinate> evaluate() throws InvalidExpressionException {
        if (left() == null || right() == null) {
            throw new InvalidExpressionException("Failed to Execute<br>" + toString() + " is missing an arg.");
        }

        Set<Coordinate> leftSet = left().evaluate();
        Set<Coordinate> rightSet = right().evaluate();
        return leftSet.stream()
                .filter(elem -> !rightSet.contains(elem))
                .collect(Collectors.toSet());
    }

    public String toString() {
        return "\\";
    }

}
