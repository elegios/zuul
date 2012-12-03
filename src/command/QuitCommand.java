package command;

public class QuitCommand extends Command {
	//variables and settings for the factory part of GoCommand
	public static final String WORD = "quit";
	
	public QuitCommand() { super(WORD); }

	@Override
	public Command parse(InputLine inputLine, int id,
			             Command.Listener listener, 
			             Command.Printer printer) {
		
		if (defaultParse(inputLine)) {
			inputLine.finalizeClaim();
			return new QuitCommand(listener, printer);
		}
		
		//nothing worked, the inputline cannot be parsed as a WaitCommand
		inputLine.releaseClaim();
		return null;
	}
	
	//variables and settings for the actual instance of GoCommand
	private Listener  listener;
	private Printer   print;
	
	public QuitCommand(Command.Listener listener, 
			           Command.Printer  printer) {
		this.listener  = (Listener) listener;
		this.print     = (Printer)  printer;
	}

	@Override
	public void give() {
		print   .quit();
		listener.quit();
	}
	
	public interface Listener extends Command.Listener {
		void quit();
	}
	
	public interface Printer extends Command.Printer {
		void quit();
	}

}
