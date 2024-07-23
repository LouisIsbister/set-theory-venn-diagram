package test;

import java.util.Collection;

import main.tree.BTSetNode;

public class Util {

    public static BTSetNode retrieveNodeByID(Collection<BTSetNode> leaves, String id) {
        return leaves.stream()
            .filter(e -> e.toString().equals(id))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
    
}
