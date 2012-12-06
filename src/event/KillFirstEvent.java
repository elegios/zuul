package event;

import geodata.Room;

/**
 * An event that kills the first few players that enter
 * the room in which this event resides.
 */
public class KillFirstEvent implements Event {
	
	private int leftToKill;
	private Listener listener;
	private Printer  print;
	
	public KillFirstEvent(int numToKill, Event.Listener listener, Event.Printer printer) {
		leftToKill = numToKill;
		this.listener = (Listener) listener;
		this.print    = (Printer)  printer;
	}

	@Override
	public void playerEntered(int id) {
		if (leftToKill-- > 0) {
			listener.kill(id);
			print.spikeKill(id);
		}
	}
	
	public interface Listener extends Event.Listener {
		void kill(int id);
	}
	
	public interface Printer extends Event.Printer {
		void spikeKill(int id);
	}

	@Override
	public void addedToRoom(Room room) {}
	public void tick()                 {}
	public void playerWaited(int id)   {}
	public void playerLeft(int id)     {}

}
