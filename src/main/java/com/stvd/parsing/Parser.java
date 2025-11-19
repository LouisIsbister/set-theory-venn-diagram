package com.stvd.parsing;

import java.util.Queue;
import java.util.ArrayDeque;

import com.stvd.util.ParserFailureException;
import com.stvd.expressiontree.BTNode;
import com.stvd.expressiontree.Complement;
import com.stvd.expressiontree.Difference;
import com.stvd.expressiontree.Intersect;
import com.stvd.expressiontree.Union;
import com.stvd.util.AppUtil;


public class Parser {


    /**
     * @param expr, the user provided expression
     * @return, the restructured expression
     * @throws ParserFailureException
     */
    public static Queue<String> parse(String expr) throws ParserFailureException {
        expr = removeWhiteSpace(expr);

        ExpressionValidator.checkCharacterSequences(expr);
        ExpressionValidator.checkBracketBalance(expr);

        Queue<String> polishExp = toPolishNotation(expr, new ArrayDeque<>());

        ExpressionValidator.checkIsExecutableExpression(polishExp);
        return polishExp;
    }

    private static String removeWhiteSpace(String expr) {
        StringBuilder sb = new StringBuilder();
        for (char c : expr.toCharArray()) {
            if (c != ' ')
                sb.append(c);
        }
        return sb.toString();
    }


    /**
     * returns and instance of the operator that corresponds to
     * the given character.
     * 
     * @param str, the operator
     * @return, an instance of the operator
     * @throws ParserFailureException 
     */
    public static BTNode parseOperator(String str) throws ParserFailureException {
        return switch (str) {
            case AppUtil.UNION      -> new Union();
            case AppUtil.INTERSECT  -> new Intersect();
            case AppUtil.DIFFERENCE -> new Difference();
            case AppUtil.COMPLEMENT -> new Complement();
            default -> throw new ParserFailureException("[BUG] Condition should never be reached.");
        };
    }


    /**
     * Converts the user provided expression into polish notation.
     * 
     * @param str the expression
     * @return the expression in polish notation
     */
    private static Queue<String> toPolishNotation(String str, Queue<String> queue) {
        if (str.isEmpty())
            return queue;

        // strip preceding and trailing brackets so long as it maintains bracket balance
        while (str.charAt(0) == '(' && str.charAt(str.length() - 1) == ')' &&
                ExpressionValidator.computeBracketBalance(str.substring(1, str.length()-1)).balance() == 0)
            str = str.substring(1, str.length() - 1);

        int splitIdx = getSplitIndex(str);
        if (splitIdx == -1) {
            queue.add(str);
            return queue;
        }
    
        queue.add(String.valueOf(str.charAt(splitIdx)));
        
        String left = str.substring(0, splitIdx);
        String right = str.substring(splitIdx + 1, str.length());

        toPolishNotation(left, queue);
        toPolishNotation(right, queue);

        return queue;
    }



    /**
     * Method that finds the split point of an expression or sub-expression. This point is 
     * defined as the most recent binary operator that is contained within balanced brackets, 
     * or, unary operator at the front of a compound expression.
     * 
     * e.g. a ∩ (b ∪ c)
     *        ^
     * e.g. (a ∩ (b \ c)) ∪ c
     *                    ^
     * e.g. ~a
     *      ^
     * e.g. ~(a \ b)
     *      ^
     * 
     * @param str, the string that is being searched for an operator
     * @return, the center operator if it exists
     */
    private static int getSplitIndex(String str) {
        if (str.length() <= 1)
            return -1;

        int bracketBalance = 0;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            bracketBalance += ch == '(' ? 1 : 0;
            bracketBalance += ch == ')' ? -1 : 0;

            if (ExpressionValidator.isBinaryOperator(String.valueOf(ch)) && bracketBalance == 0)
                return i;
        }

        // at this point we know any binary operators in the expression are enclosed in brackets,
        // as such, given the expressions are always stripped of brackets and |`str`| is > 1 
        // then we so we have a unary expression.

        assert str.length() >= 2 && ExpressionValidator.isUnaryOperator(str.charAt(0) + "") : str;
        return 0;
    }


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
        if (expression.isEmpty())
            return "";

        String elem = expression.poll();
        if (ExpressionValidator.isOperator(elem)) {
            String left = recursiveBuilder(expression);

            if (ExpressionValidator.isUnaryOperator(elem))
                return String.format("%s%s", elem, left);

            String right = recursiveBuilder(expression);
            return String.format("(%s %s %s)", left, elem, right);
        }
        return elem;
    }

}
