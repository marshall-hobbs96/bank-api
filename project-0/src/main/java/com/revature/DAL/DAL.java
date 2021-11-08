package com.revature.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import org.postgresql.Driver;

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
		int automaticallyGeneratedId = resultSet.getInt(1);
		
		return new Client(automaticallyGeneratedId, client.getFirst_name(), client.getLast_name());
			

		


	}
	
	public ArrayList<Client> getAllClients() throws SQLException { 	//sends a SQL query to return all client_id's , first_name's, last_name's from clients table. Not implementing num_accounts yet
		
		ArrayList<Client> returnList = new ArrayList<>(); 
			
		String sql = "SELECT client_id, first_name, last_name FROM clients;";
		
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
	
	public Client updateClient(String new_first_name, String new_last_name)  throws SQLException{
		
		return new Client();
		
	}
	
	public void deleteClient() {
		
		
		
	}
	
	public void createAccount() {
		
		
		
	}
	
	public void getClientAccount() {
		
		
		
	}
	
	public void getAccount() {
		
		
		
	}
	
	public void updateAccount() {
		
		
		
	}
	
	public void deleteAccount() {
		
		
		
	}
	
}
