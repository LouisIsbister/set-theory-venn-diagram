package stvd.expressionparser;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import stvd.util.InvalidExpressionException;

public class ExpressionParser {

    private static Queue<String> expression;

    /**
     * @param expr, the user provided expression
     * @return, the restructured expression
     * @throws InvalidExpressionException
     */
    public static Queue<String> parse(String expr) throws InvalidExpressionException {
        expression = new ArrayDeque<>();
        if (expr.isBlank()) {
            return expression;
        }

        validateExpressionCharacters(expr);
        validateBracketFormatting(expr);

        return restructure(expr);
    }

    /**
	 * Check whether a given character is an operator
	 * 
	 * @param c, the provided character
	 * @return
	 */
	protected static boolean isOperator(String value) {
		return value.matches("[\u222A|\u2229|\\\\|~]");
	}

    /**
     * convert the user provided expression into cambridge polish notation.
     * 
     * @throws InvalidExpressionException
     */
    private static Queue<String> restructure(String str) throws InvalidExpressionException {
        if (str.equals("(") || str.equals(")")) {
            return expression;
        }

        int exprCenter = getCenterIndex(str); // the index to split the expression by
        char centerChar = str.charAt(exprCenter);
        if (centerChar != ')' && centerChar != '(') {
            expression.add(String.valueOf(centerChar));
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
            if (isOperator(String.valueOf(ch)) && openBracketCount == closedBracketCount) {
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

        int balance = 0, unbalancedIdx = 0; // bracket balance and possible bad balance index
        char currentCh = arr[0];
        for (int i = 0; i < arr.length; i++) {
            char nextCh = i == arr.length - 1 ? ' ' : arr[i + 1];

            balance += arr[i] == '(' ? 1 : 0;
            balance += arr[i] == ')' ? -1 : 0;

            // if there are more closed than open brackets 
            if (balance < 0 && currentCh == ')' && unbalancedIdx == 0) {
                throwBracketException("Unmatched closed brackets.", expr, i);
            }

            // if there are recurring brackets that contains nothing, i.e. a ∩ () b
            if ((currentCh == '(' && nextCh == ')') || (nextCh == '(' && currentCh == ')')) {
                throwBracketException("Brackets must contain an expression.", expr, unbalancedIdx);
            }

            // if an oeprator immediately follows another an isn't ~
            if (isOperator(String.valueOf(currentCh)) && isOperator(String.valueOf(nextCh)) && nextCh != '~') {
                throw new InvalidExpressionException(
                    "Operator: " + nextCh + " cannot immediately<br>follow " + currentCh);
            }

            // update the current character to point at the latest valid part of the
            // expression
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
     *            @return, the formatted string
     * @throws InvalidExpressionException
     */
    private static void throwBracketException(String msg, String expr, int errorIdx) throws InvalidExpressionException {
        String ret = msg + "<br>" + expr + "<br>";
        ret += "_".repeat(errorIdx) + "^" + "_".repeat(expr.length() - errorIdx);
        throw new InvalidExpressionException(ret);
    }
}
