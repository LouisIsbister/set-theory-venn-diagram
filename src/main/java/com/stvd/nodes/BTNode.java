package com.stvd.nodes;

import java.util.Set;

import com.stvd.util.*;

public abstract class BTNode {

    private BTNode left;
    
    private BTNode right;

    /**
     * @return the data of the expression
     * 
     * @throws InvalidExpressionException
     */
    public abstract Set<Coordinate> evaluate() throws InvalidExpressionException;

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
