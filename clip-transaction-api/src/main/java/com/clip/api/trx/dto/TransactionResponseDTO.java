package com.clip.api.trx.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TransactionResponseDTO extends TransactionBaseDTO {

	private Integer userID;
	private UUID transactionID;
	private Double sum;	

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public UUID getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(UUID transactionID) {
		this.transactionID = transactionID;
	}

	public Double getSum() {
		return sum;
	}

	public void setSum(Double sum) {
		this.sum = sum;
	}

	@Override
	public String toString() {
		return "TransactionResponseDTO [userID=" + userID + ", transactionID=" + transactionID + ", sum=" + sum
				+ ", amount=" + getAmount() + ", description=" + getDescription() + ", date=" + getDate()
				+ "]";
	}
	
	
	
}
