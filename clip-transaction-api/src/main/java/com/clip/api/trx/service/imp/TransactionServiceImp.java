package com.clip.api.trx.service.imp;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clip.api.trx.dao.TransactionDao;
import com.clip.api.trx.dto.ErrorType;
import com.clip.api.trx.dto.TransactionReportResponseDTO;
import com.clip.api.trx.dto.TransactionRequestDTO;
import com.clip.api.trx.dto.TransactionResponseDTO;
import com.clip.api.trx.exception.TransactionException;
import com.clip.api.trx.model.TransactionModel;
import com.clip.api.trx.service.TransactionService;
import com.clip.api.util.TransactionMapperUtil;
import com.clip.api.util.TransactionReportUtil;

@Service
public class TransactionServiceImp implements TransactionService {

	@Autowired
	TransactionDao trxDao;			

	@Override
	public TransactionResponseDTO addTransaction(TransactionRequestDTO txnRequest, Integer userID) {
		TransactionModel model = TransactionMapperUtil.requestToModel(txnRequest, userID);				
		trxDao.addTransaction(model);			
		return TransactionMapperUtil.modelToResponse(model);			
	}

	@Override
	public TransactionResponseDTO getTransaction(Integer userID, UUID transactionID) {		
		TransactionModel model = trxDao.findTransaction(userID, transactionID);
		if (null == model) {
			throw new TransactionException(ErrorType.TRANSACTION_NOT_FOUND);
		}
		return TransactionMapperUtil.modelToResponse(model);									
	}

	@Override
	public List<TransactionResponseDTO> getListTransaction(Integer userID) {				
		List<TransactionModel> lstTrx = trxDao.findTransactionsByUserID(userID);			
		return TransactionMapperUtil.modelToResponse(lstTrx);					
	}

	@Override
	public TransactionResponseDTO sumTransaction(Integer userID) {		
		List<TransactionModel> lstTrx = trxDao.findTransactionsByUserID(userID);			
		Double sum = lstTrx.stream().mapToDouble(TransactionModel::getAmount).sum();
		return TransactionMapperUtil.sumToResponse(userID, sum);				
	}

	@Override
	public List<TransactionReportResponseDTO> reportTransaction(Integer userID) {		
		List<TransactionModel> lstTrx = trxDao.findTransactionsByUserID(userID);
		return TransactionReportUtil.createTransactionReport(lstTrx);					
	}

	@Override
	public TransactionResponseDTO randomTransaction() {		
		List<TransactionModel> lstTrx = trxDao.getAllTransactions();
		if(lstTrx.isEmpty()) {
			throw new TransactionException(ErrorType.TRANSACTION_NOT_FOUND);
		}
		Random r = new Random();			
		return TransactionMapperUtil.modelToResponse(lstTrx.get(r.nextInt(lstTrx.size())));		
	}

}
