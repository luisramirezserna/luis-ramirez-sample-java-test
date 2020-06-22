package com.clip.api.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("api.clip.message.error")
public class ErrorMessageProperties {
	
	private String general;
	private String readFile;
	private String writeFile;
	private String transactionNotFound;
	private String file;
	private String invalidRequest;
	private String emptyBody;
	private String emptyAmount;
	private String emptyDate;
	private String invalidDate;
	private String invalidUser;
	private String invalidUserName;
	private String invalidToken; 
	private String invalidUseridParam;
	private String invalidTrxidParam;
		
	public String getReadFile() {
		return readFile;
	}
	public String getGeneral() {
		return general;
	}
	public void setGeneral(String general) {
		this.general = general;
	}
	public void setReadFile(String readFile) {
		this.readFile = readFile;
	}
	public String getWriteFile() {
		return writeFile;
	}
	public void setWriteFile(String writeFile) {
		this.writeFile = writeFile;
	}
	public String getTransactionNotFound() {
		return transactionNotFound;
	}
	public void setTransactionNotFound(String transactionNotFound) {
		this.transactionNotFound = transactionNotFound;
	}	
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getInvalidRequest() {
		return invalidRequest;
	}
	public void setInvalidRequest(String invalidRequest) {
		this.invalidRequest = invalidRequest;
	}
	public String getEmptyBody() {
		return emptyBody;
	}
	public void setEmptyBody(String emptyBody) {
		this.emptyBody = emptyBody;
	}
	public String getEmptyAmount() {
		return emptyAmount;
	}
	public void setEmptyAmount(String emptyAmount) {
		this.emptyAmount = emptyAmount;
	}
	public String getEmptyDate() {
		return emptyDate;
	}
	public void setEmptyDate(String emptyDate) {
		this.emptyDate = emptyDate;
	}
	public String getInvalidDate() {
		return invalidDate;
	}
	public void setInvalidDate(String invalidDate) {
		this.invalidDate = invalidDate;
	}
	public String getInvalidUser() {
		return invalidUser;
	}
	public void setInvalidUser(String invalidUser) {
		this.invalidUser = invalidUser;
	}
	public String getInvalidUserName() {
		return invalidUserName;
	}
	public void setInvalidUserName(String invalidUserName) {
		this.invalidUserName = invalidUserName;
	}
	public String getInvalidToken() {
		return invalidToken;
	}
	public void setInvalidToken(String invalidToken) {
		this.invalidToken = invalidToken;
	}
	public String getInvalidUseridParam() {
		return invalidUseridParam;
	}
	public void setInvalidUseridParam(String invalidUseridParam) {
		this.invalidUseridParam = invalidUseridParam;
	}
	public String getInvalidTrxidParam() {
		return invalidTrxidParam;
	}
	public void setInvalidTrxidParam(String invalidTrxidParam) {
		this.invalidTrxidParam = invalidTrxidParam;
	}
	
}