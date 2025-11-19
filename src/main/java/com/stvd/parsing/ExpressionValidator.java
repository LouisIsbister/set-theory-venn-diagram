package com.stvd.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stvd.util.ParserFailureException;

public class ExpressionValidator {

    public record BracketBalanceInfo(int balance, int imbalanceIdx) {}

    private static final String BINARY_OPERATORS = "[\u222A\u2229\\\\]";
    private static final String UNARY_OPERATORS  = "~";

    private static final Pattern VALID_CHARACTERS = Pattern.compile("[a-zA-Z\\(\\)\u222A\u2229\\\\~\s]");
    private static final Pattern INVALID_SET_IDS  = Pattern.compile("[a-zA-Z]{2,}");
    private static final Pattern EMPTY_BRACKETS   = Pattern.compile("\\(\\)");


    public static boolean isOperator(String input) {
        return isUnaryOperator(input) || isBinaryOperator(input);
    }

    public static boolean isUnaryOperator(String input) {
        return input.matches(UNARY_OPERATORS);
    }

    public static boolean isBinaryOperator(String input) {
        return input.matches(BINARY_OPERATORS);
    }


    /**
     * ensure that there are no invalid characters/strings in the, and that there are no invalid set ids
     * or brackets with no expression
     *  
     * @param expr
     * @throws ParserFailureException
     */
    public static void checkCharacterSequences(String expr) throws ParserFailureException {
        Matcher matcher;
        for (char ch : expr.toCharArray()) {
            matcher = VALID_CHARACTERS.matcher(String.valueOf(ch));
            if (!matcher.find())
                throw new ParserFailureException("Character '" + ch + "'' is unrecognised.");
        }

        matcher = INVALID_SET_IDS.matcher(expr);
        if (matcher.find())
            throw new ParserFailureException("Set IDs must be one letter, detected: '" + matcher.group() + "'");
        
        matcher = EMPTY_BRACKETS.matcher(expr);
        if (matcher.find())
            throw new ParserFailureException("Brackets must contain an expression, detected: '" + matcher.group() + "'");
    }


    /**
     * check that the brackets in the expression are balanced
     * 
     * @param expr
     * @throws ParserFailureException
     */
    public static void checkBracketBalance(String expr) throws ParserFailureException {
        BracketBalanceInfo bracketBalance = bracketBalance(expr);
        int balance = bracketBalance.balance();
        if (bracketBalance.balance() == 0)
            return;
        
        String msg = balance < 0 ? 
                "Unmatched closed brackets." : 
                "Unmatched open brackets.";
        throwBracketException(msg, expr, bracketBalance.imbalanceIdx());
    }


    /**
     * 
     * @param str
     * @return
     */
    public static BracketBalanceInfo bracketBalance(String str){
        char[] arr = str.toCharArray();
        int balance = 0;
        for (int i = 0; i < arr.length; i++) {
            balance += arr[i] == '(' ? 1 : 0;
            balance += arr[i] == ')' ? -1 : 0;
            if (balance < 0)
                return new BracketBalanceInfo(balance, i);
        }
        if (balance > 0)
            return new BracketBalanceInfo(balance, str.length());

        return new BracketBalanceInfo(0, -1);
    } 


    /**
     * Emulate the construction of a binary tree to ensure that the arguments provided by 
     * the user will result in an executable tree. This method performs all the necassary 
     * preconditions to ensure no node will encounter null pointers during execution!
     * 
     * @param exp polish notation form of an expression
     * @throws ParserFailureException
     */
    public static void checkIsExecutableExpression(Queue<String> exp) throws ParserFailureException {
        List<String> elems = new ArrayList<>(exp);
        Stack<String> operands = new Stack<>();

        for (int i = elems.size() - 1; i >= 0; i--) {
            String elem = elems.get(i);

            if (isUnaryOperator(elem)) {
                if (operands.size() < 1)
                    throw new ParserFailureException(elem + " must have one arg");
                operands.pop();
                operands.push(elem);
            } 
            else if (isBinaryOperator(elem)) {
                if (operands.size() < 2)
                    throw new ParserFailureException(elem + " must have two args");
                operands.pop();
                operands.pop();
                operands.push(elem);
            }
            else {
                operands.push(elem);
            }
        }
        if (operands.size() != 1)
            throw new ParserFailureException("Invalid expression,<br>too many args given");
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
