package command;

public class PutCommand extends Command {
	//variables and settings for the factory part of GoCommand
	public static final String WORD = "put";
	
	public PutCommand() { super(WORD); }

	@Override
	public Command parse(InputLine inputLine, int id, 
			             Command.Listener listener, 
			             Command.Printer printer) {
		
		if (defaultParse(inputLine)) {
			inputLine.finalizeClaim();
			return new PutCommand(id, listener, printer);
		}
		
		//nothing worked, the inputline cannot be parsed as a WaitCommand
		inputLine.releaseClaim();
		return null;
	}
	
	//variables and settings for the actual instance of GoCommand
	private Listener  listener;
	private Printer   print;
	private int       id;
	
	public PutCommand(int id,
			           Command.Listener listener, 
			           Command.Printer  printer) {
		this.id        = id;
		
		this.listener  = (Listener) listener;
		this.print     = (Printer)  printer;
	}

	@Override
	public void give() {
		boolean success = listener.put(id);
		print.put(id, success);
		if (success) {
			listener.awardPoint();
			print   .awardPoint(id);
		}
	}
	
	public interface Listener extends Command.Listener {
		boolean put(int id);
		void awardPoint();
	}
	
	public interface Printer extends Command.Printer {
		void put(int id, boolean successful);
		void awardPoint(int id);
	}

}
