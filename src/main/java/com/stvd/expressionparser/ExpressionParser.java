package com.stvd.expressionparser;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stvd.util.InvalidExpressionException;

public class ExpressionParser {

    private static Queue<String> expression;

    /**
     * @param expr, the user provided expression
     * @return, the restructured expression
     * @throws InvalidExpressionException
     */
    public static Queue<String> parse(String expr) throws InvalidExpressionException {
        validateExpressionCharacters(expr);
        validateBracketFormatting(expr);

        expression = new ArrayDeque<>();
        return restructure(expr);
    }

    /**
	 * @param value
	 * @return whether a an operator has been given
	 */
	protected static boolean isOperator(String value) {
		return value.matches("[\u222A|\u2229|\\\\|~]");
	}

    /**
     * Converts the user provided expression into cambridge polish notation.
     * 
     * @param str the expression
     * @return queue of characters in CPN
     * @throws InvalidExpressionException
     */
    private static Queue<String> restructure(String str) throws InvalidExpressionException {
        if (str.matches("^$|\\(|\\)")) {    // string is empty or is a bracket
            return expression;
        }

        int exprCenter = getCenterIndex(str); // the index to split the expression by
        char centerChar = str.charAt(exprCenter);
        if (centerChar != ')' && centerChar != '(') {
            expression.add(String.valueOf(centerChar));
        }

        String left = str.substring(0, exprCenter).trim();
        String right = str.substring(exprCenter + 1, str.length()).trim();

        restructure(left);
        restructure(right);

        return expression;
    }

    /**
     * Method that finds the "center" of an expression or sub-expression.
     * The center is the most recent operator that is contained within
     * matching open and closed brackets.
     * i.e. a ∩ (b ∪ c)
     *        ^ 
     * 
     * @param str, the string that is being searched for an operator
     * @return, the center operator if it exists
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

            // if an operator has been found with matching brackets, and is not a complement (should always be index 0)
            if (isOperator(String.valueOf(ch)) && openBracketCount == closedBracketCount && ch != '~') {
                return i;
            }
        }
        return 0;
    }

    private static void validateExpressionCharacters(String expr) throws InvalidExpressionException {
        // check that there are not set ids with 2 or more characters
        Pattern pattern = Pattern.compile("[a-zA-Z]{2,}");
        Matcher matcher = pattern.matcher(expr);
        if (matcher.find()) {
            String s = matcher.group();
            String ret = s.length() > 10 ? s.substring(0, 10) + "..." : s;
            throw new InvalidExpressionException("Set ids must be one letter.<br>Found '" + ret + "'");
        }

        pattern = Pattern.compile("[a-zA-Z|\\(|\\)|\u222A|\u2229|\\\\|~|\s]");
        for (char ch : expr.toCharArray()) {
            matcher = pattern.matcher(String.valueOf(ch));
            if (!matcher.find()) {
                throw new InvalidExpressionException("Character '" + ch + "' is unrecognised!");
            }
        }
    }

    private static void validateBracketFormatting(String expr) throws InvalidExpressionException {
        char[] arr = expr.toCharArray();

        if (arr.length == 0) {
            return;
        }

        int balance = 0; // bracket balance 
        char currentCh = arr[0];
        for (int i = 0; i < arr.length; i++) {
            char nextCh = i == arr.length - 1 ? ' ' : arr[i + 1];

            balance += arr[i] == '(' ? 1 : 0;
            balance += arr[i] == ')' ? -1 : 0;

            // if there are more closed than open brackets 
            if (balance < 0) {
                throwBracketException("Unmatched closed brackets.", expr, i);
            }

            // if there are recurring brackets that contains nothing, i.e. a ∩ () b
            if ((currentCh == '(' && nextCh == ')') || (nextCh == '(' && currentCh == ')')) {
                throwBracketException("Brackets must contain an expression.", expr, i + 1);
            }

            // if an oeprator immediately follows another an isn't ~
            if (isOperator(String.valueOf(currentCh)) && isOperator(String.valueOf(nextCh)) && nextCh != '~') {
                throw new InvalidExpressionException(
                    "Operator: " + nextCh + " cannot immediately<br>follow " + currentCh);
            }

            // update the current character pointer
            if (nextCh != ' ') {
                currentCh = nextCh;
            }
        }

        // if there were too many open brackets
        if (balance > 0) {
            throwBracketException("Unmatched open brackets.", expr, expr.length());
        }
    }

    /**
     * Creates a formatted string that points to where the bracket imbalance occurs
     * 
     * @param str, the string that caused the error
     * @return, the formatted string
     * @throws InvalidExpressionException
     */
    private static void throwBracketException(String msg, String expr, int errorIdx) throws InvalidExpressionException {
        String ret = msg + "<br>" + expr + "<br>";
        ret += "_".repeat(errorIdx) + "^" + "_".repeat(expr.length() - errorIdx);
        throw new InvalidExpressionException(ret);
    }
}
