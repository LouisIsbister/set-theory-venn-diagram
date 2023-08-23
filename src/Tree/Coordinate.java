package Tree;

public record Coordinate (int x, int y) {
	public String toString(){
		return "Coordinate, x = " + x + ", y = " + y;
	}
}
