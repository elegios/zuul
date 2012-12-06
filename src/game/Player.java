package game;
import geodata.Direction;
import geodata.Room;

/**
 * A basic class to describe a Player in a room that
 * might be carrying a key.
 */
public class Player {
	
	private Room    currentRoom;
	private int     id;
	private boolean hasKey;
	
	public Player(int id, Room room) {
		this.id     = id;
		currentRoom = room;
		hasKey      = false;
	}
	
	public void go(Direction direction) {
		if (currentRoom != null) {
			setCurrentRoom(currentRoom.getExit(direction));
		}
	}
	
	public void doWait() {
		if (currentRoom != null)
			currentRoom.playerWaited(id);
	}
	
	public void setCurrentRoom(Room room) {
		if (currentRoom != null) {
			currentRoom.playerLeaving(id);
			currentRoom = room;
			if (currentRoom != null)
				currentRoom.playerEntered(id);
		}
	}
	
	public boolean inARoom() { return currentRoom != null; }
	
	public boolean inSameRoomAs(Player player) {
		return currentRoom == player.currentRoom && currentRoom != null;
	}

	public Room getCurrentRoom() { return currentRoom; }

	public void setHasKey(boolean hasKey) { this.hasKey = hasKey; }
	
	public boolean hasKey() { return hasKey; }
	
}
