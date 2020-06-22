package com.clip.api.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.clip.api.trx.dto.ErrorType;
import com.clip.api.trx.dto.TransactionErrorDTO;
import com.clip.api.trx.dto.TransactionRequestDTO;
import com.clip.api.trx.dto.TransactionResponseDTO;
import com.clip.api.trx.model.TransactionModel;

public final class TransactionMapperUtil {

	
	private TransactionMapperUtil() {
	}

	public static TransactionResponseDTO modelToResponse (final TransactionModel trxModel) {
		return modelToDto(trxModel);		

	}
	
	public static List<TransactionResponseDTO> modelToResponse (final List<TransactionModel> lstTrxModel) {		
		 return lstTrxModel.stream().map(trxModel -> {
			 return modelToDto(trxModel); 
		 }).collect(Collectors.toList());		 		 

	}						
	
	public static TransactionErrorDTO errorToDto (final ErrorType errorType) {		
		TransactionErrorDTO trxErr = new TransactionErrorDTO();		
		trxErr.setErrorType(errorType);
		trxErr.setErrorMessage(new ArrayList<String>());
		trxErr.getErrorMessage().add(errorType.getMessage());				
		return trxErr;

	}
	
	public static TransactionErrorDTO errorToDto (final ErrorType errorType, final List<String> errorMessages) {		
		TransactionErrorDTO trxErr = new TransactionErrorDTO();		
		trxErr.setErrorType(errorType);
		trxErr.setErrorMessage(errorMessages);						
		return trxErr;

	}

	public static TransactionModel requestToModel (final TransactionRequestDTO request, final Integer userID) {
		TransactionModel model = new TransactionModel();
		model.setAmount(request.getAmount());
		model.setDate(request.getDate());
		model.setDescription(request.getDescription());
		model.setTransactionID(UUID.randomUUID());
		model.setUserID(userID);
		return model;

	}
	
	public static TransactionResponseDTO sumToResponse (Integer userID, Double sum) {		
		TransactionResponseDTO trxResp = new TransactionResponseDTO();
		trxResp.setUserID(userID);
		trxResp.setSum(sum);
		return trxResp;
	}
		
	
	private static TransactionResponseDTO modelToDto (TransactionModel trxModel) {		
		TransactionResponseDTO trxResp = new TransactionResponseDTO();
		trxResp.setAmount(trxModel.getAmount());
		trxResp.setDate(trxModel.getDate());
		trxResp.setDescription(trxModel.getDescription());
		trxResp.setTransactionID(trxModel.getTransactionID());
		trxResp.setUserID(trxModel.getUserID());
		return trxResp;

	}
	
}
