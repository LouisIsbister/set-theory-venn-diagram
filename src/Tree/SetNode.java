package Tree;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SetNode extends BTNode{
	
	private String identifier;
	private Set<Coordinate> pixels;

	public static final int DIMEN = 150;

	private Coordinate center;
	private Coordinate toStringPosition;
	
	public SetNode(String id) {
		this.identifier = id;
		pixels = new HashSet<>();
	}
	
	@Override
	public Set<Coordinate> evaluate(){
		return Collections.unmodifiableSet(pixels);
	}
		
	public void addPixel(Coordinate coord) {
		pixels.add(coord);
	}
	
	public void setCenter(Coordinate center) {
		this.center = center;
	}
	
	public Coordinate center() {
		return this.center;
	}
	
	public Coordinate stringPosition() {
		return this.toStringPosition;
	}
	
	public void setStringPosition(Coordinate c) {
		this.toStringPosition = c;
	}
	
	public String toString() {
		return identifier;
	}
}
