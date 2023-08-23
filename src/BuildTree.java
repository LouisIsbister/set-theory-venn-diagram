import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;


public class BuildTree {
	
	/**
	 * The starting x and y coordinates of the Venn diagram, is used to determine the pixels (data)
	 * that is contained within a set.
	 */
	public static final int startX = 250;
	public static final int startY = 250;

	/**
	 * Map of all the set nodes in the expression, a map is required as nodes may need to be 
	 * retrieved multiple times.
	 */
	private static Map<String, SetNode> setNodes = new HashMap<>();
	
	/**
	 * Pattern that represents each of the possible operators in an expression
	 */
	private static Pattern OPERATOR_PAT = Pattern.compile("(union|intersect|difference|complement)");
	
	/**
	 * The complement of a set node, call it A, is equal to the universal set (U) minus this set.
	 * This can be expressed as U-A or U\A. The universal set represents all the data available
	 * and is constant. 
	 */
	private static final SetNode universalSet = new SetNode("Universal data") {
		public Set<Coordinate> evaluate(){
			HashSet<Coordinate> allCoords = new HashSet<>();
			for(int i = 0; i < 2*BuildTree.startX; i++) {
				for(int j = 0; j < 2*BuildTree.startY; j++) {
					allCoords.add(new Coordinate(i, j));
				}
			}
			return allCoords;
		}
	};
	
	/**
	 * Retrieves all the set nodes (leaf nodes)
	 * 
	 * @return, a collection of set nodes
	 */
	public Collection<SetNode> setNodes(){
		return setNodes.values();
	}
	
	/**
	 * This method intialises the creation of the tree structure.
	 * It first calls the recursive method to build the binary tree, it then checks for formatting
	 * errors, these cases are when the user has provided too many arguments, not enough arguments, 
	 * or invalid set identifiers.
	 * It then calls the propagateSetNodes method that fills the sets with pixels/data before 
	 * returning the root of the binary tree.
	 * 
	 * @param expression, the expression that is to be evaluated
	 * @return, the root node of binary tree representation of the expression
	 */
	public BTNode createTree(String expression) throws Exception{
		Scanner scan = new Scanner(expression);
		scan.useDelimiter("(?<=\\()|(?>=\\))|\\s+");	
		BTNode root = null;

		// recursively create the binary tree
		root = recursion(scan);
		
		// user provided an too many, or no arguments
		if(scan.hasNext()) throw new Exception("'Invalid format: too many arguments were provided.'");
		if(setNodes.size() < 1) throw new Exception("'Invalid format: no arguments were provided.'");
		
		// user entered incorrect set identifiers
		boolean validSetIdentifiers = setNodes.keySet().stream()
					.allMatch(elem-> Pattern.matches("[a-zA-Z]", elem));
		if(!validSetIdentifiers) throw new Exception("'Invalid format: make sure sets are in the from a-z or A-Z'");

		this.propagateSetNodes();
		
		scan.close();
		return root;
	}
	
	/**
	 * This method recursively builds the binary tree of the expression.
	 * It retrieves the next element from the scanner and checks whether it 
	 * is an operator. If so, create a new parent node and set its left and right children.
	 * Otherwise, find or create a new set node (leaf) and return it.
	 * 
	 * @param scan, the scanner that traverses the expression
	 * @return, the root node of the binary tree
	 */
	private BTNode recursion(Scanner scan) {
		if(!scan.hasNext()) return null;
		String str = scan.next().trim();
				
		String[] subNodes = str.split("(\\(|\\))");

		String element = subNodes[0];
		
		if(OPERATOR_PAT.matcher(element).matches()) {
			Operator op = parseOperator(element);
			BTNode node = new BTNode(op);
			
			node.setLeft(recursion(scan));

			// if the operator is a complement then the right node must be the universal set
			if(op instanceof Complement) node.setRight(universalSet);
			else node.setRight(recursion(scan));
			
			return node;
		}
		else {
			element = element.toUpperCase();
			if(!setNodes.containsKey(element)) {
				setNodes.put(element, new SetNode(element));
			}
			return setNodes.get(element);
		}
	}
	
	/**
	 * Returns a new instance of a specific operator.
	 * 
	 * @param operator, the type of operator
	 * @return, the operator if it exists
	 */
	private static Operator parseOperator(String operator){
		if(operator.equalsIgnoreCase("union")) {
			return new Union();
		}
		else if(operator.equalsIgnoreCase("intersect")) {
			return new Intersect();
		}
		else if(operator.equalsIgnoreCase("difference")) {
			return new Difference();
		}
		else if(operator.equalsIgnoreCase("complement")) {
			return new Complement();
		}
		// this case will never be reached
		throw new IllegalArgumentException();
	}	
	
		
	/**
	 * This method determines and initializes the central coordinate of a set.
	 * Then, every pixel within the sphere that represents a set is added to the 
	 * collection of pixels in that set. 
	 * Each pixel represents a piece of data within the set. 
	 */
	private void propagateSetNodes() {
		int NUMBER_OF_SETS = setNodes.size();		
		int RADIUS = SetNode.DIAMETER/2;
				
		// the angle difference of each circle from the center 
		double ANGLE_OFFSET = 360/NUMBER_OF_SETS;
				
		int setCount = 0;
		for(SetNode setNode : setNodes.values()) {
			// get the angle of the sets circle
			double angle = (setCount * ANGLE_OFFSET) - 90;
			double angleInRadians = angle * (Math.PI/180);
			
			// get the center coordinates of this sets spherical representation 
			int xCoord = BuildTree.startX + (int)(Math.cos(angleInRadians) * RADIUS/2);
			int yCoord = BuildTree.startY + (int)(Math.sin(angleInRadians) * RADIUS/2);
			
			Coordinate coord = new Coordinate(xCoord, yCoord);
			setNode.setCenter(coord);
						
			// ---- get the position for the sets identifying string on the panel
			int strX = xCoord + (int) (Math.cos(angleInRadians) * RADIUS/1.25);
			int strY = yCoord + (int) (Math.sin(angleInRadians) * RADIUS/1.25);
			Coordinate stringCoord = new Coordinate(strX, strY);
			setNode.setStringPosition(stringCoord);
			// ----
						
			// p = {(x,y) : (x-mX)^2 + (y-mY)^2 <= r^2}
			for(int x = xCoord-RADIUS; x < xCoord+RADIUS; x++) {
				for(int y = yCoord-RADIUS; y < yCoord+RADIUS; y++) {
					int xDifference = x - setNode.center().x();	// x-mX
					int yDifference = y - setNode.center().y();	// y-mY
					
					int value = xDifference * xDifference + yDifference * yDifference;
					int radiusSquared = RADIUS * RADIUS;
					
					// (x-mX)^2 + (y-mY)^2 <= r^2
					if(value <= radiusSquared) {
						Coordinate c = new Coordinate(x, y);
						setNode.addPixel(c);
					}					
				}
			}
			setCount++;
		}
	}
}
