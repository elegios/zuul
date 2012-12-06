package geodata;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This enum contains objects representing directions. The
 * directions can be obtained either directly, using
 * Direction.\<NAME\>, by using getDirection(String) to get
 * the best match by name or by getting the inversion of
 * an already existing Direction with getInversion(Direction).
 */
public enum Direction {
	NORTH("north"), EAST("east"), SOUTH("south"), WEST("west");
	
	private static Map<Direction, Direction> inversions;
	
	private static void initInversions() {
		inversions = new HashMap<>();
		
		inversions.put(NORTH, SOUTH);
		inversions.put(WEST, EAST);
		
		Map<Direction, Direction> tempInversions = new HashMap<>();
		
		for (Entry<Direction, Direction> entry : inversions.entrySet())
			tempInversions.put(entry.getValue(), entry.getKey());
		
		inversions.putAll(tempInversions);
	}
	
	private String text;
	
	private Direction(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	/**
	 * Gets a Direction object based on the provided String. This string
	 * may either be a perfect match with the Direction's name or the first
	 * few characters of the Direction's name. The match is not case-sensitive.
	 * @param direction the String describing the Direction
	 * @return
	 */
	public static Direction getDirection(String direction) {
		if (direction != null) {
			for (Direction dir : values()) {
				if (dir.toString().toLowerCase().startsWith(direction.toLowerCase()))
					return dir;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the opposite Direction. For example, getInversion(NORTH) 
	 * will return SOUTH.
	 * @param direction the Direction to be inverted
	 * @return the inverted Direction
	 */
	public static Direction getInversion(Direction direction) {
		if (inversions == null)
			initInversions();
		
		return inversions.get(direction);
	}
}
