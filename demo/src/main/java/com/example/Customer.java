package com.example;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class Customer {
	
	@Id
	public String customerId;
	
//	public String matchingId;
//	public Date accessDate;
//	public boolean isChatting;
	
	public Customer() {}
	
	public Customer(String id) {
		this.customerId = id;
//		this.accessDate = accessDate;
//		this.matchingId = null;
//		this.isChatting = false;
	}
	
	public String getId() {
		return customerId;
	}
	public void setId(String id) {
		this.customerId = id;
	}
//	public String getMatchingId() {
//		return matchingId;
//	}
//	public void setMatchingId(String matchingId) {
//		this.matchingId = matchingId;
//	}
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
	
	@Override
	public String toString() {
		return String.format("Customer[id=%s]", customerId);
	}
	
	
}
