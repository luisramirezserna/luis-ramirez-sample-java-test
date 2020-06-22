package com.clip.api.trx.dto;

public class TransactionBaseDTO {

		
	private Double amount;
	private String description;
	private String date;
			
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
	
	@Override
	public String toString() {
		return "TransactionBaseDTO [amount=" + amount + ", description=" + description + ", date=" + date + "]";
	}
	
	
	
}
