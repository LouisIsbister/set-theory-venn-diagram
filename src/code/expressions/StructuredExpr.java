package code.expressions;

import code.util.InvalidExpressionException;

public class StructuredExpr {

    /**
     * @param expr, the user provided expression
     * @return, the restructured expression
     * @throws InvalidExpressionException
     */
    public static String restructureExpression(String expr) throws InvalidExpressionException {
        int openParenCount = (int) expr.codePoints().filter(e -> e == '(').count();
        int closedParenCount = (int) expr.codePoints().filter(e -> e == ')').count();

        // if the brackets do not match then throw an exception
        if (openParenCount != closedParenCount)
            throw new InvalidExpressionException(formatBracketException(expr));

        return restructure(expr);
    }

    /**
     * convert the user provided expression into cambridge polish notation.
     * for every character in the expression, if the character is not a valid
     * set identifier, i.e. [a-zA-Z].
     * 
     * @throws InvalidExpressionException
     */
    private static String restructure(String str) throws InvalidExpressionException {
        str = str.trim();
        if (str.matches("[a-zA-Z]+") || str.isEmpty())
            return str + " ";

        String ret = "";
        int exprCenter = getCenterIndex(str);
        String left = str.substring(0, exprCenter).trim();
        String right = str.substring(exprCenter + 1, str.length()).trim();

        char ch = str.charAt(exprCenter);
        if (ch != ')' && ch != '(')
            ret += ch + " ";

        if (left.length() > 0 && !left.isBlank())
            ret += restructure(left);
        if (right.length() > 0 && !right.isBlank())
            ret += restructure(right);

        return ret;
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
