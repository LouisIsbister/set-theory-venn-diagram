package code.expressions;

public class StructuredExpr {

    private static String ret;

    /**
     * @param expr, the user provided expression
     * @return
     * @throws InvalidExpressionException
     */
    public static String get(String expr) throws InvalidExpressionException {
        ret = "";
        restructureExpression(expr);
        System.out.println(ret);
        return ret;
    }

    /**
     * convert the user provided expression into cambridge polish notation.
     * for every character in the expression, if the character is not a valid
     * set identifier, i.e. [a-zA-Z].
     * 
     * @throws Exception
     */
    public static void restructureExpression(String str) throws InvalidExpressionException {
        str = str.trim();
        if (str.length() < 1)
            return;

        while (str.charAt(0) == '(' && str.charAt(str.length() - 1) == ')' && str.length() > 1)
            str = str.substring(1, str.length() - 1);

        // char[] arr = str.toCharArray();

        int openBracketCount = 0;
        int closedBracketCount = 0;

        int cen = 0;

        // for (int i = 0; i < str.length(); i++) {
        for (int i = str.length() - 1; i >= 0; i--) {
            char c = str.charAt(i);

            if (c == ' ')
                continue;

            if (c == '(')
                openBracketCount++;
            else if (c == ')')
                closedBracketCount++;
            // find the center most opertor
            else if (!(c + "").matches("[a-zA-Z]")) {
                cen = i;
                if (openBracketCount == closedBracketCount)
                    break;
            }
        }

        if (closedBracketCount != openBracketCount && str.length() > 2)
            throw new InvalidExpressionException("Unmatching brackets");

        String left = str.substring(0, cen).trim();
        String right = str.substring(cen + 1, str.length()).trim();

        char ch = str.charAt(cen);
        if (ch != ')' && ch != '(')
            ret += ch + " ";

        if (left.length() > 0 && !left.isBlank())
            restructureExpression(left);
        if (right.length() > 0 && !right.isBlank())
            restructureExpression(right);
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
