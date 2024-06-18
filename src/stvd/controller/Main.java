package stvd.controller;

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
	 * (((b ∪ c) ∪ (d ∪ e)) \ a) ∪ ((b ∪ c) ∩ (d ∪ e))
	 * (c ∩ d) ∪ (a \ ((b ∪ c) ∩ (d ∪ e)))
	 * a ∪ (d \ c) ∪ (c \ d) ∪ ((c ∩ d) \ (b ∪ e))
	 */
	
	public static void main(String args[]) {
		new AppFrame();
	}
}
