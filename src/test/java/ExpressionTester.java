

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.stvd.expressionparser.*;
import com.stvd.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExpressionTester {

    private static Map<String, List<String>> expectedParsingFormat = Map.of(
        "a ∪ (b ∩ c)", List.of("∪","a","∩","b","c"),
        "(a ∩ b) ∪ (c ∩ d)", List.of("∪","∩","a","b","∩","c","d"),
        "~(a \\ b)", List.of("~","\\","a","b"),
        "a ∩ b ∩ ~(c ∪ d)", List.of("∩","a","∩","b","~","∪","c","d"),
        "((a ∩ b) ∪ c) ∪ d", List.of("∪","∪","∩","a","b","c","d")
    );
    private static List<String> invalidExpressions = List.of(
        "(a ∪ (b ∩ c)", "a ∪ () (b ∩ c)", "a ∪ (b ∩ cc)",
         "a ∪ (b + c)"
    );
    
    @Test
    public void testValidExpressionParsing() throws InvalidExpressionException {
        for (Map.Entry<String, List<String>> e : expectedParsingFormat.entrySet()) {
            String expr = e.getKey();
            List<String> expected = e.getValue();

            Util.assertDoesNotThrowExpressionException(() -> ExpressionParser.parse(expr));
            Queue<String> recieved = ExpressionParser.parse(expr);
            assertEquals(expected.size(), recieved.size());
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i), recieved.poll());
            }
        }
    }

    @Test
    public void testInvalidExpressionParsing() {
        for (String expr : invalidExpressions) {
            assertThrows(InvalidExpressionException.class, () -> {
                ExpressionParser.parse(expr);
            });
        }
    }

    @Test
    public void testExpressionExecution1() throws InvalidExpressionException {
        // a ∩ b
        ExpressionTree tree = new ExpressionTree("a ∩ b");
        final Set<Coordinate> A = Util.retrieveSetNodeData(tree, "a");
        final Set<Coordinate> B = Util.retrieveSetNodeData(tree, "b");

        Set<Coordinate> res = tree.execute();
        for (Coordinate coord : res) {
            assertTrue(A.contains(coord));
            assertTrue(B.contains(coord));
        }
    }

    @Test
    public void testExpressionExecution2() throws InvalidExpressionException {
        // a \ (b ∪ c)
        ExpressionTree tree = new ExpressionTree("a \\ (b ∪ c)");
        final Set<Coordinate> A = Util.retrieveSetNodeData(tree, "a");
        final Set<Coordinate> B = Util.retrieveSetNodeData(tree, "b");
        final Set<Coordinate> C = Util.retrieveSetNodeData(tree, "c");

        Set<Coordinate> res = tree.execute();
        for (Coordinate coord : res) {
            assertTrue(A.contains(coord));
            assertTrue(!B.contains(coord));
            assertTrue(!C.contains(coord));
        }
    }

    @Test
    public void testExpressionExecution3() throws InvalidExpressionException {
        // ~(a ∪ b)
        ExpressionTree tree = new ExpressionTree("~(a ∪ b)");
        final Set<Coordinate> A = Util.retrieveSetNodeData(tree, "a");
        final Set<Coordinate> B = Util.retrieveSetNodeData(tree, "b");

        Set<Coordinate> res = tree.execute();
        for (Coordinate coord : res) {
            assertTrue(!A.contains(coord));
            assertTrue(!B.contains(coord));
        }
    }

    @Test
    public void testExpressionExecution4() throws InvalidExpressionException {
        // ~a ∩ b
        ExpressionTree tree = new ExpressionTree("~a ∩ b");
        final Set<Coordinate> A = Util.retrieveSetNodeData(tree, "a");
        final Set<Coordinate> B = Util.retrieveSetNodeData(tree, "b");

        Set<Coordinate> res = tree.execute();
        for (Coordinate coord : res) {
            assertTrue(!A.contains(coord));
            assertTrue(B.contains(coord));
        }
    }

    @Test
    public void testExpressionExecution5() throws InvalidExpressionException {
        // a ∩ b ∩ ~(c ∪ d)
        ExpressionTree tree = new ExpressionTree("a ∩ b ∩ ~(c ∪ d)");
        final Set<Coordinate> A = Util.retrieveSetNodeData(tree, "a");
        final Set<Coordinate> B = Util.retrieveSetNodeData(tree, "b");
        final Set<Coordinate> C = Util.retrieveSetNodeData(tree, "c");
        final Set<Coordinate> D = Util.retrieveSetNodeData(tree, "d");

        Set<Coordinate> res = tree.execute();
        for (Coordinate coord : res) {
            assertTrue(A.contains(coord));
            assertTrue(B.contains(coord));
            assertTrue(!C.contains(coord));
            assertTrue(!D.contains(coord));
        }
    }

    @Test
    public void testExpressionExecution6() throws InvalidExpressionException {
        // a ∪ (b ∩ c)
        ExpressionTree tree = new ExpressionTree("a ∪ (b ∩ c)");
        final Set<Coordinate> A = Util.retrieveSetNodeData(tree, "a");
        final Set<Coordinate> B = Util.retrieveSetNodeData(tree, "b");
        final Set<Coordinate> C = Util.retrieveSetNodeData(tree, "c");

        Set<Coordinate> res = tree.execute();
        for (Coordinate coord : res) {
            if (!A.contains(coord)) {
                assertTrue(B.contains(coord) && C.contains(coord));
            } 
            if (!B.contains(coord) && !C.contains(coord)) {
                assertTrue(A.contains(coord));
            }
            assertTrue(A.contains(coord) || B.contains(coord) || C.contains(coord));
        }
    }
}
