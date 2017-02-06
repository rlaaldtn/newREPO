package com.example;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class Customer {
	
	@Id
	private String customerId;
	
	private String matchingId;
	private Date accessDate;
	
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
	public Date getAccessDate() {
		return accessDate;
	}
	public void setAccessDate(Date accessDate) {
		this.accessDate = accessDate;
	}
	
	@Override
	public String toString() {
		return String.format("Customer[id=%s, accessDate=%s, matched=%s]", customerId, accessDate, matchingId);
	}
	
	
}
