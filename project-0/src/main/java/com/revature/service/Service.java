package com.revature.service;

import java.sql.SQLException;

import com.revature.DAL.DAL;
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
		
		if((clientToAdd.getFirst_name() == null) || (clientToAdd.getFirst_name().equals(""))) {
			
			throw new CharacterLimitException("First name cannot be empty");
			
			
		} else if((clientToAdd.getLast_name() == null) || (clientToAdd.getLast_name().equals(""))) {
			
			throw new CharacterLimitException("Last name cannot be empty");
			
		} else if(clientToAdd.getFirst_name().length() > 255) {
			
			throw new CharacterLimitException("First name exceeds character limit"); 
			
		} else if(clientToAdd.getLast_name().length() > 255) {
			
			throw new CharacterLimitException("Last name exceeds character limit");
			
		}

		else {
			
			Client returnClient = dao.newClient(clientToAdd);
			return returnClient; 
			
			
		}
		
		
	}
	
}
