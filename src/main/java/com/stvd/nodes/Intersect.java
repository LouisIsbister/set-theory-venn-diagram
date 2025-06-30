package com.stvd.nodes;

import java.util.Set;
import java.util.stream.Collectors;

import com.stvd.util.Coordinate;
import com.stvd.util.AppUtil;

public class Intersect extends BTNode {

    /**
     * Returns all the values in left and right nodes.
     * 
     * @return the intersect of left and right nodes
     */
    @Override
    public Set<Coordinate> evaluate() {
        Set<Coordinate> leftSet = left().evaluate();
        Set<Coordinate> rightSet = right().evaluate();
        return leftSet.stream()
                .filter(elem -> rightSet.contains(elem))
                .collect(Collectors.toSet());
    }

    public String toString() {
        return AppUtil.INTERSECT;
    }

}
