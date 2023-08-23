import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Intersect implements Operator{

	/**
	 * Method that evaluates an ntersect operator, returns all the members of 
	 * the left node that are also in the right node.
	 * 
	 * @param left, the left node of the operator
	 * @param right, the right node of the operator
	 * @return, the set of all common coordinates
	 */
	@Override
	public Set<Coordinate> evaluate(BTNode left, BTNode right) throws Exception {
		Set<Coordinate> leftSet = left.evaluate();
		Set<Coordinate> rightSet = right.evaluate();
						
		List<Coordinate> list = leftSet.stream()
				.filter(elem -> rightSet.contains(elem))
				.toList();
				
		return new HashSet<>(list);
	}
	
	public String toString() {
		return "intersect";
	}
}
