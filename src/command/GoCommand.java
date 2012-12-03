package command;

import geodata.Direction;

public class GoCommand extends Command {
	//variables and settings for the factory part of GoCommand
	public static final String WORD = "go";
	
	public GoCommand() { super(WORD); }

	@Override
	public Command parse(InputLine inputLine, int id, 
			             Command.Listener listener, 
			             Command.Printer printer) {
		//handles the "go <direction>" syntax
		if (defaultParse(inputLine)) {
			GoCommand ret = new GoCommand(Direction.getDirection(inputLine.claimNext()), id,
					                      listener, printer);
			if (ret.direction != null) {
				inputLine.finalizeClaim();
				return ret;
			} else
				inputLine.releaseClaim();
		}
		
		//handles the "<direction>" syntax
		Direction direction = Direction.getDirection(inputLine.claimNext());
		if (direction != null) {
			GoCommand ret = new GoCommand(direction, id, listener, printer);
			if (ret.direction != null) {
				inputLine.finalizeClaim();
				return ret;
			} else
				inputLine.releaseClaim();
		}
		
		//nothing worked, the inputline cannot be parsed as a GoCommand
		inputLine.releaseClaim();
		return null;
	}
	
	//variables and settings for the actual instance of GoCommand
	private Direction direction;
	private Listener  listener;
	private Printer   print;
	private int       id;
	
	public GoCommand(Direction direction, int id,
			         Command.Listener listener, 
			         Command.Printer printer) {
		this.direction = direction;
		this.id        = id;
		
		this.listener  = (Listener) listener;
		this.print     = (Printer)  printer;
	}

	@Override
	public void give() {
		print.beforeGo(id, direction);
		listener   .go(id, direction);
		print .afterGo(id, direction);
	}
	
	public interface Listener extends Command.Listener {
		void go(int id, Direction direction);
	}
	
	public interface Printer extends Command.Printer {
		void beforeGo(int id, Direction direction);
		void  afterGo(int id, Direction direction);
	}

}
