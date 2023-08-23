package Tree;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import Operators.*;

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
	private static Pattern OP_PAT = Pattern.compile("(union|intersect|difference|complement)");
	
	/**
	 * The complement of the node A is equal to the universal set (U) of nodes minue this node.
	 * This is expressed as U-A or the U\A. The universal set represents all the data available
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
		
	public static Collection<SetNode> setNodes(){
		return setNodes.values();
	}
	
	public static BTNode createTree(String equ) {
		Scanner scan = new Scanner(equ);
		scan.useDelimiter("(?<=\\()|(?>=\\))|\\s+");	
		BTNode root = null;

		try {
			root = recursion(scan);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		
		while(scan.hasNext()) {
			BuildTree.reportError("Invalid format: too many arguements are provided.");
			return null;
		}

		BuildTree.propagateSetNodes();
				
		scan.close();		
		return root;
	}
	
	/**
	 * 
	 * 
	 * @param scan, the scanner that traverses the expression
	 * @return, the root node of the binary tree
	 * @throws Exception throws an exception in the case of invalid formatting
	 */
	private static BTNode recursion(Scanner scan) throws Exception {
		if(!scan.hasNext()) return null;
		String str = scan.next().trim();
		
		if(str.isBlank()) return null;
		
		String[] subNodes = str.split("(\\(|\\))");
		if(subNodes.length > 1 || subNodes.length <= 0) {
			throw new Exception("Error in BuildTree/recursion: Invalid format!");
		}
		
		String element = subNodes[0];
		
		if(OP_PAT.matcher(element).matches()) {
			Operator op = parseOperator(element);
			
			if(op == null) {
				throw new Exception("Error in Tree/BuildTree/recursion: the operator '"+ element +"', is not supported yet!");
			}
			
			BTNode node = new BTNode(op);
			node.setLeft(recursion(scan));
			
			if(op instanceof Complement) {
				node.setRight(universalSet);
			} else {
				node.setRight(recursion(scan));
			}

			return node;
		}
		else {
			SetNode sn = new SetNode(element);
			if(!setNodes.containsKey(element)) {
				setNodes.put(element, sn);
				return sn;
			} else {
				return setNodes.get(element);
			}
		}
	}
	
	/**
	 * checks to see if an element is an operator, otherwise returns null
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
		return null;
	}	
	
		
	/**
	 * This method determines and initializes the central coordinate of a set.
	 * Then, every pixel within the sphere that represents a set is added to the 
	 * collection of pixels in that set. 
	 * Each pixel represents a piece of data within the set. 
	 */
	private static void propagateSetNodes() {
		int NUMBER_OF_SETS = setNodes.size();
		
		if(NUMBER_OF_SETS < 1) return;
		
		int RADIUS = SetNode.DIMEN/2;
				
		// the angle difference of each circle from the center 
		double ANGLE_OFFSET = 360/NUMBER_OF_SETS;
				
		int setCount = 0;
		for(SetNode sn : setNodes.values()) {
			// get the angle of the sets circle
			double angle = (setCount * ANGLE_OFFSET) - 90;
			double angleInRadians = angle * (Math.PI/180);
			
			// get the center coordinates of this sets spherical representation 
			int xCoord = BuildTree.startX + (int)(Math.cos(angleInRadians) * RADIUS/2);
			int yCoord = BuildTree.startY + (int)(Math.sin(angleInRadians) * RADIUS/2);
			
			Coordinate coord = new Coordinate(xCoord, yCoord);
			sn.setCenter(coord);
			
			
			// ---- get the position for the sets identifying string on the panel
			int strX = xCoord + (int) (Math.cos(angleInRadians) * RADIUS/1.25);
			int strY = yCoord + (int) (Math.sin(angleInRadians) * RADIUS/1.25);
			Coordinate stringCoord = new Coordinate(strX, strY);
			sn.setStringPosition(stringCoord);
			// ----
			
			
			// p = {(x,y) : (x-mX)^2 + (y-mY)^2 <= r^2}
			for(int x = xCoord-RADIUS; x < xCoord+RADIUS; x++) {
				for(int y = yCoord-RADIUS; y < yCoord+RADIUS; y++) {
					int xDifference = x - sn.center().x();	// x-mX
					int yDifference = y - sn.center().y();	// y-mY
					
					int value = xDifference * xDifference + yDifference * yDifference;
					int radiusSquared = RADIUS * RADIUS;
					
					// (x-mX)^2 + (y-mY)^2 <= r^2
					if(value <= radiusSquared) {
						Coordinate c = new Coordinate(x, y);
						sn.addPixel(c);
					}					
				}
			}
			setCount++;
		}
	}
	
	private static void reportError(String message) {
		System.out.println("----- ERROR -----");
		System.out.println(message);
	}
}
