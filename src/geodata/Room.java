package geodata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import event.Event;

/**
 * Class Room - a room in an adventure game.
 * 
 * A "Room" represents one location in the scenery of the game. It is connected
 * to other rooms via exits. For each existing exit, the room stores a reference
 * to the neighboring room.
 * 
 * A Room can also contain events that might trigger when certain things happen.
 * These events require that some trigger methods are called when they should,
 * namely playerEntered(int), playerLeft(int) and tick(). See the description for
 * each of these for when they should be called.
 */

public class Room {
	
	private String title;
	private String description;
	private HashMap<Direction, Room> exits; // stores exits of this room.
	
	private List<Event> events;

	/**
	 * Create a room with the given title. Description and exits
	 * have to be provide with other methods, desc(String) and
	 * setExit(Direction, Room) or setTwoWayExit(Direction, Room)
	 * respectively.
	 * 
	 * @param title the title of this room
	 */
	public Room(String title) {
		this.title = title;
		description = "";
		exits = new HashMap<>();
		
		events = new ArrayList<>();
	}

	/**
	 * Define an exit from this room to other.
	 * 
	 * @param direction The direction of the exit.
	 * @param neighbor The room to which the exit leads.
	 */
	public void setExit(Direction direction, Room neighbor) {
		exits.put(direction, neighbor);
	}
	
	/**
	 * Defines an exit from this room to other and an opposite
	 * exit from other to this room. 
	 * @param direction
	 * @param other
	 */
	public void setTwoWayExit(Direction direction, Room other) {
		setExit(direction, other);
		other.setExit(Direction.getInversion(direction), this);
	}
	
	/**
	 * Appends a String to the description of this room.
	 * A newline character is appended after it, meaning
	 * the last character of the description will always
	 * be a newline.
	 * @param description the String to be appended to the description
	 */
	public void desc(String description) {
		this.description += description + "\n";
	}
	
	/**
	 * Adds an event to the current room.
	 * @param event the Event to add
	 */
	public void addEvent(Event event) {
		events.add(event);
		event.addedToRoom(this);
	}
	
	/**
	 * Should be called once per turn.
	 */
	public void tick() {
		for (Event event : events)
			event.tick();
	}
	
	/**
	 * Should be called when a player has entered the room
	 * @param id the id of the player that entered
	 */
	public void playerEntered(int id) {
		for (Event event : events)
			event.playerEntered(id);
	}
	
	/**
	 * Should be called when a player has left this room.
	 * @param id the id of the player that left
	 */
	public void playerLeaving(int id) {
		for (Event event : events)
			event.playerLeft(id);
	}
	
	public void playerWaited(int id) {
		for (Event event : events)
			event.playerWaited(id);
	}

	public String getTitle      () { return       title; }
	public String getDescription() { return description; }

	/**
	 * Return the room that is reached if we go from this room in direction
	 * "direction". If there is no room in that direction, return null.
	 * 
	 * @param direction The exit's direction.
	 * @return The room in the given direction.
	 */
	public Room getExit(Direction direction) {
		return exits.get(direction);
	}
	
	public Direction[] getExits() {
		return exits.keySet().toArray(new Direction[exits.size()]);
	}
	
	public interface Printer {
		void room(Room room);
	}
}
