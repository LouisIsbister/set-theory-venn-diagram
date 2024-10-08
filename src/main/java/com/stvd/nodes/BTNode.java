package com.stvd.nodes;

import java.util.Set;

import com.stvd.util.Coordinate;

public abstract class BTNode {

    private BTNode left;
    
    private BTNode right;

    /**
     * @return the data of the expression
     */
    public abstract Set<Coordinate> evaluate();

    public BTNode left() {
        return left;
    }

    public void setLeft(BTNode left) {
        this.left = left;
    }

    public BTNode right() {
        return right;
    }

    public void setRight(BTNode right) {
        this.right = right;
    }
    
}
