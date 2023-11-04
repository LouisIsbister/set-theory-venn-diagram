package code.controller;

import java.util.Stack;

/**
 * 
 * @author Louis Isbister
 * 
 */
public class Main {

	/**
	 * Some valid expressions to test:
	 * "union(intersect(A B) intersect(C D))"
	 * "union(A intersect(B C))"
	 * "intersect(A difference(B C))"
	 * "difference(intersect(C union(A B)) intersect(A B))"
	 * "union(intersect(C D) difference(A intersect(union(B C) union(D E))))"
	 * "complement(A)"
	 * "complement(difference(B C))"
	 * "union(intersect(A complement(union(B C))) intersect(B C))"
	 * "difference(A intersect(A union(B C)))"
	 */

	public static void main(String args[]) {
		// String infixExpression = "((B ∪ C) ∩ A) ∪ (B ∩ C)";
		// String postfixExpression = infixToPostfix(infixExpression);
		// System.out.println(postfixExpression);
		// boolean result = evaluatePostfix(postfixExpression);

		// if (result) {
		// 	System.out.println("Result: true");
		// } else {
		// 	System.out.println("Result: false");
		// }
		new AppFrame();
	}

	public static int precedence(char operator) {
		if (operator == '∩' || operator == '∪') {
			return 2;
		} else if (operator == '(' || operator == ')') {
			return 1;
		}
		return 0;
	}

	public static String infixToPostfix(String infix) {
		StringBuilder postfix = new StringBuilder();
		Stack<Character> stack = new Stack<>();

		for (char token : infix.toCharArray()) {
			if (Character.isLetter(token)) {
				postfix.append(token);
			} else if (token == '(') {
				stack.push(token);
			} else if (token == ')') {
				while (!stack.isEmpty() && stack.peek() != '(') {
					postfix.append(stack.pop());
				}
				if (!stack.isEmpty()) {
					stack.pop(); // Pop the '('
				}
			} else {
				while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(token)) {
					postfix.append(stack.pop());
				}
				stack.push(token);
			}
		}

		while (!stack.isEmpty()) {
			postfix.append(stack.pop());
		}

		return postfix.toString();
	}

	public static boolean evaluatePostfix(String postfix) {
		Stack<Boolean> stack = new Stack<>();

		for (char token : postfix.toCharArray()) {
			if (Character.isLetter(token)) {
				// Assuming B, C, and A are variables representing boolean values
				if (token == 'B') {
					stack.push(true); // Replace with the actual value of B
				} else if (token == 'C') {
					stack.push(true); // Replace with the actual value of C
				} else if (token == 'A') {
					stack.push(true); // Replace with the actual value of A
				}
			} else if (token == '∪') {
				if (stack.size() >= 2) {
					boolean operand2 = stack.pop();
					boolean operand1 = stack.pop();
					stack.push(operand1 || operand2);
				}
			} else if (token == '∩') {
				if (stack.size() >= 2) {
					boolean operand2 = stack.pop();
					boolean operand1 = stack.pop();
					stack.push(operand1 && operand2);
				}
			}
		}

		return stack.pop();
	}

}
