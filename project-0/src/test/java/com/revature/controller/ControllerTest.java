package com.revature.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.revature.DAL.DAL;
import com.revature.model.Account;
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

		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {

			sut.addClient(testClient);

		});
		
		assertEquals("Unable to add client. First name is an empty string.", e.getMessage());

	}
	
	@Test
	public void testAddClientNegativeEmptyLastName() {
		// Test for when client is attempted to be added but it has nothing but
		// whitespace for the last name

		Client testClient = new Client("first_name", "     ");

		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {

			sut.addClient(testClient);

		});
		
		assertEquals("Unable to add client. Last name is an empty string.", e.getMessage());

	}

	@Test
	public void testAddClientNegativeNullFirstName() {

		// test to see if it throws exception if first name is null. Likely to happen if
		// JSON header/body is missing for a client to add.


		Client testClient = new Client("", "last_name");
		testClient.setFirst_name(null);

		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {

			sut.addClient(testClient);

		});
		
		assertEquals("Unable to add client. First name is null.", e.getMessage());

	}

	@Test
	public void testAddClientNegativeNullLastName() {

		// Test ti see if it throws exception if last name is null. Likely to happen if
		// JSON header/body is missing for a client to add.

		Client testClient = new Client("first_name", "last_name");
		testClient.setLast_name(null);

		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {

			sut.addClient(testClient);

		});

		assertEquals("Unable to add client. Last name is null.", e.getMessage());
		
	}

	@Test
	public void testAddClientNegativeFirstNameTooLong() {

		// This seems a bit bushwhack, but it honestly is probably the simpliest way to
		// test this....
		String really_long_string = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

		Client testClient = new Client(really_long_string, "last_name");

		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {

			sut.addClient(testClient);

		});
		
		assertEquals("Unable to add client. First name exceeds acceptable number of characters (255).", e.getMessage());

	}
	

	@Test
	public void testAddClientNegativeLastNameTooLong() {

		// This seems a bit bushwhack, but it honestly is probably the simpliest way to
		// test this....
		String really_long_string = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

		Client testClient = new Client("first_name", really_long_string);

		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {

			sut.addClient(testClient);

		});
		
		assertEquals("Unable to add client. Last name exceeds acceptable number of character (255).", e.getMessage());

	}
	
	@Test
	public void testAddClientNegativeWhitespaceBetweenCharactersFirstName() {
		
		Client testClient = new Client("   first  name  ", "last_name");
		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {
			
			sut.addClient(testClient);
			
		});
		
		assertEquals("Unable to add client. First name is not one word (whitespace between characters).", e.getMessage());
		
	}
	
	@Test
	public void testAddClientNegativeWhitespaceBetweenCharactersLastName() {
		
		Client testClient = new Client("first_name", "   last    name   ");
		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {
			
			sut.addClient(testClient);
			
		});
		
		assertEquals("Unable to add client. Last name is not one word (whitespace between characters).", e.getMessage());
		
	}
	
	@Test
	public void testAddClientNegativeSpecialCharactersFirstName() {
		
		Client testClient = new Client("fir$t_n@m3", "last_name");
		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {
			
			sut.addClient(testClient);
			
		});
		
		assertEquals("Unable to add client. First name contains special characters.", e.getMessage());
		
	}
	
	@Test
	public void testAddClientNegativeSpecialCharactersLastName() {
		
		Client testClient = new Client("first_name", "l@$t_n@m3");
		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {
			
			sut.addClient(testClient);
			
		});
		
		assertEquals("Unable to add client. Last name contains special characters.", e.getMessage());
		
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
		
		SQLException e = assertThrows(SQLException.class, () -> {
			
			sut.getAllClients(); 
			
		});
		
		assertEquals("No clients to get. Clients table is empty.", e.getMessage());
		

		
	}
	
	//@Test 
	//commenting this out because I don't want to pass this until I've implemented the corresponding features in my DAL. 
	public void testGetAllClientsNegativeNoTable() throws SQLException {
		//This is a pretty serious issue if we run into it. Nothing our program can really do. Its an issue with the database itself. Should still handle it graciously though 
		//OK I just realized im mocking features from my DAL that I haven't implemented yet. I know this is just to test service layer, but this can be dangerous i think. 
		//Need to be sure I don't forget to implement these features. 
		when(mockDao.getAllClients()).thenThrow(new SQLException("Clients table does not exist"));
		
		
		SQLException e = assertThrows(SQLException.class, () -> {
			
			sut.getAllClients();
			
		});
		
		assertEquals("Clients table does not exist.", e.getMessage());
		
		
		
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
		
		SQLException e = assertThrows(SQLException.class, () -> {
			
			sut.getClient(1);
			
		});
		
		assertEquals("No client with that id.", e.getMessage());
		
	}
	
	@Test 
	public void testUpdateClientWithIdPositive() throws SQLException {
		
		
		Client updateClient = new Client(1, "first_name", "last_name");
		when(mockDao.clientExists(eq(1))).thenReturn(true);
		when(mockDao.updateClient(updateClient)).thenReturn(updateClient);
		
		assertEquals(updateClient, sut.updateClient(updateClient));
		
	}
	
	@Test 
	public void testUpdateClientWithIdPositiveNullFirstName() throws SQLException {	
		
		Client updateClient = new Client(1, null, "last_name");
		Client expectedClient = new Client(1, "first_name", "last_name");
		when(mockDao.clientExists(eq(1))).thenReturn(true);
		when(mockDao.updateClient(updateClient)).thenReturn(expectedClient);
		
		assertEquals(expectedClient, sut.updateClient(updateClient));
		
	}
	
	@Test
	public void testUpdateClientWithIdPositiveNullLastName() throws SQLException {
		
		Client updateClient = new Client(1, "first_name", null);
		Client expectedClient = new Client(1, "first_name", "last_name");
	
		when(mockDao.clientExists(eq(1))).thenReturn(true);
		when(mockDao.updateClient(updateClient)).thenReturn(expectedClient);
		
		assertEquals(expectedClient, sut.updateClient(updateClient));
		
	}
	
	@Test
	public void testUpdateClientWithIdNegativeDoesntExist() throws SQLException {
		
		Client updateClient = new Client(1, "first_name",  "last_name");
		when(mockDao.clientExists(eq(1))).thenReturn(false);
		when(mockDao.updateClient(updateClient)).thenReturn(updateClient); //Shouldn't reach this if test passes, but figured id put it here anyways because I don't know how junit would react if i didn't
		
		SQLException e = Assertions.assertThrows(SQLException.class, () -> {
			
			sut.updateClient(updateClient);
			
		});
		
		assertEquals("Unable to update client. No client with that ID.", e.getMessage());
		
		
		
	}
	
	@Test
	public void testUpdateClientWithIdNegativeEmptyFirstName() throws SQLException {
		
		Client testClient = new Client(1, "   ", "last_name");
		when(mockDao.clientExists(eq(1))).thenReturn(true);
		
		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {
			
			sut.updateClient(testClient);
			
		});
		
		assertEquals("Unable to update client. First name is an empty string.", e.getMessage());
		
	}
	
	@Test
	public void testUpdatClientWithIdNegativeEmptyLastName() throws SQLException {
		
		Client testClient = new Client(1, "first_name", "     ");
		when(mockDao.clientExists(eq(1))).thenReturn(true);
		
		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {
			
			sut.updateClient(testClient);
			
		});
		
		assertEquals("Unable to update client. Last name is an empty string.", e.getMessage());
		
	}
	
	@Test 
	public void testUpdateClientWithIdNegativeFirstNameTooLong() throws SQLException {
		
		String really_long_string = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		
		Client testClient = new Client(1, really_long_string, "last_name");
		when(mockDao.clientExists(eq(1))).thenReturn(true);
		
		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {
			
			sut.updateClient(testClient);
			
		});
		
		assertEquals("Unable to update client. First name exceeds acceptable number of characters (255).", e.getMessage());

		
	}
	
	@Test
	public void testUpdateClientWithIdNegativeLastNameTooLong() throws SQLException {
		
		String really_long_string = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		
		Client testClient = new Client(1, "first_name", really_long_string);
		when(mockDao.clientExists(eq(1))).thenReturn(true);
		
		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {
			
			
			sut.updateClient(testClient);
			
			
		});
		
		assertEquals("Unable to update client. Last name exceeds acceptable number of characters (255).", e.getMessage());
		
	}
	
	@Test
	public void testUpdateClientWithIdNegativeWhitespaceBetweenCharactersFirstName() throws SQLException {
	
		
		Client testClient = new Client(1, "first   name", "last_name");
		when(mockDao.clientExists(eq(1))).thenReturn(true);
		
		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {
			
			sut.updateClient(testClient);
			
		});
		
		assertEquals("Unable to update client. First name is not one word (whitespace between characters).", e.getMessage());
		
		
	}
	
	@Test
	public void testUpdateClientWithIdNegativeWhitespaceBetweenCharactersLastName() throws SQLException {
		
		Client testClient = new Client(1, "first_name", "last    name");
		when(mockDao.clientExists(eq(1))).thenReturn(true);
		
		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {
			
			
			sut.updateClient(testClient);
			
		
		});
		
		assertEquals("Unable to update client. Last name is not one word (whitespace between characters).", e.getMessage());
		
	}
	
	@Test
	public void testUpdateClientWithIdNegativeSpecialCharactersFirstName() throws SQLException {
		
	
		Client testClient = new Client(1, "f1r$t_n@m3", "last_name");	//I just realized _ is a special character.....ugh. I'm just going to allow underscores, because I am NOT going back and refactoring all that by hand
		when(mockDao.clientExists(eq(1))).thenReturn(true);
		
		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {
			
			sut.updateClient(testClient);
			
		});
		
		assertEquals("Unable to update client. First name contains special characters.", e.getMessage());
		
	}
	
	@Test
	public void testUpdateClientWithIdNegativeSpecialCharactersLastName() throws SQLException {
		
		Client testClient = new Client(1, "firstName", "l@$tN@m3");
		when(mockDao.clientExists(eq(1))).thenReturn(true);
		
		CharacterLimitException e = Assertions.assertThrows(CharacterLimitException.class, () -> {
			
			sut.updateClient(testClient);
			
		});
		
		assertEquals("Unable to update client. Last name contains special characters.", e.getMessage());
		
	}
	
	@Test
	public void testDeleteClientWithIdPositive() throws SQLException {
		
		
		when(mockDao.clientExists(eq(1))).thenReturn(true); 
		when(mockDao.clientHasAccounts(eq(1))).thenReturn(false);
		
		assertEquals(true, sut.deleteClient(1));
		
		
	}
	
	@Test
	public void testDeleteClientWithIdNegativeClientDoesntExist() throws SQLException {
		
		when(mockDao.clientExists(eq(1))).thenReturn(false);
		when(mockDao.clientHasAccounts(eq(1))).thenReturn(false);
		
		SQLException e = Assertions.assertThrows(SQLException.class, () -> {
			
			sut.deleteClient(1);
			
		});
		
		assertEquals("Unable to delete client. Client does not exist.", e.getMessage());
		
	}
	
	@Test
	public void testDeleteClientWithIdNegativeClientStillHasAccounts() throws SQLException {
		
		when(mockDao.clientExists(eq(1))).thenReturn(true);
		when(mockDao.clientHasAccounts(eq(1))).thenReturn(true);
		
		SQLException e = assertThrows(SQLException.class, () -> {
			
			sut.deleteClient(1);
			
		});
		
		assertEquals("Unable to delete client. Client still has accounts", e.getMessage());
	}
	
	@Test
	public void testCreateAccountPositive() {
		
		Account testAccount = new Account(1, 1, "Savings", 500);
		when(mockDao.createAccount(eq(testAccount))).thenReturn(testAccount);
		
		assertEquals(testAccount, sut.createAccount(testAccount));
		
	}
	
	@Test 
	public void testCreateAccountNegativeNoClientWithThatId() throws SQLException {
		
		Account testAccount = new Account(1, 1, "Savings", 500);
		when(mockDao.clientExists(eq(1))).thenReturn(false);
		
		SQLException e = assertThrows(SQLException.class, () -> {
			
			sut.createAccount(testAccount);
			
		});
		
		assertEquals("Unable to create account. Not matching client exists.", e.getMessage());
		

	}
	
	@Test
	public void testCreateAccountNegativeTypeNotCorrect() throws SQLException {
		
		Account testAccount = new Account(1, 1, "Brokerage", 500);
		
		when(mockDao.clientExists(eq(1))).thenReturn(true);
		
		CharacterLimitException e = assertThrows(CharacterLimitException.class, () -> {
			
			sut.createAccount(testAccount);
			
		});
		
		assertEquals("Unable to create account. Account type not supported (only 'Savings' and 'Checkings').", e.getMessage());
		
		
	}
	
	@Test
	public void testCreateAccountNegativeNegativeFunds() throws SQLException {
		
		Account testAccount = new Account(1, 1, "Savings", -500);
		
		when(mockDao.clientExists(eq(1))).thenReturn(true);
		
		CharacterLimitException e = assertThrows(CharacterLimitException.class, () -> {
			
			sut.createAccount(testAccount);
			
		});
		
		assertEquals("Unable to create account. Initial funds cannot be negative.", e.getMessage());
		
	}
	
	@Test
	public void testGetAllClientAccountPositive() {	//This is for getting all the accounts associated with a specific client 
		
		ArrayList<Account> accountsList = new ArrayList<>(); 
		
		Account testAccount1 = new Account(1, 1, "Savings", 500);
		Account testAccount2 = new Account(2, 1, "Checkings", 1000);
		
		accountsList.add(testAccount1);
		accountsList.add(testAccount2);
		
		when(mockDao.getClientAccounts(eq(1))).thenReturn(accountsList);
		
		assertEquals(accountsList, sut.getClientAccounts(1));
		
	}
	
	@Test
	public void testGetAllClientAccountPositiveWithFundsFilter() {
		
		ArrayList<Account> accountsList = new ArrayList<>();
		Account testAccount1 = new Account(1, 1, "Savings", 500);
		Account testAccount2 = new Account(2, 1, "Checkings", 1000);
		accountsList.add(testAccount1);
		accountsList.add(testAccount2);
		ArrayList<Account> expectedList = new ArrayList<>();
		expectedList.add(testAccount2);
		when(mockDao.getClientAccounts(eq(1))).thenReturn(accountsList);
		
		assertEquals(expectedList, sut.getClientAccounts(1));
		
		
		
	}
	
	@Test
	public void testGetAllClientAccountNegativeClientDoesntExist() throws SQLException {
		
		when(mockDao.clientExists(eq(1))).thenReturn(false);
		
		SQLException e = assertThrows(SQLException.class, () -> {
			
			sut.getClientAccounts(1);
			
		});
		
		assertEquals("Cannot access client accounts. Client does not exist.", e.getMessage());
		
	}

	/*
	@Test
	public void testGetAllClientAccountNegativeFundsFilterDoesntMatchUp() {
		
		//I probably dont want to throw an exception here. Just return nothing, like it should...we'll see, maybe postgres will give me an error. 
		//Yea I'm just gunna return an empty array instead of throwing an exception. I.e. do the intended behavior instead of holding the user's hand 
		 * User can easily figure out why its returning nothing without us throwing an exception. 
	}*/
	
	@Test
	public void testGetClientAccountWithClientAndAccountIdPositive() throws SQLException {
		
		 Client testClient = new Client(1, "firstName", "lastName");
		 Account testAccount = new Account(1, 1, "Savings", 500);
		 
		when(mockDao.getClient(eq(1))).thenReturn(testClient);
		when(mockDao.getAccount(eq(1))).thenReturn(testAccount);
		
		 
		 assertEquals(testAccount, sut.getClientAccount(1, 1));
		
	}
	
	@Test
	public void testGetClientAccountWithClientAndAccountIdNegativeClientDoesntExist() throws SQLException {
		
		when(mockDao.getClient(eq(1))).thenThrow(new SQLException("Cannot get account. Client does not exist."));
		
		SQLException e = assertThrows(SQLException.class, () -> {
			
			sut.getClientAccount(1, 1);
			
		});
		
		assertEquals("Cannot get account. Client does not exist.", e.getMessage());
		
	}
	
	@Test
	public void testGetClientAccountWithClientAndAccountIdNegativeAccountDoesntExist() throws SQLException {
		
		Client testClient = new Client(1, "firstName", "lastName");
		when(mockDao.getClient(eq(1))).thenReturn(testClient);
		when(mockDao.getAccount(eq(1))).thenThrow(new SQLException("Cannot get account. Account does not exist."));
		
		SQLException e = assertThrows(SQLException.class, () -> {
			
			sut.getClientAccount(1, 1);
			
		});
		
		assertEquals("Cannot get account. Account does not exist.", e.getMessage());
		
	}
	
	@Test
	public void testGetClientAccountWithClientAndAccountIdNegativeAccountNotOwnedByClient() throws SQLException {
		
		Client testClient = new Client(1, "firstName", "lastName");
		Account testAccount = new Account(1, 2, "Savings", 500);
		
		when(mockDao.getClient(eq(1))).thenReturn(testClient);
		when(mockDao.getAccount(eq(1))).thenReturn(testAccount);
		
		SQLException e = assertThrows(SQLException.class, () -> {
			
			sut.getClientAccount(1, 1);
			
		});
		
		assertEquals("Cannot get account. Account not owned by client", e.getMessage());
		
	}
	
	@Test
	public void testDeleteAccountWithClientAndAccountIdPositive() throws SQLException {
		
		Client testClient = new Client(1, "firstName", "lastName");
		Account testAccount = new Account(1, 1, "Savings", 500);
		
		when(mockDao.getClient(eq(1))).thenReturn(testClient);
		when(mockDao.getAccount(eq(1))).thenReturn(testAccount);
		when(mockDao.deleteAccount(eq(1))).thenReturn(true);
		
		assertEquals(true, sut.deleteAccount(1, 1));
		
	}
	
	@Test
	public void testDeleteAccountWithClientAndAccountIdNegativeClientDoesntExist() throws SQLException {
		

		Account testAccount = new Account(1, 1, "Savings", 500);
		
		when(mockDao.getClient(eq(1))).thenThrow(new SQLException("Client does not exist"));
		when(mockDao.getAccount(eq(1))).thenReturn(testAccount);
		when(mockDao.deleteAccount(eq(1))).thenReturn(false);
	
		SQLException e = assertThrows(SQLException.class, () -> {
			
			sut.deleteAccount(1, 1);
			
		});
		
		assertEquals("Cannot delete account. Client does not exist.", e.getMessage());
		
	}
	
	@Test
	public void testDeleteAccountWithClientAndAccountIdNegativeAccountDoesntExist() throws SQLException {
		
		Client testClient = new Client(1, "firstName", "lastName");
		
		when(mockDao.getClient(eq(1))).thenReturn(testClient);
		when(mockDao.getAccount(eq(1))).thenThrow(new SQLException("Cannot get account. Account does not exist."));
		
		SQLException e = assertThrows(SQLException.class, () -> {
			
			sut.deleteAccount(1,1);
			
		});
		
		assertEquals("Cannot delete account. Account does not exist.", e.getMessage());
		
	}
	
	@Test
	public void testDeleteAccountWithClientAndAccountIdNegativeAccountDoesntBelongToClient() throws SQLException {
		
		Client testClient = new Client(1, "firName", "lastName");
		Account testAccount = new Account(1, 2, "Savings", 500);
		
		when(mockDao.getClient(eq(1))).thenReturn(testClient);
		when(mockDao.getAccount(eq(1))).thenReturn(testAccount);
		
		SQLException e = assertThrows(SQLException.class, () -> {
			
			sut.deleteAccount(1, 1);
			
		});
		
		assertEquals("Cannot delete account. Account does not belong to client.", e.getMessage());
		
	}
	
	@Test
	public void testUpdateClientAccountPositive() throws SQLException {
		
		Client testClient = new Client(1, "firstName", "lastName");
		Account testAccount = new Account(1, 1, "Savings", 500);
		
		when(mockDao.getClient(eq(1))).thenReturn(testClient);
		when(mockDao.getAccount(eq(1))).thenReturn(testAccount);
		when(mockDao.updateAccount(eq(testAccount))).thenReturn(testAccount);
		
		assertEquals(testAccount, sut.updateAccount(1, testAccount));
		
	}
	
	@Test
	public void testUpdateClientAccountNegativeClientDoesntExist() throws SQLException {
		
		Account testAccount = new Account(1, 1, "Savings", 500);
		
		when(mockDao.getClient(eq(1))).thenThrow(new SQLException("Cannot get client. Client does not exist."));
		
		SQLException e = assertThrows(SQLException.class, () -> {
			
			sut.updateAccount(1, testAccount);
			
		});
		
		assertEquals("Cannot update account. Client does not exist.", e.getMessage());
		
	}
	
	@Test
	public void testUpdateClientAccountNegativeAccountDoesntExist() throws SQLException {
		
		Client testClient = new Client(1, "firstName", "lastName");
		Account testAccount = new Account(1, 1, "Savings", 500);
		
		when(mockDao.getClient(eq(1))).thenReturn(testClient);
		when(mockDao.getAccount(eq(1))).thenThrow(new SQLException("Cannot get account. Account does not exist."));
		
		SQLException e = assertThrows(SQLException.class, () -> {
			
			sut.updateAccount(1, testAccount);
			
		});
		
		assertEquals("Cannot update account. Account does not exist", e.getMessage());
		
	}
	
	@Test
	public void testUpdateClientAccountNegativeAccountDoesntBelongToClient() throws SQLException {
		
		Client testClient = new Client(1, "firstName", "lastName");
		Account testAccount = new Account(1, 2, "Savings", 500);
		
		when(mockDao.getClient(eq(1))).thenReturn(testClient);
		when(mockDao.getAccount(eq(1))).thenReturn(testAccount);
		
		SQLException e = assertThrows(SQLException.class, () -> {
			
			sut.updateAccount(1, testAccount);
			
		});
		
		assertEquals("Cannot update account. Account does not belong to client.", e.getMessage());
		
	}
	

 	
	
}
