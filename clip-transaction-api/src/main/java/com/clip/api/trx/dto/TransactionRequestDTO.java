package com.clip.api.trx.dto;

public class TransactionRequestDTO extends TransactionBaseDTO {

	@Override
	public String toString() {
		return "TransactionRequestDTO [amount=" + getAmount() + ", description=" + getDescription()
				+ ", date=" + getDate() + "]";
	}
	
	
		
}
