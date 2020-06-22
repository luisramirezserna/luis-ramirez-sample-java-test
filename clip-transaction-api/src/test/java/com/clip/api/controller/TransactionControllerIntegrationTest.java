package com.clip.api.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.clip.api.auth.dto.AuthenticationRequestDTO;
import com.clip.api.auth.dto.AuthenticationResponseDTO;
import com.clip.api.properties.ErrorMessageProperties;
import com.clip.api.trx.dto.ErrorType;
import com.clip.api.trx.dto.TransactionErrorDTO;
import com.clip.api.trx.dto.TransactionReportResponseDTO;
import com.clip.api.trx.dto.TransactionRequestDTO;
import com.clip.api.trx.dto.TransactionResponseDTO;
import com.google.gson.Gson;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TransactionControllerIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;	
	
	@Autowired
	ErrorMessageProperties errorMessageProperties;

	private static UUID trxID;
	
	private static String authorizationToken;
	
	private static Integer userID = new Random().nextInt(10000);
	
	private static final Gson GSON = new Gson();	
	
	private TransactionRequestDTO getInvalidTrxReqDTO() {
		TransactionRequestDTO dto = new TransactionRequestDTO();
		dto.setDescription("test request");
		dto.setDate("2019-14-40");		
		return dto;
	}
	
	private AuthenticationRequestDTO getAuthRequestDTO () {
		AuthenticationRequestDTO user = new AuthenticationRequestDTO();
		user.setName("clipuser");
		user.setPassword("clip@JWT02");
		return user;
	}
	
	private AuthenticationRequestDTO getInvalidAuthRequestDTO () {
		AuthenticationRequestDTO user = new AuthenticationRequestDTO();
		user.setName("clip");
		user.setPassword("clip@JW");
		return user;
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
	
	private List<TransactionReportResponseDTO> getExpectedReportResponse() {
		String json = "[{\"amount\":10.0,\"weekStartDate\":\"2019-11-08\",\"weekFinishDate\":\"2019-11-14\",\"quantity\":2,\"totalAmount\":0.0},"
				+ "{\"amount\":20.0,\"weekStartDate\":\"2019-11-15\",\"weekFinishDate\":\"2019-11-21\",\"quantity\":1,\"totalAmount\":10.0},"
				+ "{\"amount\":100.0,\"weekStartDate\":\"2019-11-22\",\"weekFinishDate\":\"2019-11-28\",\"quantity\":4,\"totalAmount\":30.0},"
				+ "{\"amount\":50.0,\"weekStartDate\":\"2019-11-29\",\"weekFinishDate\":\"2019-11-30\",\"quantity\":3,\"totalAmount\":130.0},"
				+ "{\"amount\":120.0,\"weekStartDate\":\"2019-12-01\",\"weekFinishDate\":\"2019-12-05\",\"quantity\":6,\"totalAmount\":180.0},"
				+ "{\"amount\":100.0,\"weekStartDate\":\"2019-12-06\",\"weekFinishDate\":\"2019-12-12\",\"quantity\":3,\"totalAmount\":300.0}]";
		return Arrays.asList(GSON.fromJson(json, TransactionReportResponseDTO[].class));
	}

	@Test
	@Order(1)
	void authenticateTest() {
		
		HttpEntity<AuthenticationRequestDTO> requestEntity = new HttpEntity<>(getAuthRequestDTO(), null);
		ResponseEntity<AuthenticationResponseDTO> entity = this.restTemplate.
				exchange("http://localhost:" + port + "/clip/authenticate", HttpMethod.POST, requestEntity, AuthenticationResponseDTO.class);
		
		authorizationToken = entity.getBody().getAccessToken();
		assertNotNull(entity.getBody().getAccessToken());				
		assertTrue(entity.getBody().getAccessToken().contains("Bearer"));
	}
	
	@Test	
	void invalidAuthenticateTest() {
		
		HttpEntity<AuthenticationRequestDTO> requestEntity = new HttpEntity<>(getInvalidAuthRequestDTO(), null);
		TransactionErrorDTO trxErrDTO = this.restTemplate.
				exchange("http://localhost:" + port + "/clip/authenticate", HttpMethod.POST, requestEntity, TransactionErrorDTO.class).getBody();
				
		assertEquals(trxErrDTO.getErrorType(), ErrorType.AUTH_INVALID_USER_NAME);
		assertLinesMatch(Arrays.asList(errorMessageProperties.getInvalidUserName()), trxErrDTO.getErrorMessage());
	}
	
	@Test
	void invalidAuthenticateTokenTest() {	
		HttpEntity requestEntity = new HttpEntity(getInvalidTokenRequestHeaders());
		TransactionErrorDTO trxErrDTO = this.restTemplate.
				exchange("http://localhost:" + port + "/clip/transaction/get/", 
						HttpMethod.GET, requestEntity, TransactionErrorDTO.class).getBody();
		
		assertEquals(trxErrDTO.getErrorType(), ErrorType.AUTH_INVALID_TOKEN);
		assertLinesMatch(Arrays.asList(errorMessageProperties.getInvalidToken()), trxErrDTO.getErrorMessage());
	}
	
	@Test
	@Order(2)
	void addTransactionTest() {
		List<TransactionRequestDTO> lstTrxReqDTO = getLstTrxReqDTO();	
		lstTrxReqDTO.stream().forEach(trxReqDTO -> {
			HttpEntity<TransactionRequestDTO> requestEntity = new HttpEntity<>(trxReqDTO, getRequestHeaders());
	        ResponseEntity<TransactionResponseDTO> trxRespDTO = this.restTemplate.
					exchange("http://localhost:" + port + "/clip/transaction/add/"+userID, HttpMethod.POST, requestEntity, TransactionResponseDTO.class);
			trxID = trxRespDTO.getBody().getTransactionID();
			assertEquals(trxReqDTO.getAmount(), trxRespDTO.getBody().getAmount());
			assertEquals(trxReqDTO.getDate(), trxRespDTO.getBody().getDate());
			assertEquals(trxReqDTO.getDescription(), trxRespDTO.getBody().getDescription());
			assertEquals(userID, trxRespDTO.getBody().getUserID());
			assertNotNull(trxRespDTO.getBody().getTransactionID());
		});        
	}

	@Test
	void getTransactionTest() {				
		List<TransactionRequestDTO> lstTrxReqAdded =  getLstTrxReqDTO();
		TransactionRequestDTO trxReqAdded =  lstTrxReqAdded.get(lstTrxReqAdded.size()-1);
		HttpEntity requestEntity = new HttpEntity(getRequestHeaders());
		TransactionResponseDTO trxRespDTO = this.restTemplate.
				exchange("http://localhost:" + port + "/clip/transaction/get/"+userID+"/" + trxID, 
						HttpMethod.GET, requestEntity, TransactionResponseDTO.class).getBody();
		assertEquals(trxReqAdded.getAmount(), trxRespDTO.getAmount());
		assertEquals(trxReqAdded.getDate(), trxRespDTO.getDate());
		assertEquals(trxReqAdded.getDescription(), trxRespDTO.getDescription());
		assertEquals(userID, trxRespDTO.getUserID());
		assertEquals(trxID, trxRespDTO.getTransactionID());
	}
	
	@Test
	void getTransactionInvalidUserIDTest() {						
		HttpEntity requestEntity = new HttpEntity(getRequestHeaders());
		TransactionErrorDTO trxErrDTO = this.restTemplate.
				exchange("http://localhost:" + port + "/clip/transaction/get/abcde/" + trxID, 
						HttpMethod.GET, requestEntity, TransactionErrorDTO.class).getBody();
		
		assertEquals(trxErrDTO.getErrorType(), ErrorType.INVALID_PARAM_VALUE);
		assertLinesMatch(Arrays.asList(errorMessageProperties.getInvalidUseridParam()), trxErrDTO.getErrorMessage());
	}
	
	@Test
	void getTransactionInvalidTransactionIDTest() {						
		HttpEntity requestEntity = new HttpEntity(getRequestHeaders());
		TransactionErrorDTO trxErrDTO = this.restTemplate.
				exchange("http://localhost:" + port + "/clip/transaction/get/"+userID+"/abcde", 
						HttpMethod.GET, requestEntity, TransactionErrorDTO.class).getBody();
		
		assertEquals(trxErrDTO.getErrorType(), ErrorType.INVALID_PARAM_VALUE);
		assertLinesMatch(Arrays.asList(errorMessageProperties.getInvalidTrxidParam()), trxErrDTO.getErrorMessage());
	}

	@Test
	void getListTransactionTest() {
		List<TransactionRequestDTO> lstTrxReqAdded =  getLstTrxReqDTO();		
		HttpEntity requestEntity = new HttpEntity(getRequestHeaders());
		List<TransactionResponseDTO> lstTrxResp = Arrays.asList(this.restTemplate.
				exchange("http://localhost:" + port + "/clip/transaction/get/"+userID, 
						HttpMethod.GET, requestEntity, TransactionResponseDTO[].class).getBody());
		
		
		
		assertLinesMatch(lstTrxReqAdded.stream().map(x -> x.getAmount().toString()).collect(Collectors.toList()), 
				lstTrxResp.stream().map(x -> x.getAmount().toString()).collect(Collectors.toList()));
		
		assertLinesMatch(lstTrxReqAdded.stream().map(TransactionRequestDTO::getDate).collect(Collectors.toList()), 
				lstTrxResp.stream().map(TransactionResponseDTO::getDate).collect(Collectors.toList()));
		
		assertLinesMatch(lstTrxReqAdded.stream().map(TransactionRequestDTO::getDescription).collect(Collectors.toList()), 
				lstTrxResp.stream().map(TransactionResponseDTO::getDescription).collect(Collectors.toList()));

	}
	
	@Test
	void getListTransactionNotFoundTest() {		
		HttpEntity requestEntity = new HttpEntity(getRequestHeaders());
		List<TransactionResponseDTO> lstTrxResp = Arrays.asList(this.restTemplate.
				exchange("http://localhost:" + port + "/clip/transaction/get/1111111", 
						HttpMethod.GET, requestEntity, TransactionResponseDTO[].class).getBody());		
		assertTrue(lstTrxResp.isEmpty());


	}

	@Test
	void sumTransactionTest() {		
		HttpEntity requestEntity = new HttpEntity(getRequestHeaders());		
		TransactionResponseDTO trxRespDTO = this.restTemplate.
				exchange("http://localhost:" + port + "/clip/transaction/get/sum/"+userID, 
						HttpMethod.GET, requestEntity, TransactionResponseDTO.class).getBody();		
		assertEquals(getLstTrxReqDTO().stream().mapToDouble(TransactionRequestDTO::getAmount).sum(), trxRespDTO.getSum());
	}

	@Test
	void reportTransactionTest() {
		List<TransactionReportResponseDTO> expLstRptResp = getExpectedReportResponse();
		HttpEntity requestEntity = new HttpEntity(getRequestHeaders());
		List<TransactionReportResponseDTO> lstRptRespDTO = Arrays.asList(this.restTemplate.
				exchange("http://localhost:" + port + "/clip/transaction/get/report/"+userID, 
						HttpMethod.GET, requestEntity, TransactionReportResponseDTO[].class).getBody());
		
		assertLinesMatch(lstRptRespDTO.stream().map(TransactionReportResponseDTO::getWeekStartDate).collect(Collectors.toList()), 
				expLstRptResp.stream().map(TransactionReportResponseDTO::getWeekStartDate).collect(Collectors.toList()));
		
		assertLinesMatch(lstRptRespDTO.stream().map(TransactionReportResponseDTO::getWeekFinishDate).collect(Collectors.toList()), 
				expLstRptResp.stream().map(TransactionReportResponseDTO::getWeekFinishDate).collect(Collectors.toList()));
		
		assertLinesMatch(lstRptRespDTO.stream().map(x -> x.getQuantity().toString()).collect(Collectors.toList()), 
				expLstRptResp.stream().map(x -> x.getQuantity().toString()).collect(Collectors.toList()));
		
		assertLinesMatch(lstRptRespDTO.stream().map(x -> x.getTotalAmount().toString()).collect(Collectors.toList()), 
				expLstRptResp.stream().map(x -> x.getTotalAmount().toString()).collect(Collectors.toList()));
	}

	@Test
	void randomTransactionTest() {	
		HttpEntity requestEntity = new HttpEntity(getRequestHeaders());
		TransactionResponseDTO trxRespDTO = this.restTemplate.
				exchange("http://localhost:" + port + "/clip/transaction/get/", 
						HttpMethod.GET, requestEntity, TransactionResponseDTO.class).getBody();		
		assertNotNull(trxRespDTO.getAmount());
		assertNotNull(trxRespDTO.getDate());
		assertNotNull(trxRespDTO.getDescription());
		assertNotNull(trxRespDTO.getUserID());
		assertNotNull(trxRespDTO.getTransactionID());
	}
	
	@Test	
	void addInvalidRequestTransactionTest() {		
		TransactionRequestDTO trxReqDTO = getInvalidTrxReqDTO();
		HttpEntity<TransactionRequestDTO> requestEntity = new HttpEntity<TransactionRequestDTO>(trxReqDTO, getRequestHeaders());
		TransactionErrorDTO trxErrDTO = this.restTemplate.
				exchange("http://localhost:" + port + "/clip/transaction/add/"+userID, 
						HttpMethod.POST, requestEntity, TransactionErrorDTO.class).getBody();		
		assertEquals(trxErrDTO.getErrorType(), ErrorType.REQUEST_VALIDATION);
		assertLinesMatch(Arrays.asList(errorMessageProperties.getEmptyAmount(),errorMessageProperties.getInvalidDate()), trxErrDTO.getErrorMessage());
	}
	
	@Test	
	void transactionNotFoundTest() {
		HttpEntity requestEntity = new HttpEntity(getRequestHeaders());
		TransactionErrorDTO trxErrDTO = this.restTemplate.
				exchange("http://localhost:" + port + "/clip/transaction/get/111111111/" + UUID.randomUUID().toString(), 
						HttpMethod.GET, requestEntity, TransactionErrorDTO.class).getBody();		
		assertEquals(trxErrDTO.getErrorType(), ErrorType.TRANSACTION_NOT_FOUND);
		assertEquals(Arrays.asList(errorMessageProperties.getTransactionNotFound()), trxErrDTO.getErrorMessage());
	}
	
	private HttpHeaders getRequestHeaders () {
		HttpHeaders requestHeaders = new HttpHeaders();
	    requestHeaders.setContentType(MediaType.APPLICATION_JSON);
	    requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    requestHeaders.add("Authorization", authorizationToken);
	    return requestHeaders;
	}
	
	private HttpHeaders getInvalidTokenRequestHeaders () {
		HttpHeaders requestHeaders = new HttpHeaders();
	    requestHeaders.setContentType(MediaType.APPLICATION_JSON);
	    requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    requestHeaders.add("Authorization", "invalidToken");
	    return requestHeaders;
	}

}
