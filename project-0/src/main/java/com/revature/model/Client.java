package com.revature.model;

import java.util.Objects;

public class Client { // Client class for making client objects. 1 to 1 with clients in database.
						// Might have to change later
						// depending on if I want to add/remove features or functionality. We'll see.

	private int client_id; // SERIAL PRIMARY KEY in postgresql
	private String first_name; // VARCHAR[255] NOT NULL <===IMPORTANT, NEED TO HAVE EXCEPTIONS FOR STRINGS
								// LONGER THAN 255
	private String last_name; // VARCHAR[255] NOT NULL <=== same here. Need exceptions for null stuff too...
	private int num_accounts; // INTEGER, might not utilize. Not necessary for project requirements, but I
								// want to add my own features
								// as well. Hopefully add to my portfolio. But I also don't know if this would
								// be useful for any features
								// really.......

	public Client() {
	} // empty constructor. Don't know if I want to utilize this yet
	
	public Client(int client_id, String first_name, String last_name) {
		
		this.client_id = client_id; 
		this.first_name = first_name;
		this.last_name = last_name;
		this.num_accounts = 0; 
		
	}

	public Client(String first_name, String last_name, int num_accounts) {

		client_id = -1; // Shouldn't be used, but setting to -1 in case it does.
		this.first_name = first_name;
		this.last_name = last_name;
		this.num_accounts = num_accounts;

	}

	public Client(int client_id, String first_name, String last_name, int num_accounts) {

		// will probably use for receiving data from DAL
		this(first_name, last_name, num_accounts);
		this.client_id = client_id;

	}
	
	public Client(String first_name, String last_name) {
		
		this.first_name = first_name;
		this.last_name = last_name;
		
		//Need to refactor this. Know I should be using constructor chaining. Not a priority to do right now.
		
	}

	// getters and setters and equals and hashcode oh my
	// i.e. dont bother going below here, all boring stuff

	// ******************************************************************************//

	@Override
	public String toString() {
		return "Client [client_id=" + client_id + ", first_name=" + first_name + ", last_name=" + last_name
				+ ", num_accounts=" + num_accounts + "]";
	}

	public int getClient_id() {
		return client_id;
	}

	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public int getNum_accounts() {
		return num_accounts;
	}

	public void setNum_accounts(int num_accounts) {
		this.num_accounts = num_accounts;
	}

	@Override
	public int hashCode() {
		return Objects.hash(client_id, first_name, last_name, num_accounts);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return client_id == other.client_id && Objects.equals(first_name, other.first_name)
				&& Objects.equals(last_name, other.last_name) && num_accounts == other.num_accounts;
	}

}
