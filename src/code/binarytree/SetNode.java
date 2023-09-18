package code.binarytree;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SetNode extends BTNode{
	
	/**
	 * The unique identifier of a set, will always be an uppercase letter. 
	 */
	private String identifier;

	/**
	 * The collection of all pixels in the set node, represents the "data" of this set.
	 */
	private Set<Coordinate> pixels;

	/**
	 * The coordinate of the center of the circle that graphically represents this set.
	 */
	private Coordinate center;

	/**
	 * The coordinate for the identifier to be graphically displayed. 
	 */
	private Coordinate toStringPosition;

	/**
	 * The diameter of the circle that graphically represents a set. 
	 */
	public static final int DIAMETER = 150;
	
	/**
	 * @param identifier, the unique id for this set
	 */
	public SetNode(String identifier) {
		this.identifier = identifier;
		pixels = new HashSet<>();
	}
	
	/**
	 * Evaluating a set node means only to retrieve its "data" which is 
	 * represented by the collection of Coordinates (pixels).
	 */
	@Override
	public Set<Coordinate> evaluate(){
		return Collections.unmodifiableSet(pixels);
	}
	
	/**
	 * Add a pixel to this sets pixels.
	 * 
	 * @param coord, the coordinate that represents the pixel.
	 */
	public void addPixel(Coordinate coord) {
		pixels.add(coord);
	}
	
	// ----- getters and setters ----- //
	public void setCenter(Coordinate center) {
		this.center = center;
	}
	
	public Coordinate center() {
		return center;
	}
	
	public void setStringPosition(Coordinate strPosition) {
		toStringPosition = strPosition;
	}
	
	public Coordinate stringPosition() {
		return toStringPosition;
	}
		
	public String toString() {
		return identifier;
	}
}
