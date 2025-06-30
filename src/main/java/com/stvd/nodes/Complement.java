package com.stvd.nodes;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.stvd.controller.AppPanel;
import com.stvd.util.Coordinate;
import com.stvd.util.Store;

public class Complement extends BTNode {

    /**
     * The universal set represents all the display pixels
     */
    private static final Set<Coordinate> universalSet = IntStream.range(0, AppPanel.WIDTH * AppPanel.HEIGHT).boxed()
            .map(e -> new Coordinate(e % AppPanel.WIDTH, e / AppPanel.HEIGHT))
            .collect(Collectors.toSet());

    /**
     * Returns all the values in the universal set
     * excluding the those in the left set.
     * 
     * @return the complement of the left node
     */
    @Override
    public Set<Coordinate> evaluate() {
        Set<Coordinate> leftSet = left().evaluate();
        return universalSet.stream()
                .filter(e -> !leftSet.contains(e))
                .collect(Collectors.toSet());
    }

    public String toString() {
        return Store.COMPLEMENT;
    }

}
