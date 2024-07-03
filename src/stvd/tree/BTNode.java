package src.stvd.tree;

import java.util.Set;

import src.stvd.util.*;

public abstract class BTNode {

    private BTNode left;
    
    private BTNode right;

    /**
     * @return
     */
    public abstract Set<Coordinate> evaluate() throws InvalidExpressionException ;

    // -- getters and setters for the children nodes ---

    public void setLeft(BTNode left) {
        this.left = left;
    }

    public void setRight(BTNode right) {
        this.right = right;
    }

    public BTNode left() {
        return left;
    }

    public BTNode right() {
        return right;
    }

}
