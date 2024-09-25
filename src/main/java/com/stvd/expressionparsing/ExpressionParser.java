package com.stvd.expressionparsing;

import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;
import java.util.ArrayDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stvd.util.ParserFailureException;

public class ExpressionParser {

    private static final String OPERATORS = "[\u222A|\u2229|\\\\|~]";
    private static final String VALID_CHARACTERS = "[a-zA-Z|\\(|\\)|\u222A|\u2229|\\\\|~|\s]";
    private static final String INVALID_SET_IDS = "[a-zA-Z]{2,}";

    /**
     * @param expr, the user provided expression
     * @return, the restructured expression
     * @throws ParserFailureException
     */
    public static Queue<String> parse(String expr) throws ParserFailureException {
        validateExpressionCharacters(expr);
        validateBracketFormatting(expr);

        Queue<String> polishExp = toPolishNotation(expr, new ArrayDeque<>());

        checkIsExecutableExpression(polishExp);
        return polishExp;
    }

    /**
     * @param input
     * @return whether the input is an operator
     */
    public static boolean isOperator(String input) {
        return input.matches(OPERATORS);
    }

    /**
     * Converts the user provided expression into polish notation.
     * 
     * @param str the expression
     * @return the expression in polish notation
     */
    private static Queue<String> toPolishNotation(String str, Queue<String> queue) {
        if (str.matches("^$|\\(|\\)")) {    // string is empty or is a bracket
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
     * @throws ParserFailureException
     */
    private static void validateExpressionCharacters(String expr) throws ParserFailureException {
        // check that there are not set ids with 2 or more characters
        Pattern pattern = Pattern.compile(INVALID_SET_IDS);
        Matcher matcher = pattern.matcher(expr);
        if (matcher.find()) {
            String s = matcher.group();
            String ret = s.length() > 10 ? s.substring(0, 10) + "..." : s;
            throw new ParserFailureException("Set ids must be one letter.<br>Found '" + ret + "'");
        }


        pattern = Pattern.compile(VALID_CHARACTERS);
        for (char ch : expr.toCharArray()) {
            matcher = pattern.matcher(String.valueOf(ch));
            if (!matcher.find()) {
                throw new ParserFailureException("Character '" + ch + "' is unrecognised!");
            }
        }
    }

    /**
     * check that the brackets in the expression are balanced 
     * 
     * @param expr
     * @throws ParserFailureException
     */
    private static void validateBracketFormatting(String expr) throws ParserFailureException {
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
                throw new ParserFailureException(
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
     * Emulate the construction of a binary tree to ensure that the arguments provided by 
     * the user will result in an executable tree. This method performs all the necassary 
     * preconditions to ensure no node will encounter null pointers during execution!
     * 
     * @param exp polish notation for of expression
     * @throws ParserFailureException
     */
    private static void checkIsExecutableExpression(Queue<String> exp) throws ParserFailureException {
        List<String> elems = new ArrayList<>(exp);
        Stack<String> operands = new Stack<>();

        for (int i = elems.size() - 1; i >= 0; i--) {
            
            String elem = elems.get(i);
            if (!isOperator(elem)) {
                operands.push(elem);
            }
            else if (isOperator(elem) && elem.equals("~")) {    // unary operators
                if (operands.size() < 1) {
                    throw new ParserFailureException(elem + " must have one arg.");
                }
                else {
                    operands.pop();
                    operands.push(elem);
                }
            }
            else if (isOperator(elem) && !elem.equals("~")) {    // unary operators
                if (operands.size() < 2) {
                    throw new ParserFailureException(elem + " must have two args.");
                }
                else {
                    operands.pop(); operands.pop();
                    operands.push(elem);
                }
            }
        }

        if (operands.size() != 1) {
            throw new ParserFailureException("Invalid expression,<br>too many args given.");
        }
    }

    /**
     * creates a formatted string that points to where the bracket imbalance occurs
     * 
     * @param msg, cause of the error
     * @param expr, the user provided expression
     * @param errorIdx, the index at which the brackets are imbalanced
     * @throws ParserFailureException
     */
    private static void throwBracketException(String msg, String expr, int errorIdx) throws ParserFailureException {
        String ret = msg + "<br>" + expr + "<br>";
        ret += "_".repeat(errorIdx) + "^" + "_".repeat(expr.length() - errorIdx);
        throw new ParserFailureException(ret);
    }
}
