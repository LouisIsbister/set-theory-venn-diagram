package test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.junit.jupiter.api.Test;

import main.expressionparser.*;
import main.tree.BTSetNode;
import main.util.Coordinate;
import main.util.InvalidExpressionException;

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
            
            assertDoesNotThrow(() -> ExpressionParser.parse(expr));
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
        // a ∪ (b ∩ c)
        ExpressionTree tree = new ExpressionTree("a ∪ (b ∩ c)");
        Set<Coordinate> res = tree.execute();
        Collection<BTSetNode> leaves = tree.setNodes();
        final Set<Coordinate> A = Util.retrieveNodeByID(leaves, "a").evaluate();
        final Set<Coordinate> B = Util.retrieveNodeByID(leaves, "b").evaluate();
        final Set<Coordinate> C = Util.retrieveNodeByID(leaves, "c").evaluate();

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

    @Test
    public void testExpressionExecution2() throws InvalidExpressionException {
        // ~(a ∪ b)
        ExpressionTree tree = new ExpressionTree("~(a ∪ b)");
        Set<Coordinate> res = tree.execute();
        Collection<BTSetNode> leaves = tree.setNodes();
        final Set<Coordinate> A = Util.retrieveNodeByID(leaves, "a").evaluate();
        final Set<Coordinate> B = Util.retrieveNodeByID(leaves, "b").evaluate();

        for (Coordinate coord : res) {
            assertFalse(A.contains(coord) && B.contains(coord));
        }
    }

    @Test
    public void testExpressionExecution3() throws InvalidExpressionException {
        // ~a ∩ b
        ExpressionTree tree = new ExpressionTree("~a ∩ b");
        Set<Coordinate> res = tree.execute();
        Collection<BTSetNode> leaves = tree.setNodes();
        final Set<Coordinate> A = Util.retrieveNodeByID(leaves, "a").evaluate();
        final Set<Coordinate> B = Util.retrieveNodeByID(leaves, "b").evaluate();

        for (Coordinate coord : res) {
            assertTrue(!A.contains(coord));
            assertTrue(B.contains(coord));
        }
    }
}
