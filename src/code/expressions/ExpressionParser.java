package code.expressions;

import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import code.util.InvalidExpressionException;

public class ExpressionParser {

    private static Stack<String> output;
    
    private static Stack<String> opStack;

    private static final Set<String> ops = Set.of("\u222A", "\u2229", "\\", "~");
    
    public static Stack<String> shuntingYardAlgoritm(String expr) throws InvalidExpressionException {
        validateExpression(expr);

        Pattern pat = Pattern.compile("([a-zA-Z]{1}|\\(|\\)|\u222A|\u2229|\\\\|~)");
        Matcher matcher = pat.matcher(expr);

        output = new Stack<>();
        opStack = new Stack<>();

        while (matcher.find()) {
            String token = matcher.group();

            if (token.matches("[a-zA-Z]{1}")) {
                output.add(token);
            } 
            else if (token.matches("\u222A|\u2229|\\\\|~")) {
                System.out.println(token);
                while (!opStack.isEmpty() && !opStack.peek().equals("(")){
                    output.add(opStack.pop());
                }
                opStack.add(token);
            } 
            else if (token.equals("(")) {
                opStack.add(token);
            } 
            else if (token.equals(")")) {
                while (!opStack.isEmpty() && !opStack.peek().equals("(")) {
                    output.add(opStack.pop());
                }
                if (!opStack.peek().equals("(")) {
                    // unmatched brackets
                    throw new InvalidExpressionException(formatBracketException(expr));
                }
                opStack.pop();
            } 
            else {
                throw new InvalidExpressionException("Unknown token: '" + token + "'");
            }
        }

        while (!opStack.isEmpty()) {
            if (opStack.peek().equals("(")) {
                // unmatched brackets
                throw new InvalidExpressionException(formatBracketException(expr));
            }
            output.add(opStack.pop());
        }

        return output;
    }

    /**
     * Check that all the set ids are no longer than one letter. 
     * Check that the open and closed bracket counts are equal.
     * 
     * @param str, the provided expression
     * @throws InvalidExpressionException
     */
    private static void validateExpression(String str) throws InvalidExpressionException {
        // check set ids
        Pattern pattern = Pattern.compile("[a-zA-Z]{2,}");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            String s = matcher.group();
            String ret = s.length() > 10 ? s.substring(0, 10) + "..." : s;
            throw new InvalidExpressionException("Set ids must be one letter. Found '" + ret + "'");
        }

        // check bracket format
        if (Pattern.matches(".*\\([\s]*\\).*", str))
            throw new InvalidExpressionException("Brackets must contain an expression.<br>" + formatBracketException(str));
        if (Pattern.matches(".*\\)[\s]*\\(.*", str))
            throw new InvalidExpressionException("An operator must connect a pair of brackets.<br>" + formatBracketException(str));
    }

     /**
     * Creates a formatted string that points to where the bracket imbalance occurs
     * 
     * @param str, the string that caused the error
     * @return, the formatted string
     */
    private static String formatBracketException(String str) {
        int balance = 0;
        int idx = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '(')
                balance++;
            else if (c == ')')
                balance--;
            
            if (balance != 0 && (c == ')' || c == '(')) 
                idx = i;
        }

        String ret = str + "<br>" + "_".repeat(idx) + "^" + "_".repeat(str.length() - idx);
        return ret;
    }
    
}
