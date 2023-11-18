package code.controller;

/**
 * 
 * @author Louis Isbister
 * 
 */
public class Main {

	/**
	 * Some valid expressions to test:
	 * 
	 * a ∪ (b ∩ c)              "a or b and c"
	 * (a ∩ b) ∪ (c ∩ d)        "a and b or c and d"
	 * ((b ∪ c) ∩ a) ∪ (b ∩ c)  "a and b or c or b and c"
	 * a ∩ (b \ c)              "a and the difference of b and c"
	 * a \ (b ∩ c)              "the difference of a and the intersection of b and c"
	 * (c ∩ (a ∪ b)) \ (a ∩ b)  "the difference of the (intersection of c and the union of a and b)"
	 * 							"and (a and b)"
	 * ~(a \ b)                 "complement of the difference between a and b"
	 * a ∩ b ∩ (~(c ∪ d))       "a and b and the complement of c or d"
	 * 
	 * (c ∩ d) ∪ (a \ ((b ∪ c) ∩ (d ∪ e)))
	 * "c and d or the difference between a and the intersection of 
	 * c and d's union and d and e's union"
	 */

	 //d \ (a ∪ c) ∪ b \ (a ∪ c), (a ∩ b) ∩ ( c ∩ d) ∪ (e ∩f)

	public static void main(String args[]) {
		new AppFrame();
	}
}
