package com.revature.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.DAL.DAL;
import com.revature.controller.Controller;
import com.revature.controller.ExceptionMappingController;
import com.revature.service.Service;

import io.javalin.Javalin;

public class Application {
	

	public static void main(String[] args) {
		
		//Need to establish connection for DAL layer first. 
		//Then set up javalin endpoints for controller layer. 
		DAL.establishConnection(System.getenv("db_url"), System.getenv("db_username"), System.getenv("db_password"));
		
		Javalin app = Javalin.create((config) -> {
			
			config.enableCorsForOrigin("http://localhost:5500", "http://127.0.0.1:5500", "http://revature-project-0.s3-website.us-east-2.amazonaws.com");
			
		});

		Logger logger = LoggerFactory.getLogger(Application.class);
		
		app.before(ctx -> {
			
			logger.info(ctx.method() + " request received to the " + ctx.path() + " endpoint");
			
		});
		
		
		
		Controller controller = new Controller(new Service(new DAL()));
		controller.registerEndpoint(app);
		
		ExceptionMappingController exceptionController = new ExceptionMappingController();
		exceptionController.mapExceptions(app);
		
		app.start(8080);
		
	}

}
