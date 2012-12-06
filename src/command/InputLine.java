package command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class to describe a line supplied by a user. It can be made
 * to guarantee a certain length and supports convenience methods
 * for claiming part of the input, removing it so that no other
 * part of the program accidentally uses it.
 * 
 * When one object/method/whatever is done with the inputline it
 * should either finalize the claim or release it, depending on
 * whether other parts of the program should be allowed to access
 * the claimed words. If the claim is released the inputline will
 * behave as if nothing had been claimed in the first place.
 * 
 * Ex: <code>
 *  a = new InputLine("this is a test");
 *  a.claimNext();     // -> "this"
 *  a.claimNext();     // -> "is"
 *  
 *  a.releaseClaim();
 *  a.claimNext();     // -> "this"
 *  
 *  a.finalizeClaim();
 *  a.claimNext();     // -> "is"
 *  
 *  a.releaseClaim();
 *  a.claimNext();     // -> "is"
 *  </code>
 */
public class InputLine {
	public static final String WORD_SPLIT_REGEX = " ";
	public static final int DEFAULT_MINIMUM_LENGTH = 2;
	
	private List<String> inputLine;
	private int          claimIndex;

	public InputLine(String inputLine) { this(inputLine, DEFAULT_MINIMUM_LENGTH); }
	public InputLine(String inputLine, int minimumLength) {
		this.inputLine = new ArrayList<String>(Arrays.asList(inputLine.trim().split(WORD_SPLIT_REGEX)));
		claimIndex = 0;
		
		for (int i = this.inputLine.size(); i < minimumLength; i++)
			this.inputLine.add(null);
	}
	
	/**
	 * Gets the next word of the inputline without claiming it.
	 * @return the next word of the inputline
	 */
	public String peekNext() { return inputLine.get(claimIndex); }
	
	/**
	 * Gets the next word of the inputline after claiming it.
	 * @return the next word of the inputline
	 */
	public String claimNext() { return inputLine.get(claimIndex++); }
	
	/**
	 * Removes the claimed words from the inputline so no other part
	 * of the program can access them.
	 */
	public void finalizeClaim() {
		inputLine.subList(0, claimIndex).clear();
		claimIndex = 0;
	}
	
	/**
	 * Releases any present claims on words in the inputline, enabling
	 * other parts of the program to access them.
	 */
	public void releaseClaim() {
		claimIndex = 0;
	}

}
