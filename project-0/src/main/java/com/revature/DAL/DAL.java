package com.revature.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	public void getAllClients() { 
			
		
			
			
	}
	
	public void getClient() {
		
		
		
	}
	
	public void updateClient() {
		
		
		
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
