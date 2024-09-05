

import java.util.Collection;
import java.util.Set;

import org.junit.jupiter.api.function.Executable;

import com.stvd.expressionparser.ExpressionTree;
import com.stvd.tree.BTSetNode;
import com.stvd.util.Coordinate;
import com.stvd.util.InvalidExpressionException;

public class Util {

    /**
     * retrieves the data of a given set node
     * 
     * @param tree, the binary expression tree
     * @param id, id of the set node
     * @return
     */
    public static Set<Coordinate> retrieveSetNodeData(ExpressionTree tree, String id) {
        Collection<BTSetNode> leaves = tree.setNodes();
        return leaves.stream()
                .filter(e -> e.toString().equals(id))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .evaluate();
    }

    /**
     * check that an executable does not throw an InvalidExpressionException 
     * 
     * @param exec, executable block
     */
    public static void assertDoesNotThrowExpressionException(Executable exec) {
        try {
            exec.execute();
        } catch (InvalidExpressionException actualException) {
            assert false : "ATTENTION - InvalidExpressionException incorrectly thrown";
        } catch (Throwable e) {}
    }

}
