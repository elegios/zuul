package event;

import geodata.Room;

/**
 * This interface describes an event that can be placed in a Room.
 * It has a set of callbacks used for determining whether the
 * conditions for the event has been triggered.
 * 
 * The Listener and Printer interfaces are subclassed as required
 * by the different implementations of the Event interface.
 * Objects that conform to these subclassed interfaces should
 * generally be passed as arguments to the constructor of the
 * implementation, but this might be handled differently for
 * each implementation.
 * @author elegios
 *
 */
public interface Event {
	
	/**
	 * Should be called by the room to which this event is added.
	 * @param room the Room to which this event is added.
	 */
	public void addedToRoom(Room room);
	
	/**
	 * Should be called by the room in which this event resides
	 * when a player enters the room.
	 * @param id the id of the player
	 */
	public void playerEntered(int id);
	
	/**
	 * Should be called by the room in which this event resides
	 * once per turn.
	 */
	public void tick();
	
	/**
	 * Should be called by the room in which this event resides
	 * when a player leaves the room.
	 * @param id the id of the player
	 */
	public void playerLeft(int id);
	
	/**
	 * Should be called by the rom in which this event resides
	 * when a player used the wait command in the room
	 * @param id the id of the player
	 */
	public void playerWaited(int id);
	
	public interface Listener {};
	public interface Printer  {};

}
