package event;

import geodata.Room;

public class EnterPointEvent implements Event {
	
	private Listener listener;
	private Printer  print;
	private int      timesToEnter;

	public EnterPointEvent(int timesToEnter, Event.Listener listener, Event.Printer printer) {
		this.timesToEnter = timesToEnter;
		
		this.listener = (Listener) listener;
		this.print    = (Printer)  printer;
	}

	@Override
	public void playerEntered(int id) {
		if (--timesToEnter == 0) {
			listener.awardPoint();
			print   .awardPoint(id);
		}
	}
	
	public interface Listener extends Event.Listener {
		void awardPoint();
	}
	
	public interface Printer extends Event.Printer {
		void awardPoint(int id);
	}
	
	@Override
	public void addedToRoom(Room room) {}
	public void tick()                 {}
	public void playerWaited(int id)   {}
	public void playerLeft(int id)     {}

}
