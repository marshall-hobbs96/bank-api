package com.revature.controller;

import com.revature.service.Service;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class Controller {

	private Service service; 
	
	public Controller(Service service) {
		
		this.service = service; 
		
	}
	
	public Handler newClient = (ctx) -> { 
		//controller level method for creating a new client. POST
		//POST /clients: Creates a new client
		
		
	};
	
	public Handler getAllClients = (ctx) -> {
		//controller level method for getting all clients. GET
		//GET /clients: Gets all clients
		
		
	};
	
	public Handler getClient = (ctx) -> {
		//Controller level method for getting info on specific client. GET
		//GET /clients/{client_id}: Get client with an id of X (if the client exists)
		
		
	};
	
	public Handler updateClient = (ctx) -> {
		//Controller level method for updating info on specific client. PUT
		//PUT /clients/{client_id}: Update client with an id of X (if the client exists)
		
	};
	
	public Handler deleteClient = (ctx) -> {
		//Controller level method for deleting specific client. DELETE
		//DELETE /clients/{client_id}: Delete client with an id of X (if the client exists)
		
		
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
		
		app.post("/add", newClient);
		
	}
	
}
