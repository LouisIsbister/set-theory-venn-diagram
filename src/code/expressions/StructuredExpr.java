package code.expressions;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import code.util.InvalidExpressionException;

public class StructuredExpr {

    private static Queue<Character> expression;

    /**
     * @param expr, the user provided expression
     * @return, the restructured expression
     * @throws InvalidExpressionException
     */
    public static Queue<Character> restructureExpression(String expr) throws InvalidExpressionException {
        validateExpression(expr);

        expression  = new ArrayDeque<>();
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
        if (str.isEmpty() || str.equals("\\(") || str.equals("\\)"))
            return null;
        
        int exprCenter = getCenterIndex(str);
        String left = str.substring(0, exprCenter).trim();
        String right = str.substring(exprCenter + 1, str.length()).trim();

        char ch = str.charAt(exprCenter);
        // System.out.println("DBG:: Cen-> " + ch + ", left-> " + left + ", right-> " + right);
        
        if (ch != ')' && ch != '(') 
            expression.add(ch);
        
        if (left.length() > 0 && !left.isBlank())
            restructure(left);
        if (right.length() > 0 && !right.isBlank())
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
            else if (!(c + "").matches("[a-zA-Z]")) {
                ret = i;

                // if the brackets match then we've found a center
                if (openBracketCount == closedBracketCount)
                    return ret;
            }
        }

        // if no center was found then their is either a too many open brackets
        // or there is operator
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
        // check set ids
        Pattern pattern = Pattern.compile("[a-zA-Z]{2,}");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            String s = matcher.group();
            String ret = s.length() > 10 ? s.substring(0, 10) + "..." : s;
            throw new InvalidExpressionException("Set ids must be one letter.<br>Found '" + ret + "'");
        }

        // check brackets
        int openParenCount = (int) str.codePoints().filter(e -> e == '(').count();
        int closedParenCount = (int) str.codePoints().filter(e -> e == ')').count();
        if (openParenCount != closedParenCount)
            throw new InvalidExpressionException(formatBracketException(str));
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
        
        String insrt = balance < 0 ? "open" : "closed";
        String ret = "Unmatching brackets: require '"+ insrt + "'<br>" + str + "<br>";
        ret += "_".repeat(idx - 1) + "^" + "_".repeat(str.length() - idx - 1);
        return ret;
    }
}
