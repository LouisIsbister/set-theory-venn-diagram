package com.stvd.expressionparsing;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stvd.util.ParserFailureException;

public class ExpressionValidator {

    private static final String OPERATORS = "[\u222A|\u2229|\\\\|~]";
    private static final String VALID_CHARACTERS = "[a-zA-Z|\\(|\\)|\u222A|\u2229|\\\\|~|\s]";
    private static final String INVALID_SET_IDS = "[a-zA-Z]{2,}";

    /**
     * @param input
     * @return whether the input is an operator
     */
    public static boolean isOperator(String input) {
        return input.matches(OPERATORS);
    }

    /**
     * ensure that there are no invalid characters/strings in the 
     * expression
     *  
     * @param expr
     * @throws ParserFailureException
     */
    public static void checkCharacters(String expr) throws ParserFailureException {
        // check that there are not set ids with 2 or more characters
        Pattern pattern = Pattern.compile(INVALID_SET_IDS);
        Matcher matcher = pattern.matcher(expr);
        if (matcher.find()) {
            String s = matcher.group();
            String ret = s.length() > 10 ? s.substring(0, 10) + "..." : s;
            throw new ParserFailureException("Set ids must be one letter.<br>Found '" + ret + "'");
        }

        // ensure the expression only has valid characters
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
    public static void checkBracketFormatting(String expr) throws ParserFailureException {
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
    public static void checkIsExecutableExpression(Queue<String> exp) throws ParserFailureException {
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
