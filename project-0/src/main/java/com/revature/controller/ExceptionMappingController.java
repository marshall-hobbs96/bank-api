package com.revature.controller;

import java.sql.SQLException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.revature.model.CharacterLimitException;

import io.javalin.Javalin;

public class ExceptionMappingController {

	public void mapExceptions(Javalin app) {
		
		app.exception(UnrecognizedPropertyException.class, (e, ctx) -> {
			
			ctx.status(400);
			ctx.json(e.getMessage());
			
		});
		
		app.exception(InvalidFormatException.class, (e, ctx) -> {
			
			ctx.status(406); //Not acceptable. Body of JSON doesn't fit format required of header. 
			ctx.json(e.getMessage());
			
		});
		
		app.exception(SQLException.class, (e, ctx) -> {
			
			ctx.status(404);
			ctx.json(e.getMessage());
			
		});
		
		
		app.exception(CharacterLimitException.class, (e, ctx) -> {
			
			ctx.status(413); //413:Request entity too long. Not sure if the "entity" is intended to mean what I mean it to mean here, but it works. We've past the character limit of either first_name or last_name
			ctx.json(e.getMessage());
			
		});
		
	}
	
}
