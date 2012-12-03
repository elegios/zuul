package io;

import java.io.InputStream;
import java.util.Scanner;

import command.Command;
import command.CommandList;

public class Input {
	
	private Printer print;
	
	private Scanner in;
	private CommandList commandList;
	
	public Input(InputStream in, CommandList commandList, Printer print) {
		this.commandList = commandList;
		this.print = print;
		this.in = new Scanner(in);
	}
	
	public Command getNewCommand(int id, Command.Listener listener, 
			                             Command.Printer  printer) {
		print.askForCommand();
		String inputLine = in.nextLine();
		Command c = commandList.parse(inputLine, id, listener, printer);
		
		if (c == null) {
			print.invalidCommand(inputLine);
			return getNewCommand(id, listener, printer);
		}
		
		return c;
	}
	
	public interface Printer {
		void askForCommand();
		void invalidCommand(String command);
	}
	
}
