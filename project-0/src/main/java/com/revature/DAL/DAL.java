package com.revature.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import org.postgresql.Driver;

import com.revature.model.Account;
import com.revature.model.Client;

public class DAL {

	public static String url = "";
	public static String superUsername = "";
	public static String superPassword = "";
	public static Driver postgresDriver = new Driver(); 
	public static Connection connection; 
	
	public DAL() {}
	
	public static String establishConnection(String url, String superUsername, String superPassword){
		
		String result;
		
		try {
			
			DriverManager.registerDriver(postgresDriver);
			connection = DriverManager.getConnection(url, superUsername, superPassword);
			DAL.url = url; 
			DAL.superUsername = superUsername; 
			DAL.superPassword = superPassword; 
			result = "Connection successfully established with " + url;
			return result; 
		}
		
		catch(SQLException e) {
			
			result = "Connection error: " + e.getMessage(); 
			return result; 
			
			
		}
		

		
	}
	
	public boolean clientExists(int clientId) throws SQLException {	//used to check if a client with id = clientId currently exists in the database. Better than trying getClient and having to catch an exception
												//in order to check if a client is in the database. Mostly to be used as helper function 
		
		String sql = "SELECT * FROM Clients WHERE client_id = ?";
		
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, clientId);
		
		ResultSet resultSet = statement.executeQuery();
		
		return resultSet.next(); 
		
	}
	
	public boolean clientHasAccounts(int clientId) throws SQLException {
		
		String sql = "SELECT * FROM Accounts WHERE client_id = ?";
		
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, clientId);
		
		ResultSet  resultSet = statement.executeQuery();
		
		return resultSet.next();	//Will return true is any accounts exist associated with client_id. False otherwise 
		
	}
	
	public boolean accountExists(int accountId) throws SQLException {
		
		String sql = "SELECT * FROM Accounts WHERE account_id = ?";
		
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, accountId);
		
		ResultSet resultSet = statement.executeQuery();
		
		return resultSet.next();
		
	}
 	
	public Client newClient(Client client) throws SQLException { 
		
		String sql = "INSERT INTO Clients (first_name, last_name) " + " VALUES (?, ?);";
		

		
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		
		statement.setString(1, client.getFirst_name());
		statement.setString(2, client.getLast_name());
		
		int numRecordsUpdated = statement.executeUpdate();
		
		if(numRecordsUpdated != 1) {
			
			throw new SQLException("Adding new client unsuccessful");
			
		}
		
		ResultSet resultSet = statement.getGeneratedKeys(); 
		resultSet.next();
		int automaticallyGeneratedId = resultSet.getInt(1);
		
		return new Client(automaticallyGeneratedId, client.getFirst_name(), client.getLast_name());
			

		


	}
	
	public ArrayList<Client> getAllClients() throws SQLException { 	//sends a SQL query to return all client_id's , first_name's, last_name's from clients table. Not implementing num_accounts yet
		
		ArrayList<Client> returnList = new ArrayList<>(); 
			
		String sql = "SELECT * FROM clients;";
		
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		
		ResultSet resultSet = statement.executeQuery();
		
		if(resultSet.next() == false) {	
			//I'm actually kinda proud of this little thing I did. If next() returns false on first try, then it means clients table is empty and we need to throw an exception saying our clients table
			//is empty. But now weve iterated resultSet, so how do we do a while loop to iterate over all the elements without skipping the first element? A DO WHILE LOOP! (Just suprised I found
			//a use for this kind of loop so early on from learning about it, and without forcing it. Just feels like a natural application of it) 
			
			throw new SQLException("No clients to get. clients table is empty");		
			//I mean probably not how SQLException was intended to be used, but it works and I'd really rather not make a new exception class for this one case
		}
		
		do {
			
			Client client = new Client(resultSet.getInt("client_id"), resultSet.getString("first_name"), resultSet.getString("last_name"));
			returnList.add(client);
			
		}while(resultSet.next());
		
		return returnList; 		
		
			
	}
	
	public Client getClient(int client_id) throws SQLException{		
		//definetly going to have to do good error handling here. Way too easy to try to get info on a client that doesn't exist
		
		String sql = "SELECT client_id, first_name, last_name FROM clients WHERE client_id = ?;";
		
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		
		statement.setInt(1, client_id);
		
		ResultSet resultSet = statement.executeQuery(); 
		
		if(resultSet.next() == false) {
			
			throw new SQLException("No client with that ID");
			
		}
		
		return new Client(resultSet.getInt("client_id"), resultSet.getString("first_name"), resultSet.getString("last_name"));

		
	}
	
	public Client updateClient(Client newClient)  throws SQLException{
		
		String sql = "UPDATE clients SET first_name = ?, last_name = ?, num_accounts = ? WHERE client_id = ?;";
		
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		
		statement.setString(1, newClient.getFirst_name());
		statement.setString(2, newClient.getLast_name());
		statement.setInt(3, newClient.getNum_accounts());
		statement.setInt(4, newClient.getClient_id());
		
		int numRecordsUpdated = statement.executeUpdate();
		
		if(numRecordsUpdated != 1) {
			
			throw new SQLException("Updating client unsuccessful");
			
		}
		
		sql = "SELECT * FROM clients WHERE client_id = ?;";
		statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, newClient.getClient_id());
		
		ResultSet resultSet = statement.executeQuery();
		
		if(resultSet.next() == false) {
			
			throw new SQLException("Updating client unsuccessful");
			
		}
		
		return new Client(resultSet.getInt("client_id"), resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getInt("num_accounts"));
		
	}
	
	public boolean deleteClient(int clientId) throws SQLException {
		//Return true on success, false on failure. 
		//Just realized that delete client will have to either pass failure if accounts associated with client still exists or we'll have to delete all accounts when we delete client.
		//Think it would make more sense for the client to specifically delete the accounts first, instead of automatically doing it when they delete a client. That way the person sending
		//the HTTP request knows like hey, these accounts are going to have to be deleted to delete the client ya hear? 
		
		String sql = "DELETE FROM clients WHERE client_id = ?;";
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, clientId);
		
		int numRecordsUpdated = statement.executeUpdate(); 
		if(numRecordsUpdated != 1) {
			
			return false;
			
		}
		
		return true;
		
	}
	
	public Account createAccount(Account newAccount) throws SQLException {
		
		String sql = "INSERT INTO Accounts (client_id, account_type, funds) VALUES (?, ?, ?);";
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, newAccount.getClient_id());
		statement.setString(2, newAccount.getAccount_type());
		statement.setDouble(3, newAccount.getFunds());
		
		int numRecordsUpdated = statement.executeUpdate();
		
		if(numRecordsUpdated != 1) {
			
			throw new SQLException("Unable to create account. Unknown error (probably a dropped connection or some other obscure problem).");
			
		}
		
		ResultSet resultSet = statement.getGeneratedKeys();
		resultSet.next(); 
		int accountId = resultSet.getInt("account_id");
		return new Account(accountId, newAccount.getClient_id(), newAccount.getAccount_type(), newAccount.getFunds());
		
	}
	
	public ArrayList<Account> getClientAccounts(int clientId) throws SQLException {	//Get all accounts associated with given client 
		
		ArrayList<Account> clientAccounts = new ArrayList<>();
		String sql = "SELECT * FROM Accounts WHERE client_id = ?;";
		
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, clientId);
		
		ResultSet resultSet = statement.executeQuery();
		
		while(resultSet.next()) {
			
			Account account = new Account(resultSet.getInt("account_id"), resultSet.getInt("client_id"), resultSet.getString("account_type"), resultSet.getDouble("funds"));
			clientAccounts.add(account);
			
		}
		
		return clientAccounts; 
		
	}
	
	public ArrayList<Account> getClientAccounts(int clientId, double lessThan, double greaterThan) throws SQLException {	//get all accounts associated with given client with funds between two given values 
		
		ArrayList<Account> clientAccounts = new ArrayList<>();
		
		String sql = "SELECT * FROM Accounts WHERE client_id = ? AND funds < ? AND funds > ?;";
		
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, clientId);
		statement.setDouble(2, lessThan);
		statement.setDouble(3, greaterThan);
		
		ResultSet resultSet = statement.executeQuery();
		
		while(resultSet.next()) {
			
			Account newAccount = new Account(resultSet.getInt("account_id"), resultSet.getInt("client_id"), resultSet.getString("account_type"), resultSet.getDouble("funds"));
			clientAccounts.add(newAccount);
			
		}
		
		
		
		return clientAccounts; 
		
	}
	
	public Account getAccount(int accountId) throws SQLException{
		
		String sql = "SELECT * FROM Accounts WHERE account_id = ?;";
		
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, accountId);
		
		ResultSet resultSet = statement.executeQuery();
		
		if(resultSet.next() == false) {
			
			throw new SQLException("Unable to get account. No account with that ID.");
			
		}
		
		return new Account(resultSet.getInt("account_id"), resultSet.getInt("client_id"), resultSet.getString("account_type"), resultSet.getDouble("funds"));
				
		
	}
	
	public Account updateAccount(Account accountToUpdate) throws SQLException {
		
		String sql = "UPDATE Accounts SET funds = ? WHERE account_id = ?;";
		
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		statement.setDouble(1, accountToUpdate.getFunds());
		statement.setInt(2, accountToUpdate.getAccount_id());
		
		int numRecordsUpdated = statement.executeUpdate();
		
		if(numRecordsUpdated != 1) {
			
			throw new SQLException("Unable to update account. Unknown error (time to dive into the backend code). Maybe a connection error of some kind.");
			
		}
		
		sql = "SELECT * FROM accounts WHERE account_id = ?;";
		statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, accountToUpdate.getAccount_id());
		
		ResultSet resultSet = statement.executeQuery();
		
		if(resultSet.next() == false) {
			
			throw new SQLException("Unable to update account. Unknown error (time to dive into the backend code). Maybe a connection error of some kind.");
			
		}
		
		return new Account(resultSet.getInt("account_id"), resultSet.getInt("client_id"), resultSet.getString("account_type"), resultSet.getDouble("funds")); 
		
		
	}
	
	public boolean deleteAccount(int accountId) throws SQLException {
		
		//Return true on success, false on failure.
		
		String sql = "DELETE FROM accounts WHERE account_id = ?;";
		
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, accountId);
		
		int numModifiedRows = statement.executeUpdate();
		
		if(numModifiedRows != 1) {
			
			throw new SQLException("Unable to delete account. Unknown error(time for a dive into backend code).");
			
		}
		
		return true;
		
	}
	
	public ArrayList<Account> getAllAccounts() throws SQLException { 	//sends a SQL query to return all client_id's , first_name's, last_name's from clients table. Not implementing num_accounts yet
		
		ArrayList<Account> returnList = new ArrayList<>(); 
			
		String sql = "SELECT * FROM accounts;";
		
		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		
		ResultSet resultSet = statement.executeQuery();
		
		if(resultSet.next() == false) {	
			//I'm actually kinda proud of this little thing I did. If next() returns false on first try, then it means clients table is empty and we need to throw an exception saying our clients table
			//is empty. But now weve iterated resultSet, so how do we do a while loop to iterate over all the elements without skipping the first element? A DO WHILE LOOP! (Just suprised I found
			//a use for this kind of loop so early on from learning about it, and without forcing it. Just feels like a natural application of it) 
			
			throw new SQLException("No accounts to get. accounts table is empty");		
			//I mean probably not how SQLException was intended to be used, but it works and I'd really rather not make a new exception class for this one case
		}
		
		do {
			
			Account account = new Account(resultSet.getInt("client_id"), resultSet.getInt("account_id"), resultSet.getString("account_type"), resultSet.getDouble("funds"));
			returnList.add(account);
			
		}while(resultSet.next());
		
		return returnList; 		
		
			
	}
	
}
