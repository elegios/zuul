import java.util.Scanner;

import command.Command;
import command.CommandWords;

/**
 * This class is part of the "World of Zuul" application. "World of Zuul" is a
 * very simple, text based adventure game.
 * 
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and tries
 * to interpret the line as a two-word command. It returns the command as an
 * object of class Command.
 * 
 * The parser has a set of known command words. It checks user input against the
 * known commands, and if the input is not one of the known commands, it returns
 * a command object that is marked as an unknown command.
 * 
 * @author Michael K��lling and David J. Barnes
 * @version 2011.08.10
 */
public class Parser {
	private Scanner reader; // source of command input

	/**
	 * Create a parser to read from the terminal window.
	 */
	public Parser() {
		reader = new Scanner(System.in);
	}

	/**
	 * @return The next command from the user.
	 */
	public Command getCommand() {
		String[] inputLine; // will hold the full input line

		System.out.print("> "); // print prompt

		inputLine = reader.nextLine().split(" ");

		return new Command(CommandWords.getCommandWord(inputLine[0]), inputLine[1]);
	}

	/**
	 * Print out a list of valid command words.
	 */
	public void showCommands() {
		CommandWords.showAll();
	}
}
