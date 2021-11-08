package com.revature.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.revature.DAL.DAL;
import com.revature.model.CharacterLimitException;
import com.revature.model.Client;
import com.revature.service.Service;

public class ControllerTest {

	private static Service sut;
	private static DAL mockDao; 

	// Test scenarios for addClient:
	// positive test
	// Negative test, empty/null fields
	// negative test, exceed character fields
	// negative test, whitespace between characters
	// negative test, special characters used

	@BeforeAll
	public static void DaoAndServiceSetup() {
		
		mockDao = mock(DAL.class);
		sut = new Service(mockDao);
		
	}
	
	@Test
	public void testAddClientPositive() throws SQLException {
		// arrange

		Client mockClient = new Client("Test_first_name", "Test_last_name");
		when(mockDao.newClient(eq(mockClient))).thenReturn(new Client(100, "Test_first_name", "Test_last_name"));


		// act

		Client testClient = new Client("Test_first_name", "Test_last_name");
		Client actualClient = mockDao.newClient(testClient);

		// assert

		Client expected = new Client(100, "Test_first_name", "Test_last_name");
		Assertions.assertEquals(expected, actualClient);

	}

	@Test

	public void testAddClientNegativeEmptyFirstName() {
		// Test for when client is attempted to be added when it has nothing but
		// whitespace for the first name

		// arange

		// act

		Client testClient = new Client("    ", "last_name");

		// assert

		Assertions.assertThrows(CharacterLimitException.class, () -> {

			sut.addClient(testClient);

		});

	}
	
	@Test
	public void testAddClientNegativeEmptyLastName() {
		// Test for when client is attempted to be added but it has nothing but
		// whitespace for the last name

		Client testClient = new Client("first_name", "     ");

		Assertions.assertThrows(CharacterLimitException.class, () -> {

			sut.addClient(testClient);

		});

	}

	@Test
	public void testAddClientNegativeNullFirstName() {

		// test to see if it throws exception if first name is null. Likely to happen if
		// JSON header/body is missing for a client to add.


		Client testClient = new Client("", "last_name");
		testClient.setFirst_name(null);

		Assertions.assertThrows(CharacterLimitException.class, () -> {

			sut.addClient(testClient);

		});

	}

	@Test
	public void testAddClientNegativeNullLastName() {

		// Test ti see if it throws exception if last name is null. Likely to happen if
		// JSON header/body is missing for a client to add.

		Client testClient = new Client("first_name", "last_name");
		testClient.setLast_name(null);

		Assertions.assertThrows(CharacterLimitException.class, () -> {

			sut.addClient(testClient);

		});

	}

	@Test
	public void testAddClientNegativeFirstNameTooLong() {

		// This seems a bit bushwhack, but it honestly is probably the simpliest way to
		// test this....
		String really_long_string = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

		Client testClient = new Client(really_long_string, "last_name");

		Assertions.assertThrows(CharacterLimitException.class, () -> {

			sut.addClient(testClient);

		});

	}
	

	@Test
	public void testAddClientNegativeLastNameTooLong() {

		// This seems a bit bushwhack, but it honestly is probably the simpliest way to
		// test this....
		String really_long_string = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

		Client testClient = new Client("first_name", really_long_string);

		Assertions.assertThrows(CharacterLimitException.class, () -> {

			sut.addClient(testClient);

		});

	}
	
	@Test
	public void testAddClientNegativeWhitespaceBetweenCharactersFirstName() {
		
		Client testClient = new Client("   first  name  ", "last_name");
		Assertions.assertThrows(CharacterLimitException.class, () -> {
			
			sut.addClient(testClient);
			
		});
		
	}
	
	@Test
	public void testAddClientNegativeWhitespaceBetweenCharactersLastName() {
		
		Client testClient = new Client("first_name", "   last    name   ");
		Assertions.assertThrows(CharacterLimitException.class, () -> {
			
			sut.addClient(testClient);
			
		});
		
	}
	
	@Test
	public void testAddClientNegativeSpecialCharactersFirstName() {
		
		Client testClient = new Client("fir$t_n@m3", "last_name");
		Assertions.assertThrows(CharacterLimitException.class, () -> {
			
			sut.addClient(testClient);
			
		});
		
	}
	
	@Test
	public void testAddClientNegativeSpecialCharactersLastName() {
		
		Client testClient = new Client("first_name", "l@$t_n@m3");
		Assertions.assertThrows(CharacterLimitException.class, () -> {
			
			sut.addClient(testClient);
			
		});
		
	}
	

	
	@Test
	public void testGetAllClientsPositive() throws SQLException {
		//Testing to see if get all clients works. Super simple on service side 

		ArrayList<Client> clientArray = new ArrayList<>(); 
		
		clientArray.add(new Client("John", "Doe"));
		clientArray.add(new Client("Jane", "Doe"));
		
		
		when(mockDao.getAllClients()).thenReturn(clientArray);
		assertEquals(clientArray, sut.getAllClients());
		
		
	}
	
	@Test
	public void testGetAllClientsNegativeTableEmpty() throws SQLException {

		when(mockDao.getAllClients()).thenThrow(new SQLException("No clients to get. clients table is empty"));
		
		try {
			
			sut.getAllClients();
			
		}
		
		catch(SQLException e) {
			
			assertEquals("No clients to get. clients table is empty", e.getMessage());
			
		}
		
	}
	
	//@Test 
	//commenting this out because I don't want to pass this until I've implemented the corresponding features in my DAL. 
	public void testGetAllClientsNegativeNoTable() throws SQLException {
		//This is a pretty serious issue if we run into it. Nothing our program can really do. Its an issue with the database itself. Should still handle it graciously though 
		//OK I just realized im mocking features from my DAL that I haven't implemented yet. I know this is just to test service layer, but this can be dangerous i think. 
		//Need to be sure I don't forget to implement these features. 
		when(mockDao.getAllClients()).thenThrow(new SQLException("Clients table does not exist"));
		
		try {
			
			sut.getAllClients();
			
		}
		
		catch(SQLException e) {
			
			assertEquals("Clients table does not exist", e.getMessage());
			
		}
		
	}
	
	@Test
	public void testGetClientWithIdPositive() throws SQLException {
		
		when(mockDao.getClient(eq(1))).thenReturn(new Client(1, "John", "Doe"));
		Client expectedClient = new Client(1, "John", "Doe");
		
		
		assertEquals(expectedClient, sut.getClient(1));
		
	}
	
	@Test 
	public void testGetClientWithIdNegativeDoesntExist() throws SQLException {
		
		when(mockDao.getClient(eq(1))).thenThrow(new SQLException("No client with that id"));
		
		try {
			
			sut.getClient(1);
			
		}
		
		catch(SQLException e) {
			
			assertEquals("No client with that id", e.getMessage());
			
		}
		
	}
	
	@Test 
	public void testUpdateClientWithIdPositive() {
		
		
		Client originalClient = new Client("first_name", "last_name");
		//when(mockDao.updateAccount()).thenReturn(originalClient);
		
		
	}
	
	@Test 
	public void testUpdateClientWithIdPositiveNullFirstName() {
		
		
		
	}
	
	@Test
	public void testUpdateClientWithIdPositiveNullLastName() {
		
		
		
	}
	
	@Test
	public void testUpdateClientWithIdNegativeDoesntExist() {
		
		
		
	}
	
	@Test
	public void testUpdateClientWithIdNegativeEmptyFirstName() {
		
		
		
	}
	
	@Test
	public void testUpdatClientWithIdNegativeEmptyLastName() {
		
		
		
	}
	
	@Test 
	public void testUpdateClientWithIdNegativeFirstNameTooLong() {
		
		
		
	}
	
	@Test
	public void testUpdateClientWithIdNegativeLastNameTooLong() {
		
		
		
	}
	
	@Test
	public void testUpdateClientWithIdNegativeWhitespaceBetweenCharactersFirstName() {
	
	
	}
	
	@Test
	public void testUpdateClientWithIdNegativeWhitespaceBetweenCharactersLastName() {
		
		
		
	}
	
	@Test
	public void testUpdateClientWithIdNegativeSpecialCharactersFirstName() {
		
	
		
	}
	
	@Test
	public void testUpdateClientWithIdNegativeSpecialCharactersLastName() {
		
		
		
	}
	
	@Test
	public void testDeleteClientWithIdPositive() {
		
		
		
	}
	
	@Test
	public void testDeleteClientWithIdNegativeClientDoesntExist() {
		
		
		
	}
	
	@Test
	public void testCreateAccountPositive() {
		
		
		
	}
	
	@Test 
	public void testCreateAccountNegativeNoClientWithThatId() {
		
		
		
	}
	
	@Test
	public void testCreateAccountNegativeTypeNotCorrect() {
		
		
		
	}
	
	@Test
	public void testCreateAccountNegativeNegativeFunds() {
		
		
		
	}
	
	@Test
	public void testGetAllClientAccountPositive() {
		
		
		
	}
	
	@Test
	public void testGetAllClientAccountPositiveWithFundsFilter() {
		
		
		
	}
	
	@Test
	public void testGetAllClientAccountNegativeClientDoesntExist() {
		
		
		
	}
	
	@Test
	public void testGetAllClientAccountNegativeFundsFilterDoesntMatchUp() {
		
		//I probably dont want to throw an exception here. Just return nothing, like it should...we'll see, maybe postgres will give me an error. 
		
	}
	
	@Test
	public void testGetClientAccountWithClientAndAccountIdPositive() {
		
		
		
	}
	
	@Test
	public void testGetClientAccountWithClientAndAccountIdNegativeClientDoesntExist() {
		
		
		
	}
	
	@Test
	public void testGetClientAccountWithClientAndAccountIdNegativeAccountDoesntExist() {
		
		
		
	}
	
	@Test
	public void testGetClientAccountWithClientAndAccountIdNegativeAccountNotOwnedByClient() {
		
		
		
	}
	
	@Test
	public void testDeleteAccountWithClientAndAccountIdPositive() {
		
		
		
	}
	
	@Test
	public void testDeleteAccountWithClientAndAccountIdNegativeClientDoesntExist() {
		
		
		
	}
	
	@Test
	public void testDeleteAccountWithClientAndAccountIdNegativeAccountDoesntExist() {
		
		
		
	}
	
	@Test
	public void testDeleteAccountWithClientAndAccountIdNegativeAccountDoesntBelongToClient() {
		
		
		
	}
	
	@Test
	public void testUpdateClientAccountPositive() {
		
		
		
	}
	
	@Test
	public void testUpdateClientAccountNegativeClientDoesntExist() {
		
		
		
	}
	
	@Test
	public void testUpdateClientAccountNegativeAccountDoesntExist() {
		
		
		
	}
	
	@Test
	public void testUpdateClientAccountNegativeAccountDoesntBelongToClient() {
		
		
		
	}
	

 	
	
}
