package com.stvd.nodes;

import java.util.Set;
import java.util.stream.Collectors;

import com.stvd.util.*;

public class Intersect extends BTNode {

    /**
     * Returns all the values in left and right nodes.
     * 
     * @return the intersect of left and right nodes
     * @throws InvalidExpressionException 
     */
    @Override
    public Set<Coordinate> evaluate() throws InvalidExpressionException {
        Set<Coordinate> leftSet = left().evaluate();
        Set<Coordinate> rightSet = right().evaluate();
        return leftSet.stream()
                .filter(elem -> rightSet.contains(elem))
                .collect(Collectors.toSet());
    }

    public String toString() {
        return "\u2229";
    }

}
