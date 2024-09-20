

import org.junit.jupiter.api.function.Executable;

import com.stvd.expressionparsing.ExpressionTree;
import com.stvd.nodes.BTSetNode;
import com.stvd.util.*;

public class Util {

    /**
     * retrieves the data of a given set node
     * 
     * @param tree, the binary expression tree
     * @param id, id of the set node
     * @return
     */
    public static java.util.Set<Coordinate> retrieveSetNodeData(ExpressionTree tree, String id) {
        java.util.Collection<BTSetNode> leaves = tree.setNodes();
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
    public static void assertDoesNotThrowParserException(Executable exec) {
        try {
            exec.execute();
        } catch (ParserFailureException ex) {
            assert false : "ATTENTION - ParserFailureException incorrectly thrown";
        } catch (Throwable e) {}
    }

}
