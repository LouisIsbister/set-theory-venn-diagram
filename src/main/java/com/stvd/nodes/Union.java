package com.stvd.nodes;

import java.util.HashSet;
import java.util.Set;

import com.stvd.util.*;

public class Union extends BTNode {

    /**
     * Returns all the values in left and all the values in
     * right.
     * 
     * @return the set union between of two nodes
     * @throws InvalidExpressionException
     */
    @Override
    public Set<Coordinate> evaluate() throws InvalidExpressionException {
        if (left() == null || right() == null) {
            throw new InvalidExpressionException("Failed to Execute<br>" + toString() + " is missing an arg.");
        }

        Set<Coordinate> allCoords = new HashSet<>();
        allCoords.addAll(left().evaluate());
        allCoords.addAll(right().evaluate());
        return allCoords;
    }

    public String toString() {
        return "\u222A";
    }

}
