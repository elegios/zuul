package game;
import geodata.Direction;
import geodata.Room;
import io.Input;

import java.util.ArrayList;
import java.util.List;

import command.ChargeCommand;
import command.Command;
import command.CommandList;
import command.FireCommand;
import command.GoCommand;
import command.PutCommand;
import command.QuitCommand;
import command.TakeCommand;
import command.WaitCommand;

import event.EnterPointEvent;
import event.Event;
import event.KillFirstEvent;
import event.PointOnTurnEvent;
import event.WaitToSwingEvent;

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

public class Game implements io.Printer.PlayerContainer,
                               EnterPointEvent.Listener,
                              PointOnTurnEvent.Listener,
                                KillFirstEvent.Listener,
                              WaitToSwingEvent.Listener,
                                     GoCommand.Listener,
                                   WaitCommand.Listener,
                                 ChargeCommand.Listener,
                                   FireCommand.Listener,
                                   TakeCommand.Listener,
                                    PutCommand.Listener,
                                   QuitCommand.Listener {
	public static final int LAST_TURN  = 5;
	public static final int MAX_POINTS = 5;
	
	private CommandList commandList;
	private Input       in;
	private GamePrinter print;
	
	private boolean purgeAndInit;
	private boolean printRoom;
	
	private List<List<Command>> commandQueues;
	
	private Room       startRoom;
	private Room       chargedRoom;
	private List<Room> rooms;
	private int        numPlayers;
	private Player[]   players;
	private int        timer;
	private int        points;
	private boolean    keyTaken;

	/**
	 * Create the game and initialise its internal map.
	 */
	public Game() {
		commandQueues = new ArrayList<>();
		numPlayers    = 0;
		
		commandList = new CommandList();
		//this is where supported commands are added
		commandList.add(new   WaitCommand());
		commandList.add(new ChargeCommand());
		commandList.add(new   FireCommand());
		commandList.add(new   TakeCommand());
		commandList.add(new    PutCommand());
		commandList.add(new   QuitCommand());
		commandList.add(new     GoCommand());
		
		print = new io.Printer(this);
		in    = new Input(System.in, commandList, (Input.Printer) print);
	}
	
	private void purgeAndInitLater() { purgeAndInit = true; }
	private void printRoomLater()    { printRoom    = true; }
	
	/**
	 * 
	 */
	private void purgeAndInit() {
		purgeAndInit = false;
		numPlayers++;
		commandQueues.add(new ArrayList<Command>());
		timer  = -1;
		points =  0;
		chargedRoom = null;
		keyTaken    = false;
		
		createRooms();
		createPlayers();
		
		print.setCurrentPlayer(players[currId()], currId());
		printRoomLater();
	}

	/**
	 * Create all the rooms and link their exits together.
	 */
	private void createRooms() {
		rooms = new ArrayList<Room>();
		Room main, swingEast, swingSouth, swingTo, keyRoom, locked,
		     springFrom, springTo, spike, timeRiddle;
		
		Event.Printer printer = (Event.Printer) print;

		// create the rooms
		main = new Room("Main pillar");
		main.desc("You stand on the largest of the pillars");
		
		swingSouth = new Room("Fairly large pillar");
		swingSouth.desc("You're standing on a pillar that is only slightly smaller");
		swingSouth.desc("than the main pillar. To the east is a swinging rope.");
		swingSouth.desc("You're going to have to wait for an opportune moment");
		swingSouth.desc("to be able to catch it and swing to the pillar beyond");
		swingSouth.desc("it.");
		
		swingEast = new Room("Small pillar");
		swingEast.desc("The pillar you're standing on is quite small when compared");
		swingEast.desc("to the main pillar. To the south is a swinging rope. You're");
		swingEast.desc("going to have to wait for an opportune moment to be able to");
		swingEast.desc("catch it and swing to the pillar beyond it.");
		
		swingTo = new Room("Low pillar");
		swingTo.desc("The pillar you're standing on is quite low, which made it");
		swingTo.desc("quite easy to swing to it. Also, no bridges lead anywhere,");
		swingTo.desc("and you can't reach the rope anymore. You're trapped.");
		swingTo.addEvent(new EnterPointEvent(1, this, printer));
		
		WaitToSwingEvent swingEvent = new WaitToSwingEvent(swingTo, this, printer); 
		swingSouth.addEvent(swingEvent);
		swingEast .addEvent(swingEvent);
		
		springFrom = new Room("Pillar with a spring");
		springFrom.desc("Towards the west edge of this pillar is a platform with");
		springFrom.desc("a spring underneath. It seems like a precarious way to");
		springFrom.desc("travel, but hey, it might be cool.");
		
		springTo = new Room("Lonely pillar");
		springTo.desc("The pillar you're standing on is far away from all the others.");
		springTo.desc("You can not help but feel sorry for it. It is the forever alone");
		springTo.desc("pillar. It also presents a predicament for you: you're stuck.");
		springTo.addEvent(new EnterPointEvent(1, this, printer));
		
		spike = new Room("Spike pillar");
		spike.desc("This pillar is covered with spikes, but fortunately for you,");
		spike.desc("two previous you's are covering them enabling your safe passage.");
		spike.desc("It is slightly nauseating though.");
		spike.addEvent(new EnterPointEvent(3, this, printer));
		spike.addEvent(new KillFirstEvent(2, this, printer));
		
		timeRiddle = new Room("Engraved pillar");
		timeRiddle.desc("There is a message engraved on top of this pillar. It reads:");
		timeRiddle.desc("");
		timeRiddle.desc("  This thing all things devours:");
		timeRiddle.desc("  Birds, beasts, trees, flowers;");
		timeRiddle.desc("  Gnaws iron, bites steel;");
		timeRiddle.desc("  Grinds hard stones to meal;");
		timeRiddle.desc("  Slays king, ruins town,");
		timeRiddle.desc("  And beats high mountain down.");
		timeRiddle.addEvent(new PointOnTurnEvent(LAST_TURN, numPlayers, this, printer));
		
		keyRoom = new Room("Convex pillar");
		keyRoom.desc("The top of this pillar is convex, the ground leans towards the");
		keyRoom.desc("center. If you were a ball you would roll there. Incidentally");
		keyRoom.desc("there is a strange-looking sphere lying there. It looks like");
		keyRoom.desc("something that you might want to take and use on some other");
		keyRoom.desc("pillar.");
		
		locked = new Room("Pillar with a hole");
		locked.desc("In the center of this pillar you can see a hole. Upon closer");
		locked.desc("inspection you find that something spherical would fit");
		locked.desc("perfectly in there. Perhaps you can find such an object somewhere");
		locked.desc("and put it there?");
		
		//connect the rooms
		main.setTwoWayExit(Direction. EAST, swingEast);
		main.setTwoWayExit(Direction.SOUTH, swingSouth);
		main.setTwoWayExit(Direction. WEST, springFrom);
		main.setTwoWayExit(Direction.NORTH, spike);
		
		swingEast.setTwoWayExit(Direction.EAST, locked);
		
		swingSouth.setTwoWayExit(Direction.SOUTH, keyRoom);
		
		springFrom.setExit(Direction.WEST, springTo);
		
		spike.setTwoWayExit(Direction.NORTH, timeRiddle);
		
		startRoom = main;
		
		rooms.add(main);
		rooms.add(swingEast);
		rooms.add(swingSouth);
		rooms.add(swingTo);
		rooms.add(keyRoom);
		rooms.add(locked);
		rooms.add(springFrom);
		rooms.add(springTo);
		rooms.add(spike);
		rooms.add(timeRiddle);
	}
	
	private void createPlayers() {
		players = new Player[numPlayers];
		for (int i = 0; i < numPlayers; i++)
			players[i] = new Player(i, startRoom);
	}

	/**
	 * Main play routine. Loops until end of play.
	 */
	public void play() {
		print.welcome();
		
		purgeAndInit();

		while (true) {
			print.separator();
			
			if (purgeAndInit) {
				purgeAndInit();
				print.youAre(currId());
			}
			
			checkTime();
			checkPoints();
			if (purgeAndInit)
				continue;
			
			if (printRoom)
		    	((Room.Printer) print).room(players[currId()].getCurrentRoom());
			
			getNewCommand();
			processCommands();
			
			for (Room room : rooms)
				room.tick();
		}
	}
	
	private void getNewCommand() {
		Command c = in.getNewCommand(currId(), this, (Command.Printer) print);
		commandQueues.get(currId()).add(c);
	}
	
	private void processCommands() {
		for (List<Command> queue : commandQueues) {
			if (queue.size() > timer)
				queue.get(timer).give();
		}
	}
	
	private void checkTime() {
		if (++timer >= LAST_TURN) {
			print.endOfTime();
			purgeAndInitLater();
			
		} else {
			print.turnsLeft(LAST_TURN - timer);
		}
	}
	
	private void checkPoints() {
		if (points >= MAX_POINTS) {
			print.win(numPlayers);
		} else {
			print.points(points, MAX_POINTS);
		}
	}
	
	@Override
	public void awardPoint() {
		points++;
	}
	
	@Override
	public void kill(int id) {
		if (id == currId())
			purgeAndInitLater();
	}
	
	@Override
	public void forceToRoom(int id, Room room) {
		players[id].setCurrentRoom(room);
	}
	
	@Override
	public void charge(int id) {
		chargedRoom = players[id].getCurrentRoom();
	}
	
	@Override
	public void fire(int id) {
		players[id].setCurrentRoom(chargedRoom);
		
		isInTheVoid(id);
	}

	@Override
    public void go(int id, Direction direction) {
	    players[id].go(direction);
	    
	    if (isInTheVoid(id))
	    	return;
	    
	    if (id == currId())
	    	printRoomLater();
    }
	
	@Override
	public void doWait(int id) {
		players[id].doWait();
	}
	
	@Override
	public boolean take(int id) {
		if (!keyTaken &&
			players[id].getCurrentRoom().getTitle().equals("Convex pillar")) {
			
			keyTaken = true;
			players[id].setHasKey(true);
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean put(int id) {
		if (players[id].hasKey() && 
			players[id].getCurrentRoom().getTitle().equals("Pillar with a hole")) {
			
			players[id].setHasKey(false);
			return true;
		}
		
		return false;
	}
	
	@Override
	public Player getPlayer(int id) {
		return players[id];
	}
	
	@Override
	public void quit() {
		System.exit(0);
	}
	
	
	private boolean isInTheVoid(int id) {
		if (!players[id].inARoom()) {
	    	print.fallToDeath(id);
	    	if (id == currId())
	    		purgeAndInitLater();
	    	return true;
	    }
		
		return false;
	}
	
	private int currId() { return numPlayers - 1; }
	
	public static void main(String[] args) {
		new Game().play();
	}
	
}
