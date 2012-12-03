package command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	public String peekNext() { return inputLine.get(claimIndex); }
	
	public String claimNext() { return inputLine.get(claimIndex++); }
	
	public void finalizeClaim() {
		inputLine.subList(0, claimIndex).clear();
		claimIndex = 0;
	}
	
	public void releaseClaim() {
		claimIndex = 0;
	}

}
