package com.revature.controller;


import java.sql.SQLException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.revature.model.Account;
import com.revature.model.CharacterLimitException;
import com.revature.model.Client;
import com.revature.service.Service;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class Controller {
	
	@JsonIgnoreProperties(ignoreUnknown = true)

	private Service service; 
	
	public Controller(Service service) {
		
		this.service = service; 
		
	}
	
	public Handler newClient = (ctx) -> { 
		//controller level method for creating a new client. POST
		//POST /clients: Creates a new client
		
		//Need to handle if names have more than 255 characters. This is our limit in our database. Also need to make sure 
		//we have both a first and last name.
		// I would like to disable exceptions for unrecognized fields in our json so I can handle that myself, but I can't figure that out
		try {
			Client newClient = ctx.bodyAsClass(Client.class);
			Client returnClient = this.service.addClient(newClient);
			ctx.json(returnClient);
			ctx.status(201);
		}
		
		catch(CharacterLimitException e) {
			
			ctx.status(406);
			ctx.result(e.getMessage());
			
		}
		
		catch(SQLException e) {
			
			ctx.status(400);
			ctx.result(e.getMessage());
			
		}
		
		
	};
	
	public Handler getAllClients = (ctx) -> {
		//controller level method for getting all clients. GET
		//GET /clients: Gets all clients
		//maybe I'll add some additional functionality to this later because it feels just too easy, but for now I'm pretty sure it meets the project requirements so *shrug*
		
		try {
		ctx.json(service.getAllClients());
		ctx.status(200);
		}
		
		catch(SQLException e) {	//maybe different catches for what went wrong. No table? Empty table? Connection lost? But a lot of work just to make sure we get the right http status code 
			
			ctx.status(204);
			ctx.result(e.getMessage());
			
		}
		
	};
	
	public Handler getClient = (ctx) -> {
		//Controller level method for getting info on specific client. GET
		//GET /clients/{client_id}: Get client with an id of X (if the client exists)
		
		try {
		
			String param = ctx.queryParam("client_id");
			int client_id = Integer.parseInt(param);
			Client returnClient = service.getClient(client_id);
			ctx.json(returnClient);
			ctx.status(200);
			
		}
		
		catch(SQLException e) {
			
			ctx.json(e.getMessage());
			ctx.status(406); //Not acceptable
			
		}
		
		
	};
	
	public Handler updateClient = (ctx) -> {
		//Controller level method for updating info on specific client. PUT
		//PUT /clients/{client_id}: Update client with an id of X (if the client exists)
		try {
			String param = ctx.queryParam("client_id");
			int client_id = Integer.parseInt(param);
			Client newClient = ctx.bodyAsClass(Client.class);
			newClient.setClient_id(client_id);
			Client returnClient = service.updateClient(newClient);
			ctx.json(returnClient);
			ctx.status(200);
		}
		
		catch(SQLException e) {
			
			ctx.json(e.getMessage());
			ctx.status(400);
			
		}
		
	};
	
	public Handler deleteClient = (ctx) -> {
		//Controller level method for deleting specific client. DELETE
		//DELETE /clients/{client_id}: Delete client with an id of X (if the client exists)
		String param = ctx.queryParam("client_id");
		int client_id = Integer.parseInt(param);
		try {
			service.deleteClient(client_id);
			ctx.status(200);
			ctx.result("Client successfully deleted.");
		}
		
		catch(SQLException e) {
			
			ctx.status(400);
			ctx.result(e.getMessage());
			
		}
	};
	
	public Handler createAccount = (ctx) -> {
		//Controller level method responsible for creating an account for a client with a matching client_id
		//Create a new account for a client with id of X (if client exists)
		String param = ctx.queryParam("client_id");
		int client_id = Integer.parseInt(param);
		Account accountToMake = ctx.bodyAsClass(Account.class);
		accountToMake.setClient_id(client_id);	//I dont care what your trying to set the client id as in the json, all that matters is the client id specified in the endpoint
												//maybe I'll throw some extra functionality in here in the eventuality that they don't match up. 
		
		service.createAccount(accountToMake);
		
		
	};
	
	public Handler getClientAccounts = (ctx) -> {
		//Controller level method for getting accounts associated with a specific client. GET
		//Has additional, optional parameters including accountsLessThan and accountsGreaterThan
		//These allow the optional parameter of only getting the accounts belonging to the client 
		//that either have more or less than the specified amount. 
		
		/*
		 * 
		 * These two should be the same endpoint (check for query parameters using ctx.queryParam("amountLessThan") / ctx.queryParam("amountGreaterThan)):

    GET /clients/{client_id}/accounts: Get all accounts for client with id of X (if client exists)
    GET /clients/{client_id}/accounts?amountLessThan=2000&amountGreaterThan=400: Get all accounts for client id of X with balances between 400 and 2000 (if client exists)

		 * 
		 */
		
		
	};
	
	
	public Handler getAccount = (ctx) -> {
		//Controller level method for getting accounts with specific account id. Must also have 
		//a matching client ID. I.E, only can access accounts belonging to a client. GET
		//GET /clients/{client_id}/accounts/{account_id}: Get account with id of Y belonging to client 
		//with id of X (if client and account exist AND if account belongs to client)
		
		
		
	};
	
	public Handler updateAccount = (ctx) -> {
		//Controller level method for updating account with specific account id and must
		//belong to matching client id. PUT
		//PUT /clients/{client_id}/accounts/{account_id}: Update account with id of Y belonging to client 
		//with id of X (if client and account exist AND if account belongs to client)
		
		
	};
	
	public Handler deleteAccount = (ctx) -> {
		//Controller level method for deleting account with specific account id and must
		//belong to matching client id. DELETE
		//DELETE /clients/{client_id}/accounts/{account_id}: Delete account with id of Y belonging to 
		//client with id of X (if client and account exist AND if account belongs to client)
		
		
	};
	
	public void registerEndpoint(Javalin app) {
		
		app.post("/clients", newClient);	
		app.get("/clients", getAllClients);
		app.get("/clients/{client_id}", getClient);
		app.put("/clients/{client_id}", updateClient);
		app.delete("/clients/{client_id}", deleteClient);
		app.post("/clients/{client_id}/accounts", createAccount);
		app.get("/clients/{client_id}/accounts", getClientAccounts);
		app.get("/clients/{client_id}/accounts/{account_id}", getAccount);
		app.put("/clients/{client_id}/accounts/{account_id}", updateAccount);
		app.delete("/clients/{client_id}/accounts/{account_id}", deleteAccount);
		
	}
	
}
