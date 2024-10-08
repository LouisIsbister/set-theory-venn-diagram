

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.stvd.parsing.*;
import com.stvd.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExpressionTester {

    private static Map<String, List<String>> expectedParsingFormat;
    private static List<String> badBracketExpressions;
    private static List<String> badCharacterExpressions;
    private static List<Queue<String>> unexecutableExpressions;
     
    static {
        expectedParsingFormat = Map.of(
            "a ∪ (b ∩ c)", List.of("∪","a","∩","b","c"),
            "(a ∩ b) ∪ (c ∩ d)", List.of("∪","∩","a","b","∩","c","d"),
            "~(a \\ b)", List.of("~","\\","a","b"),
            "a ∩ b ∩ ~(c ∪ d)", List.of("∩","a","∩","b","~","∪","c","d"),
            "((a ∩ b) ∪ c) ∪ d", List.of("∪","∪","∩","a","b","c","d")
        );

        badBracketExpressions = List.of(
            "(a ∪ (b ∩ c)", "a ∪ () (b ∩ c)", "a)",
            "(((a ∪ (b ∩ c)))"
        );

        badCharacterExpressions = List.of(
            "a ∪ (b ∩ cc)",  "a ∪ (b + c)", "a - b",
            "a \t b"
        );

        unexecutableExpressions = List.of(
            Util.generateQueue("a", "∪","∩"),
            Util.generateQueue("∪", "b", "∩", "c"),
            Util.generateQueue("~", "a", "a"),
            Util.generateQueue("a", "\\", "b",  "~", "c"), 
            Util.generateQueue("∪", "~", "a")
        );
    }

    
    
    @Test
    public void testValidExpressionParsing() throws ParserFailureException {
        for (String expr : expectedParsingFormat.keySet()) {
            List<String> expected = expectedParsingFormat.get(expr);
            
            assertDoesNotThrow(() -> Parser.parse(expr));
            Queue<String> recieved = Parser.parse(expr);
            assertEquals(expected.size(), recieved.size());
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i), recieved.poll());
            }
        }
    }

    @Test
    public void testInvalidBracketFormatting() {
        for (String expr : badBracketExpressions) {
            assertThrows(ParserFailureException.class, () -> {
                ExpressionValidator.checkBracketFormatting(expr);
            });
        }
    }

    @Test
    public void testInvalidCharacters() throws NoSuchMethodException, SecurityException {
        for (String expr : badCharacterExpressions) {
            assertThrows(ParserFailureException.class, () -> {
                ExpressionValidator.checkCharacters(expr);
            });
        }
    }

    @Test
    public void testUnexecutableExpressions() throws NoSuchMethodException, SecurityException {
        for (Queue<String> expr : unexecutableExpressions) {
            assertThrows(ParserFailureException.class, () -> {
                ExpressionValidator.checkIsExecutableExpression(expr);
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
