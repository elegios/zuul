package command;

public class ChargeCommand extends Command {
	//variables and settings for the factory part of GoCommand
	public static final String WORD = "charge";
	
	public ChargeCommand() { super(WORD); }

	@Override
	public Command parse(InputLine inputLine, int id, 
			             Command.Listener listener, 
			             Command.Printer printer) {
		
		if (defaultParse(inputLine)) {
			inputLine.finalizeClaim();
			return new ChargeCommand(id, listener, printer);
		}
		
		//nothing worked, the inputline cannot be parsed as a WaitCommand
		inputLine.releaseClaim();
		return null;
	}
	
	//variables and settings for the actual instance of GoCommand
	private Listener  listener;
	private Printer   print;
	private int       id;
	
	public ChargeCommand(int id,
			           Command.Listener listener, 
			           Command.Printer  printer) {
		this.id        = id;
		
		this.listener  = (Listener) listener;
		this.print     = (Printer)  printer;
	}

	@Override
	public void give() {
		print   .charge(id);
		listener.charge(id);
	}
	
	public interface Listener extends Command.Listener {
		void charge(int id);
	}
	
	public interface Printer extends Command.Printer {
		void charge(int id);
	}

}
