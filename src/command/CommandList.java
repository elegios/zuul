package command;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a list of Commands, more specifically
 * commands factories, i.e. those that respond to the parse
 * method and were created with the no argument constructor.
 */
public class CommandList {

	private List<Command> factories;
	
	public CommandList() {
		factories = new ArrayList<>();
	}
	
	public void add(Command commandFactory) { factories.add(commandFactory); }
	
	public Command parse(String in, int id, 
			             Command.Listener listener, 
			             Command.Printer  printer) {
		InputLine inputLine = new InputLine(in);
		
		for (Command factory : factories) {
			Command ret = factory.parse(inputLine, id, listener, printer);
			if (ret != null)
				return ret;
		}
		
		return null;
	}
	
}
