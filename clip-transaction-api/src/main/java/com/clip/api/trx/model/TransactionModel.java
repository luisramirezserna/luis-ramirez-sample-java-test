package com.clip.api.trx.model;

import java.util.UUID;

public class TransactionModel {

	private UUID transactionID;
	private Integer userID;	
	private Double amount;
	private String description;
	private String date;
	
	
	public UUID getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(UUID transactionID) {
		this.transactionID = transactionID;
	}
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	
}
