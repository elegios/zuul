import geodata.Direction;
import geodata.Room;

import java.util.ArrayList;
import java.util.List;

import command.Command;
import command.CommandWord;

/**
 * This class is the main class of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game. Users can walk
 * around some scenery. That's all. It should really be extended to make it more
 * interesting!
 * 
 * To play this game, create an instance of this class and call the "play"
 * method.
 * 
 * This main class creates and initialises all the others: it creates all rooms,
 * creates the parser and starts the game. It also evaluates and executes the
 * commands that the parser returns.
 * 
 * @author Michael Kölling and David J. Barnes
 * @version 2011.08.10
 */

public class Game {
	private Parser parser;
	private Print  print;
	
	private List<List<Command>> commandLists;
	
	private Room     startRoom;
	private int      numPlayers;
	private Player[] players;
	private int      timer;

	/**
	 * Create the game and initialise its internal map.
	 */
	public Game(Print print) {
		numPlayers = 0;
		parser     = new Parser();
		this.print = print;
		commandLists = new ArrayList<>();
		purgeAndInit();
	}
	
	/**
	 * 
	 */
	private void purgeAndInit() {
		numPlayers++;
		commandLists.add(new ArrayList<Command>());
		timer = 0;
		createRooms();
		createPlayers();
	}

	/**
	 * Create all the rooms and link their exits together.
	 */
	private void createRooms() {
		Room outside, theater, pub, lab, office;

		// create the rooms
		outside = new Room("outside the main entrance of the university");
		theater = new Room("in a lecture theater");
		pub = new Room("in the campus pub");
		lab = new Room("in a computing lab");
		office = new Room("in the computing admin office");

		// initialise room exits
		outside.setExit(Direction.EAST, theater);
		outside.setExit(Direction.SOUTH, lab);
		outside.setExit(Direction.WEST, pub);

		theater.setExit(Direction.WEST, outside);

		pub.setExit(Direction.EAST, outside);

		lab.setExit(Direction.NORTH, outside);
		lab.setExit(Direction.EAST, office);

		office.setExit(Direction.WEST, lab);
	}
	
	private void createPlayers() { //TODO
		players = new Players[numPlayers];
		for (int i = 0; i < numPlayers; i++)
			players[i] = new Player(startRoom);
	}

	/**
	 * Main play routine. Loops until end of play.
	 */
	public void play() {
		printWelcome();

		// Enter the main command loop. Here we repeatedly read commands and
		// execute them until the game is over.

		while (true) {
			
		}
	}

	/**
	 * Print out the opening message for the player.
	 */
	private void printWelcome() {
		System.out.println();
		System.out.println("Welcome to the World of Zuul!");
		System.out
				.println("World of Zuul is a new, incredibly boring adventure game.");
		System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
		System.out.println();
		System.out.println(players[numPlayers-1].getCurrentRoom().getLongDescription());
	}

	/**
	 * Given a command, process (that is: execute) the command.
	 * 
	 * @param command
	 *            The command to be processed.
	 * @return true If the command ends the game, false otherwise.
	 */
	private void processCommand(int playerId, Command command) {
		//actionable commands
		switch (command.getCommandWord()) {
			case GO:
				
				break;
				
			case QUIT:
				quit();
		}
		
		//printable commands for current player
		if (playerId == numPlayers - 1) {
			switch (command.getCommandWord()) {
				case GO:
					print.go(command);
					break;
					
				case HELP:
					print.help();
					break;
					
				case UNKNOWN:
					print.unknown(print.YOU);
			}
		} else {
			switch (command.getCommandWord()) {
				case GO:
					print.go(print.YOU +" #"+ (playerId + 1), command);
			}
		}
	}
	
	private void quit() {
		System.out.println("Thank you for playing. Do try again soon.");
		System.exit(0);
	}
}
