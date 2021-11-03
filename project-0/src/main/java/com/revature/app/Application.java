package com.revature.app;

import com.revature.DAL.DAL;
import com.revature.controller.Controller;
import com.revature.service.Service;

import io.javalin.Javalin;

public class Application {

	public static void main(String[] args) {
		
		//Need to establish connection for DAL layer first. 
		//Then set up javalin endpoints for controller layer. 
		
		DAL.establishConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "password");
		
		Javalin app = Javalin.create(); //Dont need to add static files because this project doesnt
										//require an HTML frontend. Will probably add later though 
										//just because it sounds fun and will make a good addition to 
										//my portfolio I think 

		Controller controller = new Controller(new Service());
		controller.registerEndpoint(app);
		app.start(8080);
		
	}

}
