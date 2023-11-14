package code.expressions;

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
            throw new InvalidExpressionException("Unmatching brackets");

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

        // remove the unnecassary brackets
        while (str.charAt(0) == '(' && str.charAt(str.length() - 1) == ')' && str.length() > 2)
            str = str.substring(1, str.length() - 1);

        int exprCenter = getCenterIndex(str);
        String left = str.substring(0, exprCenter).trim();
        String right = str.substring(exprCenter + 1, str.length()).trim();

        char ch = str.charAt(exprCenter);
        if (ch != ')' && ch != '(')
            ret += ch + " ";

        if (left.length() > 0 && !left.isBlank())
            ret += restructureExpression(left);
        if (right.length() > 0 && !right.isBlank())
            ret += restructureExpression(right);
        
        return ret;
    }

    /**
     * Method that finds the "center" of an expression or sub-expression.
     * The center is the most recent operator that is contained within 
     * matching open and closed brackets. 
     * i.e. a ∩ (b ∪ c)
     *             ^ is the center of this expression 
     * 
     * @param str, the string that is being searched for an operator
     * @return, the index of the operator if one is found
     * @throws InvalidExpressionException
     */
    private static int getCenterIndex(String str) throws InvalidExpressionException {
        int openBracketCount = 0;
        int closedBracketCount = 0;
        int ret = 0;

        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) == ' ')
                continue;
            
            char c = str.charAt(i);
            if (c == '(')
                openBracketCount++;
            else if (c == ')')
                closedBracketCount++;
            
            // if an operator has been found
            if (!(c + "").matches("[a-zA-Z]")) {
                ret = i;

                // if the brackets match then we've found a center
                if (openBracketCount == closedBracketCount)    
                    return ret;
            }
        }        
        return 0;
    }  

    // private static void formatBracketException(String str) throws InvalidExpressionException {
    //     int openBrCount = 0;
    //     int closeBrCount = 0;
    //     int idx = 0;
    //     for (int i = 0; i < str.length(); i++) {
    //         char c = str.charAt(i);

    //         if (c == '(' || c == ')') {
    //             if (c == '(')
    //                 openBrCount++;
    //             else if (c == ')')
    //                 closeBrCount++;
    //             // find the center most opertor
    //             if (openBrCount != closeBrCount) {
    //                 idx = i;
    //             }
    //         }
    //     }
    //     System.out.println("mismatch: " + idx);
    //     String ret = "<br>" + str + "<br>";
    //     ret += " ".repeat(idx);
    //     ret += "^";
    //     ret += " ".repeat(str.length() - idx) + "";
    //     System.out.println(ret);
    //     throw new InvalidExpressionException(ret);
    // }
}
