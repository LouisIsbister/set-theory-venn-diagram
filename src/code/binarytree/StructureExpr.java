package code.binarytree;

public class StructureExpr {

    private String ret; 

    public StructureExpr(String expr) {
        ret = "";
        restructureExpression(expr);
    }

    public String restructuredString() {
        return ret;
    }

    /**
	 * convert the user provided expression into cambridge polish notation.
	 * for every character in the expression, if the character is not a valid
	 * set identifier, i.e. [a-zA-Z].
	 */
	public void restructureExpression(String str) {
        str = str.trim();
        if (str.length() < 1) {
            return;
        }

		char[] arr = str.toCharArray();

		int openBracketCount = 0;
		int closedBracketCount = 0;

        int mid = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == ' ') continue;

            char c = arr[i];
            if (c == '(') { 
                openBracketCount++;
            }
            else if (c == ')'){
                closedBracketCount++;
            }
            // find the center most opertor
            else if (!(c + "").matches("[a-zA-Z]") && openBracketCount == closedBracketCount && c != ' '){
                mid = i;
                break;
            }
        }


        String left = str.substring(0, mid);
        String right = str.substring(mid + 1, str.length());

        // ((B ∪ C) ∩ A) ∪ (B ∩ C)

        String post = str.substring(mid, mid + 1);

        if (post.charAt(0) != ')' && post.charAt(0) != '(') {
            ret += post;

            if (!(post.charAt(0) + "").matches("[a-zA-Z]") && post.charAt(0) != ' ')
                ret += "(";
            else 
                ret += " ";
        }

        if (left.length() > 0)
            restructureExpression(left);
        if (right.length() > 0)
            restructureExpression(right);
	}
}
