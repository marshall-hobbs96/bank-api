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

	public Service() {
	} // empty service constructor

	public Service(DAL dao) {

		this.dao = dao;

	}

	public Client addClient(Client clientToAdd) throws CharacterLimitException, SQLException {

		// probably want to adjust my error handling. Maybe give out field names
		// required if theyre null. Maybe just tell them fields cant be empty if theyre
		// just empty

		// First trim leading and trailing whitespace. Won't throw an error here, we'll
		// assume its an honest mistake and hasn't affected the actual intended names

		if ((clientToAdd.getFirst_name() == null)) {

			throw new CharacterLimitException("Unable to add client. First name is null.");

		} else if ((clientToAdd.getLast_name() == null)) {

			throw new CharacterLimitException("Unable to add client. Last name is null.");

		}

		// Have to take care of potential nulls first. Then we can trim. If names were
		// null, would throw nullpointerexception when we tried to trim. Best to avoid
		// that.

		clientToAdd.setFirst_name(clientToAdd.getFirst_name().trim());
		clientToAdd.setLast_name(clientToAdd.getLast_name().trim());
		Pattern pattern = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE); // I kinda decided if I wanted to do this
																				// for real, I would allow the user to
																				// input anything they want as their
																				// names, because who am I to decide
																				// what
		// Should and shouldnt go into an name. However, I also spent a lot of time,
		// thougth, and effort into how I would filter out special characters and
		// whitespace between
		// Characters in the names, so I'm going to leave this in my project. And if
		// anyone has any complaints, its my project and I can do what I want :p
		Matcher firstNameMatcher = pattern.matcher(clientToAdd.getFirst_name());
		Matcher lastNameMatcher = pattern.matcher(clientToAdd.getLast_name());

		if (clientToAdd.getFirst_name() == "") {

			throw new CharacterLimitException("Unable to add client. First name is an empty string.");

		} else if (clientToAdd.getLast_name() == "") {

			throw new CharacterLimitException("Unable to add client. Last name is an empty string.");

		} else if (firstNameMatcher.find()) {

			throw new CharacterLimitException(
					"Unable to add client. First name must only contain letters. No whitespace between characters.");

		} else if (lastNameMatcher.find()) {

			throw new CharacterLimitException(
					"Unable to add client. Last name must only contain letters. No whitespace between characters."); // Yes
																														// I
																														// know,
																														// there
																														// are
																														// more
																														// letters
																														// than
																														// the
																														// letters
																														// in
																														// the
																														// english
																														// alphabet.
																														// Just
																														// relax
																														// ok,
																														// its
																														// only
																														// a
																														// personal
																														// project

		} else if (clientToAdd.getFirst_name().length() > 255) {

			throw new CharacterLimitException(
					"Unable to add client. First name exceeds acceptable number of characters (255).");

		} else if (clientToAdd.getLast_name().length() > 255) {

			throw new CharacterLimitException(
					"Unable to add client. Last name exceeds acceptable number of characters (255).");

		} else { 

			Client returnClient = dao.newClient(clientToAdd);
			return returnClient;

		}

	}

	public ArrayList<Client> getAllClients() throws SQLException {

		return dao.getAllClients();

	}

	public Client getClient(int client_id) throws SQLException {

		// what if we accidently pass client_id as a string? Won't encounter. JSON
		// parsing will throw exception when we hit controller layer

		return dao.getClient(client_id);

	}

	public Client updateClient(Client client) throws SQLException, CharacterLimitException {
		
		Client existingClient = dao.getClient(client.getClient_id());	
		//If any fields are empty in our updating client object, then we'll assume thats intentional and they didn't want to update that part. So we'll keep it the same as the original 
		if ((client.getFirst_name() == null)) {

			client.setFirst_name(existingClient.getFirst_name());

		} 
		if ((client.getLast_name() == null)) {

			client.setLast_name(existingClient.getLast_name());

		}
		

		client.setFirst_name(client.getFirst_name().trim());
		client.setLast_name(client.getLast_name().trim());
		Pattern pattern = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);
		Matcher firstNameMatcher = pattern.matcher(client.getFirst_name());
		Matcher lastNameMatcher = pattern.matcher(client.getLast_name());

		if (client.getFirst_name() == "") {

			throw new CharacterLimitException("Unable to update client. First name is an empty string.");

		} else if (client.getLast_name() == "") {

			throw new CharacterLimitException("Unable to update client. Last name is an empty string.");

		} else if (firstNameMatcher.find()) {

			throw new CharacterLimitException(
					"Unable to update client. First name must only contain letters. No whitespace between characters.");

		} else if (lastNameMatcher.find()) {

			throw new CharacterLimitException(
					"Unable to update client. Last name must only contain letters. No whitespace between characters."); 
		} else if (client.getFirst_name().length() > 255) {

			throw new CharacterLimitException(
					"Unable to update client. First name exceeds acceptable number of characters (255).");

		} else if (client.getLast_name().length() > 255) {

			throw new CharacterLimitException(
					"Unable to update client. Last name exceeds acceptable number of characters (255).");

		} else { 


			Client returnClient = dao.updateClient(client);
			return returnClient;

		}


	}

	public boolean deleteClient(int client_id) throws SQLException {
		
		ArrayList<Account> clientAccounts = dao.getClientAccounts(client_id);
		
		if(clientAccounts.size() != 0) {
			
			throw new SQLException("Unable to delete client. Client still has accounts.");
			
		}
	
		return dao.deleteClient(client_id);	//I mean honestly, it will always return true or throw an exception....Should probably refactor to just make void, but it works so
		
		
	}

	public Account createAccount(Account account) throws SQLException, CharacterLimitException {
		//Need to check if:
		//Client exists
		//Account type is correct. Currently we only support "Savings" and "Checkings"
		//Check to see if negative funds. Can't start a new account with negative funds. Because that how I want it. No deadbeats
		
		dao.getClient(account.getClient_id());
		
		if((account.getAccount_type().compareTo("Savings") != 0) && (account.getAccount_type().compareTo("Checkings") != 0)) {
			
			throw new CharacterLimitException("Unable to create account. Account type not supported (only 'Savings' and 'Checkings').");
			
		} else if(account.getFunds() < 0) {
			
			throw new CharacterLimitException("Unable to create account. Initial funds cannot be negative.");
			
		}
		return dao.createAccount(account);
	}

	public ArrayList<Account> getClientAccounts(int client_id) throws SQLException {	//Will return all accounts associated with this client 
		
		dao.getClient(client_id);
		return dao.getClientAccounts(client_id);	//Will return empty if there are no accounts. 
	}

	public ArrayList<Account> getClientAccounts(int client_id, double lessThan, double greaterThan) throws SQLException {
		
		dao.getClient(client_id);
		return dao.getClientAccounts(client_id, lessThan, greaterThan);
		
	}
	
	public Account getClientAccount(int client_id, int account_id) throws SQLException {
		
		
		
		dao.getClient(client_id);
		Account account = dao.getAccount(account_id);
		
		if(account.getClient_id() != client_id) {
			
			throw new SQLException("Unable to get account. Account does not belong to client.");
			
		}
		
		return account; 
		
		

	}

	public boolean deleteAccount(int client_id, int account_id) throws SQLException {
		
		Account account = dao.getAccount(account_id);
		dao.getClient(client_id);
		if(account.getClient_id() != client_id) {
			
			throw new SQLException("Unable to get account. Account does not belong to client.");
			
		}
		return dao.deleteAccount(account_id);
	}

	public Account updateAccount(int client_id, Account updateAccount) throws SQLException {	//I really only want to let this update funds. Ignore all other arguments that may have been passed.
		
		if(updateAccount.getClient_id() != client_id) {
			
			throw new SQLException("Unable to update account. Account does not belong to client.");
			
		}
		
		dao.getClient(client_id); 	//making sure client actually exists
		Account currentAccount = dao.getAccount(updateAccount.getAccount_id()); 	//making sure account actually exists
		
		if(updateAccount.getFunds() == null) {	//Really the only thing update account changes right now is the funds. 
			
			updateAccount.setFunds(currentAccount.getFunds());
			
		} 
		
		
		return dao.updateAccount(updateAccount);
	}
	
	public ArrayList<Account> getAllAccounts() throws SQLException {
		
		return dao.getAllAccounts();
		
	}

}
