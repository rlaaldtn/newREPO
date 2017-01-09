package com.example;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class Customer {
	
	@Id
	public String customerId;
	
	public String matchingId;
<<<<<<< HEAD
//	public Date accessDate;
//	public boolean isChatting;
=======
	public Date accessDate;
>>>>>>> 9c25ce8bdd12f369d3a1507b0e9904418fd0bf3f
	
	public Customer() {}
	
	public Customer(String id, Date accessDate) {
		this.customerId = id;
		this.accessDate = accessDate;
		this.matchingId = "empty";
	}
	
	public String getId() {
		return customerId;
	}
	public void setId(String id) {
		this.customerId = id;
	}
	public String getMatchingId() {
		return matchingId;
	}
	public void setMatchingId(String matchingId) {
		this.matchingId = matchingId;
	}
<<<<<<< HEAD
//	public Date getAccessDate() {
//		return accessDate;
//	}
//	public void setAccessDate(Date accessDate) {
//		this.accessDate = accessDate;
//	}
//	public boolean isChatting() {
//		return isChatting;
//	}
//	public void setChatting(boolean isChatting) {
//		this.isChatting = isChatting;
//	}
=======
	public Date getAccessDate() {
		return accessDate;
	}
	public void setAccessDate(Date accessDate) {
		this.accessDate = accessDate;
	}
>>>>>>> 9c25ce8bdd12f369d3a1507b0e9904418fd0bf3f
	
	@Override
	public String toString() {
		return String.format("Customer[id=%s, accessDate=%s, matched=%s]", customerId, accessDate, matchingId);
	}
	
	
}
