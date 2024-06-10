package stvd.expressionparser;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import stvd.util.InvalidExpressionException;

public class ExpressionParser {

    private static Queue<Character> expression;

    /**
     * @param expr, the user provided expression
     * @return, the restructured expression
     * @throws InvalidExpressionException
     */
    public static Queue<Character> parse(String expr) throws InvalidExpressionException {  
        expression = new ArrayDeque<>();
        validateExpression(expr);
        
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
            restructure(left);
        }
        if (right.length() > 0) {
            restructure(right);
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
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                continue;
            }

            char ch = str.charAt(i);
            openBracketCount += ch == '(' ? 1 : 0;
            closedBracketCount += ch == ')' ? 1 : 0;

            // if an operator has been found with matching brackets
            if (BTParser.charIsOperator(ch) && openBracketCount == closedBracketCount) {
                return i;
            }
        }
        return 0;
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

        char[] arr = str.toCharArray();
        int balance = 0, unbalancedIdx = 0;    // bracket balance and possible bad balance index
        char currentCh = arr[0];
        for (int i = 0; i < arr.length; i++) {
            char nextCh = i == arr.length - 1 ? ' ' : arr[i + 1];
            
            balance += arr[i] == '(' ? 1 : 0;
            balance += arr[i] == ')' ? -1 : 0;
  
            // -- used for formatting error msg when brackets are unblanced -- 
            if (balance != 0 && (currentCh == '(' || currentCh == ')')) {
                unbalancedIdx = i;
            }

            // if there are recurring brackets that contains nothing, i.e. a ∩ () b
            if ((currentCh == '(' && nextCh == ')') || (nextCh == '(' && currentCh == ')')) {
                throwBracketException("Brackets must contain an expression.", str, balance, unbalancedIdx);
            }

            // if an oeprator immediately follows another an isn't ~
            if (BTParser.charIsOperator(currentCh) && BTParser.charIsOperator(nextCh) && nextCh != '~') {
                throw new InvalidExpressionException("Operator: " + nextCh + " cannot immediately<br>follow " + currentCh);
            }

            // update the current character to point at the latest valid part of the expression 
            if (nextCh != ' ') {
                currentCh = nextCh;
            }
        }

        // if the number of open brackets != number of closed brackets
        if (balance != 0) {
            throwBracketException("Found unmatched brackets.", str, balance, unbalancedIdx);
        }
    }

    /**
     * Creates a formatted string that points to where the bracket imbalance occurs
     * 
     * @param str, the string that caused the error
     * @return, the formatted string
     * @throws InvalidExpressionException 
     */
    private static void throwBracketException(String msg, String expr, int balance, int errorIdx) throws InvalidExpressionException {
        String ret = msg + "<br>" + expr + "<br>";
        ret += "_".repeat(errorIdx) + "^" + "_".repeat(expr.length() - errorIdx);
        throw new InvalidExpressionException(ret);
    }
}
