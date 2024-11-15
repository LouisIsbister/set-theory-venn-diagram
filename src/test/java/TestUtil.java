

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Queue;
import java.util.Set;

import org.junit.jupiter.api.function.Executable;

import com.stvd.nodes.BTSetNode;
import com.stvd.parsing.ExpressionTree;
import com.stvd.util.*;

public class TestUtil {

    /**
     * @param args elements to be added to the queue
     * @return a queue containing all the passed elements
     */
    public static final Queue<String> generateQueue(String... args) {
        return new ArrayDeque<>(Arrays.asList(args));
    }

    /**
     * retrieves the data of a given set node
     * 
     * @param tree the binary expression tree
     * @param id id of the set node
     * @return the data of the set 
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
     * check that an executable does not throw an ParserFailureException 
     * 
     * @param exec executable block
     */
    public static void assertDoesNotThrowParserException(Executable exec) {
        try {
            exec.execute();
        } catch (ParserFailureException ex) {
            assert false : "ATTENTION - ParserFailureException incorrectly thrown";
        } catch (Throwable e) {}
    }

}
