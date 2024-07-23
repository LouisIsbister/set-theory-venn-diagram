package test;

import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.junit.jupiter.api.Test;

import main.expressionparser.*;
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
    public void testValidExpressions() throws InvalidExpressionException {
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
    public void testInvalidExpression() {
        for (String expr : invalidExpressions) {
            assertThrows(InvalidExpressionException.class, () -> {
                ExpressionParser.parse(expr);
            });
        }
    }

    @Test
    public void testExpression1() throws InvalidExpressionException {
        // a ∪ (b ∩ c)
        ExpressionTree tree = new ExpressionTree("a ∪ (b ∩ c)");
    }
}
