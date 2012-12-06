package event;

import geodata.Room;

/**
 * An event that makes a player swing (teleport) to another room
 * after issuing a wait command in the room in which this event
 * resides.
 */
public class WaitToSwingEvent implements Event {
	
	private Room room;
	private Listener listener;
	private Printer  print;
	
	public WaitToSwingEvent(Room toRoom, Event.Listener listener, Event.Printer printer) {
		room = toRoom;
		
		this.listener = (Listener) listener;
		this.print    = (Printer)  printer;
	}

	@Override
	public void playerWaited(int id) {
		print   .swingTo(id);
		listener.forceToRoom(id, room);
	}
	
	public interface Listener extends Event.Listener {
		void forceToRoom(int id, Room room);
	}
	
	public interface Printer extends Event.Printer {
		void swingTo(int id);
	}

	@Override
	public void addedToRoom(Room room) {}
	public void playerEntered(int id) {}
	public void tick() {}
	public void playerLeft(int id) {}

}
