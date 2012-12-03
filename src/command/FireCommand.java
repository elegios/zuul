package command;

public class FireCommand extends Command {
	//variables and settings for the factory part of GoCommand
	public static final String WORD = "fire";
	
	public FireCommand() { super(WORD); }

	@Override
	public Command parse(InputLine inputLine, int id, 
			             Command.Listener listener, 
			             Command.Printer printer) {
		
		if (defaultParse(inputLine)) {
			inputLine.finalizeClaim();
			return new FireCommand(id, listener, printer);
		}
		
		//nothing worked, the inputline cannot be parsed as a WaitCommand
		inputLine.releaseClaim();
		return null;
	}
	
	//variables and settings for the actual instance of GoCommand
	private Listener  listener;
	private Printer   print;
	private int       id;
	
	public FireCommand(int id,
			           Command.Listener listener, 
			           Command.Printer  printer) {
		this.id        = id;
		
		this.listener  = (Listener) listener;
		this.print     = (Printer)  printer;
	}

	@Override
	public void give() {
		print   .fire(id);
		listener.fire(id);
	}
	
	public interface Listener extends Command.Listener {
		void fire(int id);
	}
	
	public interface Printer extends Command.Printer {
		void fire(int id);
	}

}
