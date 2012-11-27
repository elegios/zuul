import command.Command;
import command.CommandWords;


public class Print {
	private static final String YOU = "You";
	
	private String you(int id) { return YOU + " #" + id; }
	
	public void help() {
		System.out.println("You are lost. You are alone. You wander");
		System.out.println("around at the university.");
		System.out.println();
		System.out.println("Your command words are:");
		CommandWords.showAll();
	}
	
	public void go(        Command command) { go(YOU, command); }
	public void go(int id, Command command) { go(you(id), command); }
	
	private void go(String name, Command command) {
		System.out.println(name + " leaves the room, heading " +command.getSecondWord());
	}
	
	public void unknown(      ) { unknown(YOU); }
	public void unknown(int id) { unknown(you(id)); }
	
	private void unknown(String name) {
		System.out.println(name + "");
	}
}
