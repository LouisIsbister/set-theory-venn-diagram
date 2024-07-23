package test;

import java.util.Collection;
import java.util.Set;

import main.expressionparser.ExpressionTree;
import main.tree.BTSetNode;
import main.util.Coordinate;

public class Util {

    public static Set<Coordinate> retrieveSetNodeData(ExpressionTree tree, String id) {
        Collection<BTSetNode> leaves = tree.setNodes();
        return leaves.stream()
            .filter(e -> e.toString().equals(id))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new)
            .evaluate();
    }

}
