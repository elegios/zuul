package game;

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
