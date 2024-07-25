

import java.util.Collection;
import java.util.Set;

import com.stvd.expressionparser.ExpressionTree;
import com.stvd.tree.BTSetNode;
import com.stvd.util.Coordinate;

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
