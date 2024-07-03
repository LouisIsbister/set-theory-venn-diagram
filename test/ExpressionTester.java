import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import stvd.expressionparser.ExpressionParser;
import stvd.util.InvalidExpressionException;


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
    public void testValidExpressions() {
        for (Map.Entry<String, List<String>> e : expectedParsingFormat.entrySet()) {
            String expr = e.getKey();
            List<String> expected = e.getValue();
            Queue<String> recieved = handleParsing(expr);
            assertNotEquals(null, recieved);
            assertEquals(expected.size(), recieved.size());
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i), recieved.poll());
            }
        }
    }

    @Test
    public void testInvalidExpression() {
        for (String expr : invalidExpressions) {
            assertThrows(InvalidExpressionException.class, () -> {
                ExpressionParser.parse(expr);
            });
        }
    }

    // --- helpers ---

    static Queue<String> handleParsing(String expr) {
        try {
            return ExpressionParser.parse(expr);
        } catch (InvalidExpressionException e) { return null; }
    }
}
