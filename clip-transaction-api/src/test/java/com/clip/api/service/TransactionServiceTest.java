package com.clip.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.clip.api.trx.dao.TransactionDao;
import com.clip.api.trx.dto.ErrorType;
import com.clip.api.trx.dto.TransactionReportResponseDTO;
import com.clip.api.trx.dto.TransactionRequestDTO;
import com.clip.api.trx.dto.TransactionResponseDTO;
import com.clip.api.trx.exception.TransactionException;
import com.clip.api.trx.model.TransactionModel;
import com.clip.api.trx.service.TransactionService;
import com.clip.api.trx.service.imp.TransactionServiceImp;
import com.clip.api.util.TransactionMapperUtil;
import com.google.gson.Gson;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	private static final Gson GSON = new Gson();
	
	@Mock
	private TransactionDao trxDao;
	
	@InjectMocks
	private TransactionService trxService = new TransactionServiceImp();
	
	private static Integer userID = new Random().nextInt(10000);
	
	private TransactionRequestDTO getTrxReqDTO() {
		TransactionRequestDTO dto = new TransactionRequestDTO();
		dto.setAmount(5.0);
		dto.setDescription("test request");
		dto.setDate("2019-11-10");		
		return dto;
	}
	
	private List<TransactionRequestDTO> getLstTrxReqDTO() {
		String json = "[{\"amount\":5.0,\"description\":\"test\",\"date\":\"2019-11-10\"},"
				+ "{\"amount\":5.0,\"description\":\"test\",\"date\":\"2019-11-11\"},"
				+ "{\"amount\":20.0,\"description\":\"test\",\"date\":\"2019-11-21\"},"
				+ "{\"amount\":25.0,\"description\":\"test\",\"date\":\"2019-11-23\"},"
				+ "{\"amount\":25.0,\"description\":\"test\",\"date\":\"2019-11-24\"},"
				+ "{\"amount\":25.0,\"description\":\"test\",\"date\":\"2019-11-25\"},"
				+ "{\"amount\":25.0,\"description\":\"test\",\"date\":\"2019-11-26\"},"
				+ "{\"amount\":30.0,\"description\":\"test\",\"date\":\"2019-11-29\"},"
				+ "{\"amount\":10.0,\"description\":\"test\",\"date\":\"2019-11-29\"},"
				+ "{\"amount\":10.0,\"description\":\"test\",\"date\":\"2019-11-30\"},"
				+ "{\"amount\":20.0,\"description\":\"test\",\"date\":\"2019-12-01\"},"
				+ "{\"amount\":20.0,\"description\":\"test\",\"date\":\"2019-12-02\"},"
				+ "{\"amount\":20.0,\"description\":\"test\",\"date\":\"2019-12-03\"},"
				+ "{\"amount\":20.0,\"description\":\"test\",\"date\":\"2019-12-04\"},"
				+ "{\"amount\":20.0,\"description\":\"test\",\"date\":\"2019-12-05\"},"
				+ "{\"amount\":20.0,\"description\":\"test\",\"date\":\"2019-12-05\"},"
				+ "{\"amount\":50.0,\"description\":\"test\",\"date\":\"2019-12-06\"},"
				+ "{\"amount\":25.0,\"description\":\"test\",\"date\":\"2019-12-09\"},"
				+ "{\"amount\":25.0,\"description\":\"test\",\"date\":\"2019-12-12\"}]";		
	
		return Arrays.asList(GSON.fromJson(json, TransactionRequestDTO[].class));
	}
	
	private List<TransactionModel> getLstTrxModel () {
		return getLstTrxReqDTO().stream().map( trxReq -> {						
			return TransactionMapperUtil.requestToModel(trxReq, userID); 
		}).collect(Collectors.toList());
	}
	
	@Test
	void addTransactionTest() {				
		TransactionRequestDTO trxReqDTO = getTrxReqDTO();		
		when(trxDao.addTransaction(any(TransactionModel.class))).thenReturn(Boolean.TRUE);		
		TransactionResponseDTO trxRespDTO = trxService.addTransaction(trxReqDTO, userID);
		assertEquals(trxReqDTO.getAmount(), trxRespDTO.getAmount());
		assertEquals(trxReqDTO.getDate(), trxRespDTO.getDate());
		assertEquals(trxReqDTO.getDescription(), trxRespDTO.getDescription());
		assertEquals(userID, trxRespDTO.getUserID());
		assertNotNull(trxRespDTO.getTransactionID());		
	}		
	
	@Test
	void getTransactionTest() {
		TransactionModel trxModel = TransactionMapperUtil.requestToModel(getTrxReqDTO(), userID);
		UUID trxID = trxModel.getTransactionID();
		when(trxDao.findTransaction(userID, trxID)).thenReturn(trxModel);
		TransactionResponseDTO trxRespDTO = trxService.getTransaction(userID, trxID);
		assertEquals(trxModel.getAmount(), trxRespDTO.getAmount());
		assertEquals(trxModel.getDate(), trxRespDTO.getDate());
		assertEquals(trxModel.getDescription(), trxRespDTO.getDescription());
		assertEquals(userID, trxRespDTO.getUserID());
		assertEquals(trxID, trxRespDTO.getTransactionID());
	}

	@Test
	void getListTransactionTest() {
		List<TransactionRequestDTO> lstTrxRerq = getLstTrxReqDTO();
		List<TransactionModel> lstTrxModel = getLstTrxModel();
		when(trxDao.findTransactionsByUserID(userID)).thenReturn(lstTrxModel);		
		List<TransactionResponseDTO> lstTrxResp = trxService.getListTransaction(userID);
		lstTrxRerq.stream().forEach(trxReq -> {
			assertTrue(lstTrxResp.stream().anyMatch(trxResp -> 
				trxResp.getAmount().equals(trxReq.getAmount()) && 
						trxResp.getDate().equals(trxReq.getDate()) &&
							trxResp.getDescription().equals(trxReq.getDescription())
			));
		});
		
	}
	
	@Test
	void sumTransactionTest() {
		List<TransactionRequestDTO> lstTrxRerq = getLstTrxReqDTO();
		List<TransactionModel> lstTrxModel = getLstTrxModel();
		when(trxDao.findTransactionsByUserID(userID)).thenReturn(lstTrxModel);
		TransactionResponseDTO trxRespDTO = trxService.sumTransaction(userID);
		Double reqSum = lstTrxRerq.stream().mapToDouble(trxReq -> trxReq.getAmount()).sum();
		assertEquals(reqSum, trxRespDTO.getSum());
	}
	
	@Test
	void reportTransactionTest() {
		String startDate = "2019-11-08";
		String endDate = "2019-11-14";
		Double amount = 10.0;
		List<TransactionModel> lstTrxModel = getLstTrxModel();
		when(trxDao.findTransactionsByUserID(userID)).thenReturn(lstTrxModel);
		List<TransactionReportResponseDTO> lstRptRespDTO = trxService.reportTransaction(userID);		
		assertTrue(lstRptRespDTO.stream().anyMatch(trxRpt ->
			trxRpt.getWeekStartDate().equals(startDate) && 
				trxRpt.getWeekFinishDate().equals(endDate) && trxRpt.getAmount().equals(amount))
		);
	}
	
	@Test
	void randomTransactionTest() {
		List<TransactionModel> lstTrxModel = getLstTrxModel();
		when(trxDao.getAllTransactions()).thenReturn(lstTrxModel);
		TransactionResponseDTO trxRespDTO = trxService.randomTransaction();		
		assertTrue(lstTrxModel.stream().anyMatch(trxModel -> 
			trxRespDTO.getAmount().equals(trxModel.getAmount()) &&
				trxRespDTO.getDate().equals(trxModel.getDate()) && 
					trxRespDTO.getTransactionID().equals(trxModel.getTransactionID()))
		);
	}
	
	@Test
	void transactionServiceExceptionThrownTest() {
		Exception exception = null;
		TransactionRequestDTO trxReqDTO = getTrxReqDTO();
		String fileErrorMessage = ErrorType.FILE_ERROR.toString();		
		String actualMessage;
		
		when(trxDao.addTransaction(any(TransactionModel.class))).thenThrow(new TransactionException(ErrorType.FILE_ERROR, ErrorType.FILE_ERROR.toString()));		
		
		exception = assertThrows(RuntimeException.class, () -> {
			trxService.addTransaction(trxReqDTO, 11111);
	    });			    
	    actualMessage = exception.getMessage();	 
	    assertEquals(fileErrorMessage, actualMessage);	    
	}
	
}
