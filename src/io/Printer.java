package io;

import event.EnterPointEvent;
import event.KillFirstEvent;
import event.PointOnTurnEvent;
import event.WaitToSwingEvent;
import game.GamePrinter;
import game.Player;
import geodata.Direction;
import geodata.Room;

import java.util.Random;

import command.ChargeCommand;
import command.FireCommand;
import command.GoCommand;
import command.PutCommand;
import command.QuitCommand;
import command.TakeCommand;
import command.WaitCommand;

public class Printer implements       GamePrinter,
                                    Input.Printer,
                                     Room.Printer,
                                GoCommand.Printer,
                              WaitCommand.Printer,
                            ChargeCommand.Printer,
                              FireCommand.Printer,
                              TakeCommand.Printer,
                               PutCommand.Printer,
                              QuitCommand.Printer,
                          EnterPointEvent.Printer,
                           KillFirstEvent.Printer,
                         PointOnTurnEvent.Printer,
                         WaitToSwingEvent.Printer {

	private int	   currentPlayerId;
	private Player currentPlayer;
	private PlayerContainer players;
	
	private Random rand;

	public Printer(PlayerContainer players) {
		this.players  = players;
		currentPlayerId = 0;
		
		rand = new Random();
	}

	@Override
	public void setCurrentPlayer(Player player, int id) {
		currentPlayer   = player;
		currentPlayerId = id;
	}

	@Override
	public void beforeGo(int id, Direction direction) {
		if (id == currentPlayerId) {
			pl("You head " +direction+ ".");
			
		} else if (inSameRoomAsCurrentPlayer(id)){
			pl(youCap(id) +" heads "+ direction +".");
		}
	}
	
	@Override
	public void afterGo(int id, Direction direction) {
		if (id != currentPlayerId && inSameRoomAsCurrentPlayer(id)) {
			pl(youCap(id) +" enters from the "+ Direction.getInversion(direction));
		}
	}
	
	@Override
	public void youAre(int id) {
		pl("You are currently " +you(id)+ ".");
	}

	@Override
    public void welcome() {
	    pl();
	    pl("Welcome to the world of Zuul.");
	    pl("Zuul is a gamemaster. He's tasked you to complete");
	    pl("a slightly paradoxical challenge. You have five");
	    pl("commands, then you die.");
	    pl();
	    pl("The challenge is set on a couple of pillars in an");
	    pl("otherwise absolute void. There are no walls; walk");
	    pl("over the edge and you will fall. For every cool");
	    pl("thing you accomplish you get a point. Get as many");
	    pl("as possible, points are awesome. Good luck.");
	    pl("");
	    pl("Also, you have a beamer. Charge and fire!");
    }
	
	@Override
	public void turnsLeft(int turnsLeft) {
		if (turnsLeft > 1)
			pl(turnsLeft +" commands to go until the end of time.");
		else
			pl("Just one more command!");
	}
	
	@Override
	public void endOfTime() {
		pl("Time's up! Everything around you vanishes, and you");
		pl("fall to your death.");
	}

	@Override
    public void quit() {
	    pl("Zuul says: Leaving already? Oh well, see you soon!");
    }

	@Override
    public void awardPointOnTurn(int id) {
		if (id == currentPlayerId) {
			pl("Zuul says: Well, well, what have we here? A clever one");
			pl("           it would seem. You solved the riddle! That was");
			pl("           cool, have a point!");
		} else {
			awardPoint(id);
		}
    }

	@Override
    public void spikeKill(int id) {
	    if (id == currentPlayerId) {
	    	pl("You fall and the spikes that cover this pillar pierce through");
	    	pl("you. You're dead.");
	    } else {
	    	pl(youCap(id) +" dies on some spikes.");
	    }
    }

	@Override
    public void awardPoint(int id) {
	    if (id == currentPlayerId) {
	    	p("Zuul says: ");
	    	switch (rand.nextInt(4)) {
	    		case 0:
	    			pl("That was awesome! Have a point!");
	    			return;
	    			
	    		case 1:
	    			pl("Cool stuff! A point for you!");
	    			return;
	    			
	    		case 2:
	    			pl("You're getting the hang of this. Point!");
	    			return;
	    			
	    		case 3:
	    			pl("I see a point coming your way!");
	    			return;
	    	}
	    } else {
	    	pl(youCap(id) +" got you a point.");
	    }
    }
	
	@Override
	public void points(int currentPoints, int maxPoints) {
		pl("You have "+ currentPoints +"/"+ maxPoints +" points.");
	}

	@Override
    public void waited(int id) {
	    if (id == currentPlayerId) {
	    	pl("You wait a while.");
	    	
	    } else if (inSameRoomAsCurrentPlayer(id)) {
	    	pl(youCap(id) +" waits here a while.");
	    }
    }
	


	@Override
    public void swingTo(int id) {
	    if (id == currentPlayerId) {
	    	pl("The opportune moment comes, you seize it (and the rope) and");
	    	pl("swing over to the other pillar.");
	    	
	    } else if (inSameRoomAsCurrentPlayer(id)) {
	    	pl(youCap(id) +" grabs the swinging rope and soars off, Indiana Jones style.");
	    }
    }

	@Override
    public void fallToDeath(int id) {
		if (id == currentPlayerId) {
			pl("You fall to your death.");
			
		} else {
			pl(youCap(id) +" falls to the void below.");
		}
    }

	@Override
    public void fire(int id) {
		if (id == currentPlayerId) {
			pl("You fire up the beamer and vanish from where you were.");
		} else if (inSameRoomAsCurrentPlayer(id)) {
			pl(youCap(id) +" fires the beamer and vanishes.");
		}
    }

	@Override
    public void charge(int id) {
		if (id == currentPlayerId) {
			pl("You charge the beamer. All later fires of the beamer will teleport to here.");
		} else {
			p(youCap(id) +" charges the beamer on the ");
			pl(players.getPlayer(id).getCurrentRoom().getTitle().toLowerCase() +".");
		}
    }

	@Override
    public void take(int id, boolean successful) {
	    if (id == currentPlayerId) {
	    	if (successful) {
	    		pl("You take the sphere. Now to find somewhere to put it.");
	    	} else {
	    		pl("There's nothing to take.");
	    	}
	    } else if (inSameRoomAsCurrentPlayer(id)) {
	    	if (successful) {
	    		pl(youCap(id) +" takes the sphere.");
	    	} else {
	    		pl(youCap(id) +" tries to take something, but there's nothing to take. Stupid " +you(id));
	    	}
	    }
    }

	@Override
    public void put(int id, boolean successful) {
	    if (id == currentPlayerId) {
	    	if (successful) {
	    		pl("You put the sphere in the hole. It fits perfectly. Cool.");
	    	} else {
	    		pl("You're not putting anything here...");
	    	}
	    } else if (inSameRoomAsCurrentPlayer(id)) {
	    	if (successful) {
	    		pl(youCap(id) +" puts the sphere in the hole.");
	    	} else {
	    		pl(youCap(id) +" seems to want to put something somewhere. Not happening.");
	    	}
	    }
    }
	
	@Override
	public void win(int numberOfPlayers) {
		pl("Zuul says: Congratulations! You did all the cool things this world");
		pl("           to offer! It was pretty fun to watch you run around, ");
		pl("           all "+ numberOfPlayers +"of you. Come back some time!");
		pl();
		pl("Everything around you vanishes, only this time you vanish too. Have");
		pl("fun in your non-existance!");
	}

	@Override
    public void room(Room room) {
		p("_");
		for (int i = 0; i < room.getTitle().length(); i++)
			p("_");
		pl("_");
		pl("|" +room.getTitle()+ "|");
		p("-");
		for (int i = 0; i < room.getTitle().length(); i++)
			p("-");
		pl("-");
		
	    pl(room.getDescription());
	    
	    pl("There are don't-fall-to-your-death routes in these directions:");
	    p("  ");
	    for (Direction dir : room.getExits())
	    	p(dir + " ");
	    pl();
    }

	@Override
	public void askForCommand() {
		p("> ");
	}

	@Override
	public void invalidCommand(String command) {
		pl("Something is wrong with \"" +command+"\", I cannot understand it.");
	}
	
	@Override
	public void separator() {
		pl("-----------------------------");
		pl();
		try {
	        Thread.sleep(1000);
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
	}

	private void p (String... strings) {
		for (String s : strings)
			System.out.print(s);
	}
	private void pl(String... strings) {
		p(strings);
		System.out.println();
	}
	
	private String you   (int id) { return "you #" +(id+1); }
	private String youCap(int id) { return "You #" +(id+1); }
	
	private boolean inSameRoomAsCurrentPlayer(int id) {
		return players.getPlayer(id).inSameRoomAs(currentPlayer);
	}
	
	public interface PlayerContainer {
		Player getPlayer(int id);
	}

}
