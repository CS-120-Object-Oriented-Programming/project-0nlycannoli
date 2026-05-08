package edu.kings;
/**
 * This class is part of the "Campus of Kings" application. "Campus of Kings" is a
 * very simple, text based adventure game.
 *
 * This class holds an enumeration of all command words known to the game. It is
 * used to recognize commands as they are typed in.
 *
 * @author Maria Jump
 * @version 2015.02.01
 *
 * Used with permission from Dr. Maria Jump at Northeastern University
 */

public class CommandWords {
	/** A constant array that holds all valid command words. */
	private static String[] validCommands;

	/**
	 * Static block to initialize the fields of CommandWords.
	 */
	static {
		String[] tempCommands = {"go", "quit", "help", "look", "take", "use", "inventory"};
		validCommands = tempCommands;
	}

	/**
	 * Check whether a given String is a valid command word.
	 *
	 * @param aString The string to determine whether it is a valid command.
	 * @return true if a given string is a valid command, false if it isn't.
	 */
	public static boolean isCommand(String aString) {
		boolean valid = false;
		int index = 0;
		while (!valid && index < validCommands.length) {
			if (validCommands[index].equals(aString)) {
				valid = true;
			}
			index++;
		}
		// if we get here, the string was not found in the commands
		return valid;
	}
}
