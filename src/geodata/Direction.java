package geodata;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public enum Direction {
	NORTH("north"), EAST("east"), SOUTH("south"), WEST("west");
	
	private static Map<Direction, Direction> inversions;
	
	private static void initInversions() {
		inversions = new HashMap<>();
		
		inversions.put(NORTH, SOUTH);
		inversions.put(WEST, EAST);
		
		for (Entry<Direction, Direction> entry : inversions.entrySet())
			inversions.put(entry.getValue(), entry.getKey());
	}
	
	private String text;
	
	private Direction(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	public static Direction getDirection(String direction) {
		for (Direction dir : values()) {
			if (dir.toString().toLowerCase().startsWith(direction.toLowerCase()))
				return dir;
		}
		
		return null;
	}
	
	public static Direction getInversion(Direction direction) {
		if (inversions == null)
			initInversions();
		
		return inversions.get(direction);
	}
}
