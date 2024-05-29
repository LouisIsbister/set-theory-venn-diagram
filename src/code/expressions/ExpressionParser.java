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
     * 
     * @throws InvalidExpressionException
     */
    private static Queue<Character> restructure(String str) throws InvalidExpressionException {
        if (str.equals("(") || str.equals(")")) {
            return expression;
        }

        int exprCenter = getCenterIndex(str);    // the index to split the expression by
        char centerChar = str.charAt(exprCenter);
        if (centerChar != ')' && centerChar != '(') {
            expression.add(centerChar);
        }

        String left = str.substring(0, exprCenter).trim();
        String right = str.substring(exprCenter + 1, str.length()).trim();
                
        if (left.length() > 0) {
            restructure(left.trim());
        }
        if (right.length() > 0) {
            restructure(right.trim());
        }

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
        if (str.length() <= 1) {
            return 0;
        }

        int openBracketCount = 0, closedBracketCount = 0;
        int center = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                continue;
            }

            char ch = str.charAt(i);
            openBracketCount += ch == '(' ? 1 : 0;
            closedBracketCount += ch == ')' ? 1 : 0;

            // if an operator has been found
            if (BTParser.charIsOperator(ch) && openBracketCount == closedBracketCount) {
                center = i;
            }
        }
        return center;
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
