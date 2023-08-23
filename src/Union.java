import java.util.HashSet;
import java.util.Set;

public class Union implements Operator{

	/**
	 * Method that evaluates a union operator, return all the elements in the two sets.
	 * 
	 * @param left, the left node of the operator
	 * @param right, the right node of the operator
	 * @return, all the coordinates of the left and right nodes
	 */
	@Override
	public Set<Coordinate> evaluate(BTNode left, BTNode right) throws Exception {
		Set<Coordinate> allCoords = new HashSet<>();
		allCoords.addAll(left.evaluate());
		allCoords.addAll(right.evaluate());
		return allCoords;
	}
	
	public String toString() {
		return "union";
	}
}