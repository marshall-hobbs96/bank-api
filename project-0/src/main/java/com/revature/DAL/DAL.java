package com.revature.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.postgresql.Driver;

public class DAL {

	public static String url = "";
	public static String superUsername = "";
	public static String superPassword = "";
	public static Driver postgresDriver = new Driver(); 
	public static Connection connection; 
	
	public static String establishConnection(String url, String superUsername, String superPassword){
		
		String result;
		
		try {
			
			DriverManager.registerDriver(postgresDriver);
			connection = DriverManager.getConnection(url, superUsername, superPassword);
			DAL.url = url; 
			DAL.superUsername = superUsername; 
			DAL.superPassword = superPassword; 
			result = "Connection successfully establishes with " + url;
			return result; 
		}
		
		catch(SQLException e) {
			
			result = "Connection error: " + e.getMessage(); 
			return result; 
			
			
		}
		

		
	}
	
	public static void addClient(String clientName) {
		
		
		
	}
	
	
	
}
