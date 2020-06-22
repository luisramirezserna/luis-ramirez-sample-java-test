package com.clip.api.trx.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.clip.api.trx.dto.TransactionReportResponseDTO;
import com.clip.api.trx.dto.TransactionRequestDTO;
import com.clip.api.trx.dto.TransactionResponseDTO;
import com.clip.api.trx.service.TransactionService;
import com.clip.api.trx.validator.TransactionRequestValidator;

@RestController
@RequestMapping("/clip/transaction")
public class TransactionController {		

	private static final String APP_JSON_TYPE =  "application/json";
	
	@Autowired
	private TransactionService trxService;			
	
	@Autowired
	TransactionRequestValidator trxReqValidator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(trxReqValidator);
	}

	@RequestMapping(value = "/add/{userID}", method = RequestMethod.POST, consumes = APP_JSON_TYPE, produces = APP_JSON_TYPE)
	@ResponseBody
	public ResponseEntity<TransactionResponseDTO> addTransaction(@Valid @RequestBody final TransactionRequestDTO trxReq ,@PathVariable final Integer userID) {		
		return ResponseEntity.ok(trxService.addTransaction(trxReq, userID));				
	}

	@RequestMapping(value = "/get/{userID}/{transactionID}", method = RequestMethod.GET, produces = APP_JSON_TYPE)
	@ResponseBody
	public ResponseEntity<TransactionResponseDTO> getTransaction(@PathVariable final Integer userID, @PathVariable final UUID transactionID) {
		return ResponseEntity.ok(trxService.getTransaction(userID, transactionID));		
	}

	@RequestMapping(value = "/get/{userID}", method = RequestMethod.GET, produces = APP_JSON_TYPE)
	@ResponseBody
	public ResponseEntity<List<TransactionResponseDTO>> getListTransaction(@PathVariable final Integer userID) {
		return ResponseEntity.ok(trxService.getListTransaction(userID));

	}

	@RequestMapping(value = "/get/sum/{userID}", method = RequestMethod.GET, produces = APP_JSON_TYPE)
	@ResponseBody
	public ResponseEntity<TransactionResponseDTO> sumTransaction(@PathVariable final Integer userID) {
		return ResponseEntity.ok(trxService.sumTransaction(userID));
	}	

	@RequestMapping(value = "/get/report/{userID}", method = RequestMethod.GET, produces = APP_JSON_TYPE)
	@ResponseBody
	public ResponseEntity<List<TransactionReportResponseDTO>> reportTransaction(@PathVariable final Integer userID) {
		return ResponseEntity.ok(trxService.reportTransaction(userID));
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET, produces = APP_JSON_TYPE)
	@ResponseBody
	public ResponseEntity<TransactionResponseDTO> randomTransaction() {
		return ResponseEntity.ok(trxService.randomTransaction());
	}

}
