package com.clip.api.trx.dao.imp;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.clip.api.properties.Properties;
import com.clip.api.trx.dao.TransactionDao;
import com.clip.api.trx.dto.ErrorType;
import com.clip.api.trx.exception.TransactionException;
import com.clip.api.trx.model.TransactionModel;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

@Component
public class TransactionDaoImp implements TransactionDao {			
			
	private static final Gson GSON = new Gson();	
	
	@Autowired
	Properties properties;
	
	@Override
	public TransactionModel findTransaction(Integer userID, UUID transactionID){						
		try (JsonReader jsReader = getReader();) {									
			while(JsonToken.BEGIN_OBJECT.equals(jsReader.peek())) {				
				TransactionModel trx = GSON.fromJson(jsReader, TransactionModel.class);				
				if (trx.getUserID().equals(userID) && trx.getTransactionID().equals(transactionID)) {
					return trx;
				}
			}	
		} catch (EOFException e) {
			return null; 
		} catch (IOException e) {					
			throw new TransactionException(ErrorType.FILE_ERROR, e.getMessage(), e);
		}
		return null;
	}

	@Override
	public Boolean addTransaction(TransactionModel transaction) {		
		try (BufferedWriter bufferWriter = getWriter();) {			
			bufferWriter.write(GSON.toJson(transaction));
			return true;
		} catch (IOException e) {					
			throw new TransactionException(ErrorType.FILE_ERROR, e.getMessage(), e);
		}		
		
	}
		
	@Override
	public List<TransactionModel> findTransactionsByUserID(Integer userID) {		
		List<TransactionModel> lstTrx = new ArrayList<TransactionModel>();
		try (JsonReader jsReader = getReader();) {						
			jsReader.setLenient(true);
			lstTrx = new ArrayList<TransactionModel>();
			while(JsonToken.BEGIN_OBJECT.equals(jsReader.peek())) {				
				TransactionModel trx = GSON.fromJson(jsReader, TransactionModel.class);				
				if (trx.getUserID().equals(userID)) {
					lstTrx.add(trx);
				}
			}	
		} catch (EOFException e) {
			return lstTrx; 
		} catch (IOException e) {					
			throw new TransactionException(ErrorType.FILE_ERROR, e.getMessage(), e);
		}
		return lstTrx;
	}		

	@Override
	public List<TransactionModel> getAllTransactions() {		
		List<TransactionModel> lstTrx = new ArrayList<TransactionModel>();
		try (JsonReader jsReader = getReader()) {						
			jsReader.setLenient(true);
			lstTrx = new ArrayList<TransactionModel>();
			while(JsonToken.BEGIN_OBJECT.equals(jsReader.peek())) {				
				TransactionModel trx = GSON.fromJson(jsReader, TransactionModel.class);								
				lstTrx.add(trx);				
			}
			return lstTrx;
		} catch (EOFException e) {
			return lstTrx; 
		} catch (IOException e) {					
			throw new TransactionException(ErrorType.FILE_ERROR, e.getMessage(), e);
		}		
	}
	
	private File getFile(boolean createFile){
		File dataFile = new File(properties.getFilename());
		if(!dataFile.exists()) {
			if(createFile) {
				try {
					dataFile.createNewFile();
				} catch (IOException e) {
					throw new TransactionException(ErrorType.FILE_ERROR, e.getMessage(), e);
				}
			} else {
				throw new TransactionException(ErrorType.FILE_ERROR, "File System does not exist");
			}			
		}
		return dataFile;
	}
	
	private JsonReader getReader() throws IOException{
		File dataFile = getFile(false);						
		JsonReader jsReader = new JsonReader(new InputStreamReader(new FileInputStream(dataFile), "UTF-8"));				
		jsReader.setLenient(true);
		return jsReader;						
	}
	
	private BufferedWriter getWriter() throws IOException { 
		File dataFile = getFile(true);
		return new BufferedWriter(new FileWriter(dataFile, true));
			
	}

}
