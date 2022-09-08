package com.chitranshu.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.chitranshu.bean.MetroCard;
import com.chitranshu.bean.Transaction;
import com.chitranshu.bean.TransactionList;
import com.chitranshu.persistence.TransactionDao;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class TransactionServiceImpl implements TransactionService {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	TransactionDao transactionDao;
	
	@Autowired
	StationService stationService;

	@Override
	public TransactionList getAllTransactionsForId(long cardId) {
		return new TransactionList(transactionDao.findByCardId(cardId));
	}

	@Override
	public Transaction getLastTransaction(long cardId) {
//		transactionDao.findById(null); use this in case of no transactions
		return transactionDao.getLastTransaction(cardId);
	}

	@Override
	public boolean swipeIn(long cardId, String stationName) {
		int rows=transactionDao.insertWhileSwipeIn(cardId, stationName);
		if(rows>0) {
			return true;
		}
		return false;
	}

	@Override
	public String swipeOut(MetroCard card, String destinationStation) {
//		double fare=0;
//		int rows=transactionDao.updateWhileSwipeOut(cardId, stationName, fare);
//		if(rows>0) {
//			return true;
//		}
		long cardId=card.getCardId();
		String result="";
		String sourceStation=getLastTransaction(cardId).getSourceStationName();
		double fare=0, fine=0;
//		if source and destination are same, a fine of Rs. 30 is levied
		if(sourceStation.equals(destinationStation)) {
			fare=0;
			fine=30;
		}
		else {
			fare=calculateFare(sourceStation,destinationStation);
		}
		fine+=calculateFine(cardId, sourceStation, destinationStation);
		double totalCharge=fare+fine;
		if(card.getBalance()>=totalCharge) {
			if(transactionDao.updateWhileSwipeOut(cardId, destinationStation, totalCharge)>0) {
				
				HttpHeaders headers = new HttpHeaders();
				HttpEntity<MetroCard> entity = new HttpEntity<MetroCard>(card);
				boolean recharged=restTemplate.exchange("http://card-service/cards/"+(-totalCharge), HttpMethod.PUT, entity, Boolean.class).getBody();
				if(recharged) {
					result="Swipe-Out successful! Rs."+totalCharge+" was deducted.";
					if(fine>0) {
						result+="( includes Rs."+fine+" fine)";
					}
				}
				else {
					result="Oops! Recharge failed! Please try again";
				}
				return result;
			}
			result="Insufficient balance. Please recharge. Fare: Rs."+fare+", Fine levied: Rs."+fine+" Current balance: Rs"+card.getBalance();
		}
		else {
			result="Swipe-out unsuccessful! Invalid station ID";
		}
		return result;
	}

	
	private double calculateFare(String source, String destination) {
//		long noOfStationsCovered=restTemplate.getForObject("http://station-service/stations/"+source+"/"+destination, Long.class);
		long noOfStationsCovered=stationService.getNoOfStationsCovered(source, destination);
		return noOfStationsCovered*5;
	}


	private double calculateFine(long cardId, String source, String destination) {
		Transaction lastTransaction=transactionDao.getLastTransaction(cardId);
//		long noOfStationsCovered=restTemplate.getForObject("http://station-service/stations/"+source+"/"+destination, Long.class);
		long noOfStationsCovered=stationService.getNoOfStationsCovered(source, destination);
//		assuming time to travel from a station to the immediate next station is 30 mins.
//		If more time taken, then charge Rs. 30 for every extra 30 mins
		long countOf30mins=lastTransaction.getDateAndTimeOfBoarding().until(LocalDateTime.now(), ChronoUnit.MINUTES)/30;
		return Math.max(0,(countOf30mins-noOfStationsCovered)*30);
	}
	
//	@CircuitBreaker(name = "stationsCovered", fallbackMethod = "fallbackForGetNoOfStationsCovered")
//	public long getNoOfStationsCovered(String source, String destination) {
//		return restTemplate.getForObject("http://station-service/stations/"+source+"/"+destination, Long.class);
//	}
//	
//	public long fallbackForGetNoOfStationsCovered(Exception e) {
//		return 0;
//	}

}
