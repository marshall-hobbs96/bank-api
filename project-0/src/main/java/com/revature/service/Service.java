package com.revature.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.revature.DAL.DAL;
import com.revature.model.Account;
import com.revature.model.CharacterLimitException;
import com.revature.model.Client;

public class Service {
	
	DAL dao; 

	public Service() {}	//empty service constructor
	
	public Service(DAL dao) {
		
		this.dao = dao; 
		
	}
	
	public Client addClient(Client clientToAdd) throws CharacterLimitException, SQLException {
		
		//probably want to adjust my error handling. Maybe give out field names required if theyre null. Maybe just tell them fields cant be empty if theyre just empty
		
		//First trim leading and trailing whitespace. Won't throw an error here, we'll assume its an honest mistake and hasn't affected the actual intended names
		
		
		if((clientToAdd.getFirst_name() == null)) {
			
			throw new CharacterLimitException("Unable to add client. First name is null.");
			
		} else if ((clientToAdd.getLast_name() == null)) {
			
			throw new CharacterLimitException("Unable to add client. Last name is null.");
			
		}
		
		//Have to take care of potential nulls first. Then we can trim. If names were null, would throw nullpointerexception when we tried to trim. Best to avoid that. 
		
		clientToAdd.setFirst_name(clientToAdd.getFirst_name().trim());
		clientToAdd.setLast_name(clientToAdd.getLast_name().trim());
		Pattern pattern = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);	//I kinda decided if I wanted to do this for real, I would allow the user to input anything they want as their names, because who am I to decide what
																			//Should and shouldnt go into an name. However, I also spent a lot of time, thougth, and effort into how I would filter out special characters and whitespace between 
																			//Characters in the names, so I'm going to leave this in my project. And if anyone has any complaints, its my project and I can do what I want :p
		Matcher firstNameMatcher = pattern.matcher(clientToAdd.getFirst_name());
		Matcher lastNameMatcher = pattern.matcher(clientToAdd.getLast_name());
		
		if(clientToAdd.getFirst_name() == "" ) {
			
			throw new CharacterLimitException("Unable to add client. First name is an empty string.");
			
		} else if(clientToAdd.getLast_name() == "") {
			
			throw new CharacterLimitException("Unable to add client. Last name is an empty string.");
			
		}	else if (firstNameMatcher.find()) {
			
			throw new CharacterLimitException("Unable to add client. First name must only contain letters. No whitespace between characters.");
			
		} else if (lastNameMatcher.find()) {
			
			throw new CharacterLimitException("Unable to add client. Last name must only contain letters. No whitespace between characters.");	//Yes I know, there are more letters than the letters in the english alphabet. Just relax ok, its only a personal project
			
		} else if(clientToAdd.getFirst_name().length() > 255) {
			
			throw new CharacterLimitException("Unable to add client. First name exceeds acceptable number of characters (255).");
	
		} else if(clientToAdd.getLast_name().length() > 255) {
			
			throw new CharacterLimitException("Unable to add client. Last name exceeds acceptable number of character (255).");
			
		} else {	//Ok we checked to see if we have empty, null, or exceed character limit of names. Now lets check to see if a name is one word. 
			
			
			if(clientToAdd.getFirst_name().compareTo(clientToAdd.getFirst_name().replaceAll("\\s+", "")) != 0) { 
				
				//this is checking to see if we have an whitespace or non-visible characters between characters in our firstname. A firstname must be one whole word. If it isn't, then we tell 
				//the client hey, your first name needs to be like, one word yo. I mean I don't know, I don't want to be discrimatory, but are there first names that aren't one word? What does 
				//it mean to be a "first name"? Like, doesn't that necessarily mean it has to be one word?!?!? I don't know man its too late for this.....Friday night and I choose to do this bs.
				
				throw new CharacterLimitException("First name must be one word, no special characters or whitespace between characters.");
				
				
				
			} else if (clientToAdd.getLast_name().compareTo(clientToAdd.getLast_name().replaceAll("\\s+", "")) != 0) {
				
				//Yea same thing as above. Also compareTo returns 0 if the strings are the same. Like lexicographically, not objectively...as objects...not the same string object :p
				throw new CharacterLimitException("Last name must be one word, no special characters or whitespace between characters.");
				
				//Actually I just realized you can have visible special characters. Do I want to test for stuff like &, %, ^ in names?!?! This is a lot for such a simple application............
				//I think I'll just put that on the back burner for now...and by back burner I mean I'm just going to forget to do it later lol 
				
			}
			
			Client returnClient = dao.newClient(clientToAdd);
			return returnClient; 
			
			
		}
		
		
	}
	
	public ArrayList<Client> getAllClients() throws SQLException {
		
		return dao.getAllClients(); 
		
	}
	
	public Client getClient(int client_id) throws SQLException {
		
		//what if we accidently pass client_id as a string? Won't encounter. JSON parsing will throw exception when we hit controller layer
		
		return dao.getClient(client_id); 
		
		
	}
	
	public Client updateClient(Client client) throws SQLException {
		
		
		return new Client();
		//return dao.updateClient(client);
		
	}

	public Object deleteClient(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object createAccount(Account testAccount) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getClientAccounts(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getClientAccount(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object deleteAccount(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object updateAccount(int i, Account testAccount) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
