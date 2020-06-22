package com.clip.api.trx.dao;

import java.util.List;
import java.util.UUID;

import com.clip.api.trx.model.TransactionModel;

public interface TransactionDao {

	TransactionModel findTransaction(Integer userID, UUID transactionID);
	Boolean addTransaction(TransactionModel transaction);
	List<TransactionModel> findTransactionsByUserID(Integer userID);
	List<TransactionModel> getAllTransactions();
	
}
