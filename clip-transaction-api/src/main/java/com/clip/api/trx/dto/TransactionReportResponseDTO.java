package com.clip.api.trx.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TransactionReportResponseDTO extends TransactionResponseDTO {
		
	private String weekStartDate;
	private String weekFinishDate;
	private Integer quantity;
	private Double totalAmount;
	
	public TransactionReportResponseDTO(Integer userID) {
		super();
		super.setUserID(userID);
		super.setAmount(0.0);
		this.totalAmount = 0.0;
		this.quantity = 0;
	}
	
	public TransactionReportResponseDTO(Integer userID, Double amount, Integer quantity) {
		super();
		super.setUserID(userID);
		super.setAmount(amount);
		this.totalAmount = 0.0;
		this.quantity = quantity;
	}
	
	public String getWeekStartDate() {
		return weekStartDate;
	}
	public void setWeekStartDate(String weekStartDate) {
		this.weekStartDate = weekStartDate;
	}
	public String getWeekFinishDate() {
		return weekFinishDate;
	}
	public void setWeekFinishDate(String weekFinishDate) {
		this.weekFinishDate = weekFinishDate;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	@Override
	public String toString() {
		return "TransactionReportResponseDTO [weekStartDate=" + weekStartDate + ", weekFinishDate=" + weekFinishDate
				+ ", quantity=" + quantity + ", totalAmount=" + totalAmount + ", amount=" + getAmount() + "]";
	}
	
	
	
}
