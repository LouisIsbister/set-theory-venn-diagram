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
     */
    @Override
    public Set<Coordinate> evaluate() {
        Set<Coordinate> allCoords = new HashSet<>();
        allCoords.addAll(left().evaluate());
        allCoords.addAll(right().evaluate());
        return allCoords;
    }

    public String toString() {
        return "\u222A";
    }

}
