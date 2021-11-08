package com.revature.model;

public class CharacterLimitException extends Exception {

	/**
	 * 	Ok this is was initially just for throwing exceptions if names for clients went over our database's 255 character limit, but I've decided I'm going to expand its scope to include
	 * also any special characters, non-visible characters such as newlines, and having whitespace between regular characters (i.e., no one name made up of multiple separate words)
	 * Maybe its better practice to have different exceptions for all these scenarios, but I don't want to end up with twenty thousand different exception classes and I also don't 
	 * want to have to refactor my code, so just understand what all this exception is for. 
	 */
	private static final long serialVersionUID = 1L;

	public CharacterLimitException(String message) {
		
		super(message);
		
	} 
	

	
}
