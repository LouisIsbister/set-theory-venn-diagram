package src.stvd.controller;

public class Main {

	/**
	 * Some valid expressions to test:
	 * 
	 * a ∪ (b ∩ c)
	 * (a ∩ b) ∪ (c ∩ d)
	 * ((b ∪ c) ∩ a) ∪ (b ∩ c)
	 * a ∩ (b \ c)
	 * a \ (b ∩ c)
	 * (c ∩ (a ∪ b)) \ (a ∩ b)
	 * ~(a \ b)
	 * a ∩ b ∩ ~(c ∪ d)
	 * 
	 * More complex expressions to test:
	 * 
	 * (c ∩ d) ∪ (a \ (b ∩ e))
	 * (((b ∪ c) ∪ (d ∪ e)) \ a) ∪ ((b ∪ c) ∩ (d ∪ e))
	 * a ∪ (d \ c) ∪ (c \ d) ∪ ((c ∩ d) \ (b ∪ e))
	 * (a \ (b ∪ e)) ∪ (b \ (a ∪ c)) ∪ (e \ (a ∪ d)) ∪ (d ∩ c)
	 * ((a ∩ (b ∪ d)) ∪ (c ∩ (b ∪ d))) \ ((a ∩ c) ∪ (b ∩ d))
	 */
	
	public static void main(String args[]) {
		new AppFrame();
	}
}
