package command;

/**
 * This is an abstract class representing a command. An instance
 * of a command can either be a factory instance or an actual
 * instance. A factory instance can use the parse(InputLine, int,
 * Listener, Printer) method to try and parse the InputLine as a
 * command of the current class. If successful it will return an
 * actual instance of the current class. This actual instance
 * responds to the give() method that issues the command.
 * 
 * The interfaces Listener and Printer are subclassed as required
 * by subclasses of Command to provide the necessary methods.
 * The objects passed as Listener and Printer to the parse method
 * should make sure to implement the correct subclass of these
 * interfaces.
 */
public abstract class Command {
	
	private String keyWord;
	
	protected Command(String keyWord) {
		this.keyWord = keyWord;
	}
	
	/**
	 * Tries to parse the given InputLine to an instance of the current
	 * Command class. If successful such an instance is returned, otherwise
	 * null is returned. The id parameter is used by other classes to
	 * differentiate this command from others. The exact use may vary.
	 * 
	 * The Listener and Printer parameters should implement the subclassed
	 * interfaces in the current command, if any, otherwise a ClassCastException
	 * will be thrown
	 * 
	 * @param inputLine the InputLine to be parsed
	 * @param id an id to be stored in the command
	 * @param listener the object that listens to the new command
	 * @param printer the object that prints for the new command
	 * @return a new instance of the current command class if successful, otherwise null.
	 * @throws ClassCastException if either listener or printer do not conform to the interfaces for this class
	 */
	public abstract Command parse(InputLine inputLine, int id,
		                          Listener listener, 
		                          Printer printer) throws ClassCastException;
	
	protected boolean defaultParse(InputLine inputLine) {
		if (keyWord.equals(inputLine.claimNext()))
			return true;
		
		inputLine.releaseClaim();
		return false;
			
	}
	
	protected Command() {}
	
	public abstract void give();
	
	public interface Listener {}
	public interface Printer  {}
	
}
