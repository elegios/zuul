package event;

import geodata.Room;

public class PointOnTurnEvent implements Event {
	
	private boolean[] playerInRoom; 
	private int currentTurn;
	private int pointOnTurn;
	
	private Listener listener;
	private Printer  print;
	
	public PointOnTurnEvent(int turn, int numPlayers, Event.Listener listener, Event.Printer printer) {
		pointOnTurn = turn;
		currentTurn = 0;
		
		this.listener = (Listener) listener;
		this.print    = (Printer)  printer;
		
		playerInRoom = new boolean[numPlayers];
	}

	@Override
	public void playerEntered(int id) {
		playerInRoom[id] = true;
	}

	@Override
	public void tick() {
		if (++currentTurn == pointOnTurn) {
			for (int i = 0; i < playerInRoom.length; i++) {
				if (playerInRoom[i]) {
					listener.awardPoint();
					print.awardPointOnTurn(i);
					return;
				}
			}
		}
	}

	@Override
	public void playerLeft(int id) {
		playerInRoom[id] = false;
	}
	
	public interface Listener extends Event.Listener {
		void awardPoint();
	}
	
	public interface Printer extends Event.Printer {
		void awardPointOnTurn(int id);
	}

	@Override
	public void addedToRoom(Room room) {}
	public void playerWaited(int id)   {}

}
