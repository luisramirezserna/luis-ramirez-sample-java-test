package com.clip.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.clip.api.trx.dto.ErrorType;
import com.clip.api.trx.dto.TransactionReportResponseDTO;
import com.clip.api.trx.exception.TransactionException;
import com.clip.api.trx.model.TransactionModel;

public final class TransactionReportUtil {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	private TransactionReportUtil () {
	}

	public static List<TransactionReportResponseDTO> createTransactionReport (List<TransactionModel> lstTrx) {
		Map<Long, TransactionReportResponseDTO> map = new HashMap<Long, TransactionReportResponseDTO>();
		lstTrx.stream().forEach( trx -> {					
			try {									
				Calendar c = Calendar.getInstance();
				Date date = DATE_FORMAT.parse(trx.getDate()); 
				c.setTime(date);
				int startDayWeek = Calendar.FRIDAY;
				int endDayWeek = Calendar.THURSDAY;
				int dayWeek = c.get(Calendar.DAY_OF_WEEK);				
				int dayMonth = c.get(Calendar.DAY_OF_MONTH);
				int daysMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
				int daysDiffStart = dayWeek >= startDayWeek ? dayWeek - startDayWeek : 7 - (startDayWeek - dayWeek);
				daysDiffStart = (dayMonth - daysDiffStart) < 0 ? dayMonth - 1 : daysDiffStart;				
				Date weekStartDate = new Date(date.getTime() - daysDiffStart * 24 * 3600 * 1000);						
				TransactionReportResponseDTO report = map.get(weekStartDate.getTime());
				if (null == report) {
					report = new TransactionReportResponseDTO(trx.getUserID());									 					
					report.setWeekStartDate(DATE_FORMAT.format(weekStartDate));
					int daysDiffFinish = (endDayWeek - dayWeek) < 0 ? 7 - (dayWeek - endDayWeek) : endDayWeek - dayWeek;
					daysDiffFinish = (dayMonth + daysDiffFinish) > daysMonth ? daysMonth - dayMonth : daysDiffFinish;					
					Date weekFinishDate = new Date(date.getTime() + daysDiffFinish * 24 * 3600 * 1000);		
					report.setWeekFinishDate(DATE_FORMAT.format(weekFinishDate));
					map.put(weekStartDate.getTime(), report);
				}
				report.setAmount(report.getAmount() + trx.getAmount());
				report.setQuantity(report.getQuantity() + 1);
				
			} catch (ParseException e) {
				throw new TransactionException(ErrorType.GENERAL_ERROR, e);
			}
		});
		
		Map<Long, TransactionReportResponseDTO> sorted = map.entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
				(oldValue, newValue) -> oldValue, LinkedHashMap::new));
		
		
		List<TransactionReportResponseDTO> lstRpt = sorted.values().stream().collect(Collectors.toList());
		Double totalAmount = 0.0;
		for (TransactionReportResponseDTO rpt : lstRpt) {
			rpt.setTotalAmount(totalAmount);
			totalAmount = totalAmount + rpt.getAmount();
		}
		return lstRpt;
	}

}
