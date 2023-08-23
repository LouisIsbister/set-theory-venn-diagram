import java.util.Set;

public interface Operator {
	public Set<Coordinate> evaluate(BTNode left, BTNode right) throws Exception;
}
