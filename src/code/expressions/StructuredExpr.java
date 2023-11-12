package code.expressions;

public class StructuredExpr {

    private String ret;

    public StructuredExpr(String expr) {
        ret = "";
        try {
            restructureExpression(expr);
        } catch (InvalidExpressionException e) {
            e.printStackTrace();
        }
    }

    public String get() {
        System.out.println(ret);
        return ret;
    }

    /**
     * convert the user provided expression into cambridge polish notation.
     * for every character in the expression, if the character is not a valid
     * set identifier, i.e. [a-zA-Z].
     * @throws Exception
     */
    public void restructureExpression(String str) throws InvalidExpressionException {
        str = str.trim();
        if (str.length() < 1) 
            return;

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

        // if (closedBracketCount != openBracketCount) 
        //     throw new Exception("Unclosed brackets");

        String left = str.substring(0, cen);
        String right = str.substring(cen + 1, str.length());

        // ((B ∪ C) ∩ A) ∪ (B ∩ C)

        System.out.println("dbg 1: " + str.charAt(cen));
        System.out.println("dbg 2: " + left);
        System.out.println("dbg 3: " + right);

        String post = str.charAt(cen) + "";

        if (post.charAt(0) != ')' && post.charAt(0) != '(')
            ret += post + " ";

        if (left.length() > 0)
            restructureExpression(left);
        if (right.length() > 0)
            restructureExpression(right);
    }
}
