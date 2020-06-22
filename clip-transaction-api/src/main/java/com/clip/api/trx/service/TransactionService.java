package com.clip.api.trx.service;

import java.util.List;
import java.util.UUID;

import com.clip.api.trx.dto.TransactionReportResponseDTO;
import com.clip.api.trx.dto.TransactionRequestDTO;
import com.clip.api.trx.dto.TransactionResponseDTO;

public interface TransactionService {
	
	TransactionResponseDTO addTransaction(TransactionRequestDTO txnRequest, Integer userID);
	
	TransactionResponseDTO getTransaction(Integer userID, UUID transactionID);
	
	List<TransactionResponseDTO> getListTransaction(Integer userID);
	
	TransactionResponseDTO sumTransaction(Integer userID);
	
	List<TransactionReportResponseDTO> reportTransaction(Integer userID);
	
	TransactionResponseDTO randomTransaction(); 
}
