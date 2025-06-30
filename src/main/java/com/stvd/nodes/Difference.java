package com.stvd.nodes;

import java.util.Set;
import java.util.stream.Collectors;

import com.stvd.util.Coordinate;
import com.stvd.util.Store;

public class Difference extends BTNode {

    /**
     * Returns all the values in left that are not
     * in right.
     * 
     * @return the difference between left and right nodes
     */
    @Override
    public Set<Coordinate> evaluate() {
        Set<Coordinate> leftSet = left().evaluate();
        Set<Coordinate> rightSet = right().evaluate();
        return leftSet.stream()
                .filter(elem -> !rightSet.contains(elem))
                .collect(Collectors.toSet());
    }

    public String toString() {
        return Store.DIFFERENCE;
    }

}
