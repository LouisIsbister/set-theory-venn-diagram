package code.expressions;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import code.util.InvalidExpressionException;

public class ExpressionParser {

    private static Queue<Character> expression;

    /**
     * @param expr, the user provided expression
     * @return, the restructured expression
     * @throws InvalidExpressionException
     */
    public static Queue<Character> parse(String expr) throws InvalidExpressionException {
        validateExpression(expr);

        expression = new ArrayDeque<>();
        return restructure(expr);
    }

    /**
     * convert the user provided expression into cambridge polish notation.
     * for every character in the expression, if the character is not a valid
     * set identifier, i.e. [a-zA-Z].
     * 
     * @throws InvalidExpressionException
     */
    private static Queue<Character> restructure(String str) throws InvalidExpressionException {
        str = str.trim();
        if (str.equals("(") || str.equals(")"))
            return expression;

        int exprCenter = getCenterIndex(str);
        char centerChar = str.charAt(exprCenter);
        String left = str.substring(0, exprCenter).trim();
        String right = str.substring(exprCenter + 1, str.length()).trim();
        
        if (centerChar != ')' && centerChar != '(')
            expression.add(centerChar);
        
        if (left.length() > 0)
            restructure(left);
        if (right.length() > 0)
            restructure(right);

        return expression;
    }

    /**
     * Method that finds the "center" of an expression or sub-expression.
     * The center is the most recent operator that is contained within
     * matching open and closed brackets.
     * i.e. a ∩ (b ∪ c)
     *        ^ is the center of this expression as the brackets
     *          are matching, i.e. there are none
     * 
     * @param str, the string that is being searched for an operator
     * @return, the index of the operator if one is found
     * @throws InvalidExpressionException
     */
    private static int getCenterIndex(String str) throws InvalidExpressionException {
        if (str.length() <= 1)
            return 0;

        int openBracketCount = 0;
        int closedBracketCount = 0;
        int minDifference = Integer.MAX_VALUE;
        int ret = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ')
                continue;

            char c = str.charAt(i);
            if (c == '(')
                openBracketCount++;
            else if (c == ')')
                closedBracketCount++;

            // if an operator has been found
            if (!(c + "").matches("[a-zA-Z]") && !(c + "").matches("[\\(|\\)]")) {
                if (openBracketCount - closedBracketCount < minDifference) {
                    minDifference = openBracketCount - closedBracketCount;
                    ret = i;
                }
            }
        }
        // if no center was found then their is either a too many open brackets
        // or there is operator
        return ret;
    }

    /**
     * Check that all the set ids are no longer than one letter. 
     * Check that the open and closed bracket counts are equal.
     * 
     * @param str, the provided expression
     * @throws InvalidExpressionException
     */
    private static void validateExpression(String str) throws InvalidExpressionException {
        // check that there are not set ids with 2 or more characters
        Pattern pattern = Pattern.compile("[a-zA-Z]{2,}");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            String s = matcher.group();
            String ret = s.length() > 10 ? s.substring(0, 10) + "..." : s;
            throw new InvalidExpressionException("Set ids must be one letter.<br>Found '" + ret + "'");
        }

        // check bracket formatting
        int balance = 0;
        int unbalancedIdx = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            char ch = i == str.length() - 1 ? ' ' : str.charAt(i + 1);
            if (c == '(')
                balance++;
            else if (c == ')')
                balance--;
  
            if (balance != 0 && (c == ')' || c == '('))
                unbalancedIdx = i;

            // if there are recurring brackets that contains nothing, i.e. a ∩ () b
            if ((c == '(' && ch == ')') || (ch == '(' && c == ')'))
                throw new InvalidExpressionException("Brackets must contain an expression.");
        }

        // if the number of open brackets != number of closed brackets
        if (balance != 0) {
            String errorMsg = formatBracketException(str, balance, unbalancedIdx);
            throw new InvalidExpressionException(errorMsg);
        }
    }

    /**
     * Creates a formatted string that points to where the bracket imbalance occurs
     * 
     * @param str, the string that caused the error
     * @return, the formatted string
     */
    private static String formatBracketException(String expr, int balance, int errorIdx) {
        String insrt = balance < 0 ? "open" : "closed";
        String ret = "Found unmatched '"+ insrt + "' bracket.<br>" + expr + "<br>";
        ret += "_".repeat(errorIdx) + "^" + "_".repeat(expr.length() - errorIdx);
        return ret;
    }
}
