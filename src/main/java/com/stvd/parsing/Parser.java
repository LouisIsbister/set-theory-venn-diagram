package com.stvd.parsing;

import java.util.Queue;
import java.util.ArrayDeque;

import com.stvd.nodes.BTNode;
import com.stvd.nodes.Complement;
import com.stvd.nodes.Difference;
import com.stvd.nodes.Intersect;
import com.stvd.nodes.Union;
import com.stvd.util.ParserFailureException;
import com.stvd.util.Store;

public class Parser {

    /**
     * @param expr, the user provided expression
     * @return, the restructured expression
     * @throws ParserFailureException
     */
    public static Queue<String> parse(String expr) throws ParserFailureException {
        ExpressionValidator.checkCharacters(expr);
        ExpressionValidator.checkBracketFormatting(expr);

        Queue<String> polishExp = toPolishNotation(expr, new ArrayDeque<>());

        ExpressionValidator.checkIsExecutableExpression(polishExp);
        return polishExp;
    }

    /**
     * returns and instance of the operator that corresponds to
     * the given character.
     * 
     * @param str, the operator
     * @return, an instance of the operator
     */
    public static BTNode parseOperator(String str) {
        return switch (str) {
            case Store.UNION      -> new Union();
            case Store.INTERSECT  -> new Intersect();
            case Store.DIFFERENCE -> new Difference();
            default -> new Complement();   // default case is "~"
        };
    }

    /**
     * Converts the user provided expression into polish notation.
     * 
     * @param str the expression
     * @return the expression in polish notation
     */
    private static Queue<String> toPolishNotation(String str, Queue<String> queue) {
        if (str.matches("^$|\\(|\\)")) { // string is empty or is a bracket
            return queue;
        }

        int exprCenter = getCenterIndex(str); // the index to split the expression by
        char centerChar = str.charAt(exprCenter);
        if (centerChar != ')' && centerChar != '(') {
            queue.add(String.valueOf(centerChar));
        }

        String left = str.substring(0, exprCenter).trim();
        String right = str.substring(exprCenter + 1, str.length()).trim();

        toPolishNotation(left, queue);
        toPolishNotation(right, queue);

        return queue;
    }

    /**
     * Method that finds the "center" of an expression or sub-expression.
     * The center is the most recent operator that is contained within
     * balanced open and closed brackets.
     * i.e. a ∩ (b ∪ c)
     *        ^ 
     * 
     * @param str, the string that is being searched for an operator
     * @return, the center operator if it exists
     */
    private static int getCenterIndex(String str) {
        if (str.length() <= 1) {
            return 0;
        }
        int bracketBalance = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                continue;
            }

            char ch = str.charAt(i);
            bracketBalance += ch == '(' ? 1 : 0;
            bracketBalance += ch == ')' ? -1 : 0;

            // if an operator has been found with matching brackets, and is not a complement (should always be index 0)
            if (ExpressionValidator.isOperator(String.valueOf(ch)) && bracketBalance == 0 && ch != '~') {
                return i;
            }
        }
        return 0;
    }


    // Parser utils


    /**
     * Returns the execution representation of the provided expression,
     * giving the user insight into what is actually happening. For example,
     * a ∪ b ∩ c -> (a ∪ (b ∩ c))
     * 
     * @param expr, the user provided expression
     * @return, exec string string 
     * @throws ParserFailureException 
     */
    public static String getExecRepresentation(String expr) throws ParserFailureException {
        Queue<String> expression = Parser.parse(expr);
        return recursiveBuilder(expression);
    }

    /**
     * recursively rebuild the the original expression, adding
     * brackets to show precedence
     * 
     * @param expression, current/root node of subtree
     * @return execution string
     */
    private static String recursiveBuilder(Queue<String> expression) {
        if (expression.isEmpty()) {
            return "";
        }

        String elem = expression.poll();
        if (ExpressionValidator.isOperator(elem)) {
            String left = recursiveBuilder(expression);
            String right = new String();

            if (!elem.equals("~")) {    // complement should only have a left child node!
                right = recursiveBuilder(expression);
            } else {
                return String.format("%s%s", elem, left);
            }

            return String.format("(%s %s %s)", left, elem, right);
        }
        return elem;
    }

}
