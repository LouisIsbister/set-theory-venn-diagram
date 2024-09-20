

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.stvd.expressionparsing.*;
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
    
    /**
     * invalid expressions, these are invalid due to either unbalanced brackets, 
     * enclosed brackets with no contents, invalid set ids, or invalid operators.
     */
    private static List<String> invalidExpression1 = List.of(
        "(a ∪ (b ∩ c)", "a ∪ () (b ∩ c)", "a ∪ (b ∩ cc)",
         "a ∪ (b + c)", "a - b", ""
    );

    /**
     * some more invalid expressions, each of these expressions is testing the
     * checkIsExecutableExpression() method in ExpressionParser.java
     */
    private static List<String> invalidExpression2 = List.of(
        "a ∪ (b ∩ )", "a ∪ b ∩ c)", "∪ b ∩ c",
         "~a a", "a \\ b ~c", "∪ ~a"
    );
    
    @Test
    public void testValidExpressionParsing() throws ParserFailureException {
        for (Map.Entry<String, List<String>> e : expectedParsingFormat.entrySet()) {
            String expr = e.getKey();
            List<String> expected = e.getValue();

            Util.assertDoesNotThrowParserException(() -> ExpressionParser.parse(expr));
            java.util.Queue<String> recieved = ExpressionParser.parse(expr);
            assertEquals(expected.size(), recieved.size());
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i), recieved.poll());
            }
        }
    }

    @Test
    public void testInvalidExpressionParsing1() {
        for (String expr : invalidExpression1) {
            assertThrows(ParserFailureException.class, () -> {
                ExpressionParser.parse(expr);
            });
        }
    }

    @Test
    public void testInvalidExpressionParsing2() {
        for (String expr : invalidExpression2) {
            assertThrows(ParserFailureException.class, () -> {
                ExpressionParser.parse(expr);
            });
        }
    }

    @Test
    public void testExpressionExecution1() throws ParserFailureException {
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
    public void testExpressionExecution2() throws ParserFailureException {
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
    public void testExpressionExecution3() throws ParserFailureException {
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
    public void testExpressionExecution4() throws ParserFailureException {
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
    public void testExpressionExecution5() throws ParserFailureException {
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
    public void testExpressionExecution6() throws ParserFailureException {
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
