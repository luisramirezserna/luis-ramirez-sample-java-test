package com.clip.api.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.clip.api.properties.Properties;
import com.clip.api.trx.dao.TransactionDao;
import com.clip.api.trx.dao.imp.TransactionDaoImp;
import com.clip.api.trx.model.TransactionModel;
import com.google.gson.Gson;

@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class TransactionDaoTest {

	@Mock	
	Properties properties;
	
	@InjectMocks
	private TransactionDao trxDao = new TransactionDaoImp();
	
	private static String rndTestFileName = "DataFileTest_" + new Random().nextInt(10000);
	private static String emptyFileName = "EmptyDataFileTest_" + new Random().nextInt(10000);
	private static Integer userID = new Random().nextInt(10000);
	private static UUID trxID = UUID.randomUUID();
	private static final Gson GSON = new Gson();
	
	private List<TransactionModel> getLstTrxModel() {
		String json = "[{\"transactionID\":\"dd2e69c7-94a3-40eb-b626-0f4d6d7f69d8\",\"userID\":"+userID+",\"amount\":5.0,\"description\":\"luis\",\"date\":\"2019-11-10\"},"
				+ "{\"transactionID\":\"6cb3e729-c489-453d-a008-58725e9b94c2\",\"userID\":"+userID+",\"amount\":5.0,\"description\":\"luis\",\"date\":\"2019-11-11\"},"
				+ "{\"transactionID\":\"e8eb1ecc-fbae-40a5-880a-9c2b6f2bc719\",\"userID\":"+userID+",\"amount\":20.0,\"description\":\"luis\",\"date\":\"2019-11-21\"},"
				+ "{\"transactionID\":\"6eda7145-6ebd-4e32-a27b-41e1fc96104b\",\"userID\":"+userID+",\"amount\":25.0,\"description\":\"luis\",\"date\":\"2019-11-23\"},"
				+ "{\"transactionID\":\"05698864-467c-4f8b-9407-a1735d04baf0\",\"userID\":"+userID+",\"amount\":25.0,\"description\":\"luis\",\"date\":\"2019-11-24\"},"
				+ "{\"transactionID\":\"842a884e-8429-45a0-b05c-9b4978f9eb60\",\"userID\":"+userID+",\"amount\":25.0,\"description\":\"luis\",\"date\":\"2019-11-25\"},"
				+ "{\"transactionID\":\"604791eb-683f-4188-b140-3e7792605814\",\"userID\":"+userID+",\"amount\":25.0,\"description\":\"luis\",\"date\":\"2019-11-26\"},"
				+ "{\"transactionID\":\"e2add70e-6929-4841-97cb-7b8b53916361\",\"userID\":"+userID+",\"amount\":30.0,\"description\":\"luis\",\"date\":\"2019-11-29\"},"
				+ "{\"transactionID\":\"792d3e71-1c85-42d0-b329-9442acd4c383\",\"userID\":"+userID+",\"amount\":10.0,\"description\":\"luis\",\"date\":\"2019-11-29\"},"
				+ "{\"transactionID\":\"6740bca8-45f7-409d-a80f-08bf6b9c3037\",\"userID\":"+userID+",\"amount\":10.0,\"description\":\"luis\",\"date\":\"2019-11-30\"},"
				+ "{\"transactionID\":\"fd046b52-3837-4bde-ac1e-53ce7f89cda3\",\"userID\":"+userID+",\"amount\":20.0,\"description\":\"luis\",\"date\":\"2019-12-01\"},"
				+ "{\"transactionID\":\"72254174-8ea0-4b8c-9ba6-1915c5438acc\",\"userID\":"+userID+",\"amount\":20.0,\"description\":\"luis\",\"date\":\"2019-12-02\"},"
				+ "{\"transactionID\":\"0c008e90-f44b-4437-a681-df7f3970b6b5\",\"userID\":"+userID+",\"amount\":20.0,\"description\":\"luis\",\"date\":\"2019-12-03\"},"
				+ "{\"transactionID\":\"2af8dc91-cb47-47f3-8de6-e74cea4d0e0e\",\"userID\":"+userID+",\"amount\":20.0,\"description\":\"luis\",\"date\":\"2019-12-04\"},"
				+ "{\"transactionID\":\"6c5fa736-bca6-4822-8954-1db7f110d465\",\"userID\":"+userID+",\"amount\":20.0,\"description\":\"luis\",\"date\":\"2019-12-05\"},"
				+ "{\"transactionID\":\"4dc7d23b-dc24-413e-81d7-34445fddd462\",\"userID\":"+userID+",\"amount\":20.0,\"description\":\"luis\",\"date\":\"2019-12-05\"},"
				+ "{\"transactionID\":\"41395ca5-8173-4f49-9b61-ce59496efadc\",\"userID\":"+userID+",\"amount\":50.0,\"description\":\"luis\",\"date\":\"2019-12-06\"},"
				+ "{\"transactionID\":\"82648c22-2553-4216-8635-e29d239f0043\",\"userID\":"+userID+",\"amount\":25.0,\"description\":\"luis\",\"date\":\"2019-12-09\"},"
				+ "{\"transactionID\":\""+trxID+"\",\"userID\":"+userID+",\"amount\":25.0,\"description\":\"luis\",\"date\":\"2019-12-12\"}]";		
	
		return Arrays.asList(GSON.fromJson(json, TransactionModel[].class));
	}
	
	@BeforeEach
	void mockFileDataName () {
		when(properties.getFilename()).thenReturn(rndTestFileName);
	}
	
	@AfterAll
	static void deleteTestFile () {
		File dataFile = new File(rndTestFileName);
		dataFile.delete();
		File emptyFile = new File(emptyFileName);
		emptyFile.delete();
	}
	
	@Test
	@Order(1)
	void addTransactionTest() {		
		List<TransactionModel> getLstTrxMdl = getLstTrxModel();
		getLstTrxMdl.stream().forEach(trxMdl -> {
			assertTrue(trxDao.addTransaction(trxMdl));
		});
		
	}
	
	@Test
	void findTransactionTest() {
		TransactionModel trxMdl = trxDao.findTransaction(userID, trxID);
		assertEquals(userID, trxMdl.getUserID());
		assertEquals(trxID, trxMdl.getTransactionID());
	}
	
	@Test
	void transactionNotFoundTest() {		
		assertNull(trxDao.findTransaction(userID, UUID.randomUUID()));
	}
	
	@Test
	void findTransactionsByUserIDTest() {
		List<TransactionModel> lstTrxMdl = trxDao.findTransactionsByUserID(userID);
		assertTrue(lstTrxMdl.stream().allMatch(trxMdl -> trxMdl.getUserID().equals(userID)));
	}
	
	void transactionsByUserIDNotFoundTest() {		
		assertTrue(trxDao.findTransactionsByUserID(new Random().nextInt(10000)).isEmpty());
	}
	
	@Test
	void getAllTransactions () {
		List<TransactionModel> expectedLstTrxModel = getLstTrxModel();
		List<TransactionModel> lstTrxMdl = trxDao.getAllTransactions();
		
		assertLinesMatch(expectedLstTrxModel.stream().map(trxMdl -> trxMdl.getAmount().toString()).collect(Collectors.toList()), 
				lstTrxMdl.stream().map(trxMdl -> trxMdl.getAmount().toString()).collect(Collectors.toList()));
		
		assertLinesMatch(expectedLstTrxModel.stream().map(trxMdl -> trxMdl.getTransactionID().toString()).collect(Collectors.toList()), 
				lstTrxMdl.stream().map(trxMdl -> trxMdl.getTransactionID().toString()).collect(Collectors.toList()));
		
		assertLinesMatch(expectedLstTrxModel.stream().map(trxMdl -> trxMdl.getUserID().toString()).collect(Collectors.toList()), 
				lstTrxMdl.stream().map(trxMdl -> trxMdl.getUserID().toString()).collect(Collectors.toList()));
		
		assertLinesMatch(expectedLstTrxModel.stream().map(TransactionModel::getDate).collect(Collectors.toList()), 
				lstTrxMdl.stream().map(TransactionModel::getDate).collect(Collectors.toList()));
	}
	
	@Test
	void transactionsNotFound () throws IOException {		
		File emptyFile = new File(emptyFileName);
		emptyFile.createNewFile();
		when(properties.getFilename()).thenReturn(emptyFileName);
		assertTrue(trxDao.getAllTransactions().isEmpty());		
	}
		

}
