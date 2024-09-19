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
        toPolishNotation(expr);
        return expression;
    }

    /**
     * @param input
     * @return whether the input is an operator
     */
    public static boolean isOperator(String input) {
        return input.matches("[\u222A|\u2229|\\\\|~]");
    }

    /**
     * Converts the user provided expression into polish notation.
     * 
     * @param str the expression
     * @return queue of characters in CPN
     * @throws InvalidExpressionException
     */
    private static void toPolishNotation(String str) throws InvalidExpressionException {
        if (str.matches("^$|\\(|\\)")) {    // string is empty or is a bracket
            return;
        }

        int exprCenter = getCenterIndex(str); // the index to split the expression by
        char centerChar = str.charAt(exprCenter);
        if (centerChar != ')' && centerChar != '(') {
            expression.add(String.valueOf(centerChar));
        }

        String left = str.substring(0, exprCenter).trim();
        String right = str.substring(exprCenter + 1, str.length()).trim();

        toPolishNotation(left);
        toPolishNotation(right);
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
     * @throws InvalidExpressionException
     */
    private static int getCenterIndex(String str) throws InvalidExpressionException {
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
            if (isOperator(String.valueOf(ch)) && bracketBalance == 0 && ch != '~') {
                return i;
            }
        }
        return 0;
    }

    /**
     * ensure that there are no invalid characters/strings in the 
     * expression
     *  
     * @param expr
     * @throws InvalidExpressionException
     */
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

    /**
     * check that the brackets in the expression are balanced 
     * 
     * @param expr
     * @throws InvalidExpressionException
     */
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
     * creates a formatted string that points to where the bracket imbalance occurs
     * 
     * @param msg, cause of the error
     * @param expr, the user provided expression
     * @param errorIdx, the index at which the brackets are imbalanced
     * @throws InvalidExpressionException
     */
    private static void throwBracketException(String msg, String expr, int errorIdx) throws InvalidExpressionException {
        String ret = msg + "<br>" + expr + "<br>";
        ret += "_".repeat(errorIdx) + "^" + "_".repeat(expr.length() - errorIdx);
        throw new InvalidExpressionException(ret);
    }
}
