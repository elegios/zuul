package game;

/**
 * This interface describes what the Game class requires from it's Printer.
 * The most important of these requirements is that it needs to support
 * multiple players and be able to decide what to print depending on where
 * those players are in relation to the current player.
 * 
 * All methods but the setCurrentPlayer(Player, int) are methods that only
 * print things.
 */
public interface GamePrinter {
	
	void setCurrentPlayer(Player player, int id);
	
	void youAre(int id);
	
	void points(int currentPoints, int maxPoints);
	
	void welcome();
	void turnsLeft(int turnsLeft);
	void endOfTime();
	void fallToDeath(int id);
	
	void separator();

	void win(int numPlayers);

}
