package com.revature.model;

import java.util.Objects;

public class Account {	
	//Account class. 1 to 1 of whats in postgresql database. 

	private int account_id; 	//SERIAL PRIMARY KEY
	private int client_id;		//FOREIGN KEY, matches client_id of Clients in Clients table/Clients in Client class. 
	private String account_type;	//VARCHAR[255] 
	private int funds;				//dollar dollar bills yall 
	
	public Account() {} //empty constructor, probably wont use
	
	public Account(int client_id, String account_type, int funds) {		//will probably use for sending through DAL 
		
		//
		account_id = -1; //shouldn't be using anyways, but setting to -1 so at least we know it isnt valid if it is accessed. 
		this.client_id = client_id; 
		this.account_type = account_type;
		this.funds = funds; 
		
	}
	
	public Account(int account_id, int client_id, String account_type, int funds) {	//will probably use for receiving DAL info 
		
		this(client_id, account_type, funds);
		this.account_id = account_id; 
		
	}
	
	
	
	//Boring stuff below here
	//******************************************//

	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int acccount_id) {
		this.account_id = acccount_id;
	}

	public int getClient_id() {
		return client_id;
	}

	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}

	public int getFunds() {
		return funds;
	}

	public void setFunds(int funds) {
		this.funds = funds;
	}

	@Override
	public int hashCode() {
		return Objects.hash(account_id, account_type, client_id, funds);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		return account_id == other.account_id && Objects.equals(account_type, other.account_type)
				&& client_id == other.client_id && funds == other.funds;
	}
	
	
	
}
