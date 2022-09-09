package com.chitranshu.model.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.chitranshu.bean.MetroCard;
import com.chitranshu.bean.MetroCardCredentials;
import com.chitranshu.bean.MetroCardIdsList;
import com.chitranshu.bean.MetroStationNamesList;
import com.chitranshu.bean.Transaction;
import com.chitranshu.bean.TransactionList;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class MetroSystemServiceImpl implements MetroSystemService {
	
	@Autowired
	RestTemplate restTemplate;

	@Override
	@CircuitBreaker(name="LoginCard", fallbackMethod = "fallbackForloginCard")
	public MetroCard loginCard(MetroCardCredentials cardCredentials) {
		try {
			return restTemplate.postForEntity("http://card-service/cards/login", cardCredentials , MetroCard.class).getBody();
		}
		catch(RestClientException e) {
			return new MetroCard(); 
		}
	}
	public MetroCard fallbackForloginCard(Exception e) {
//		System.out.println("/n-------fallbackForLoginCard--------/n");
		return new MetroCard(500500500,500500500,100); //dummy card 
	}

	
	@Override
	@CircuitBreaker(name="RegisterCard", fallbackMethod = "fallbackForRegisterCard")
	public MetroCard registerCard(MetroCardCredentials cardCredentials) {
		try {
			return restTemplate.postForEntity("http://card-service/cards/register", cardCredentials , MetroCard.class).getBody();
		}
		catch(HttpClientErrorException e) {
			return new MetroCard();
		}
	}
	public MetroCard fallbackForRegisterCard(Exception e) {
//		System.out.println("/n-------fallbackForRegisterCard--------/n");
		return new MetroCard(500500500,500500500,100); //dummy card
	}
	
	
	@Override
	@CircuitBreaker(name = "GetCardById", fallbackMethod = "fallbackForGetCardById")
	public MetroCard getCardById(long cardId) {
		try {
			return restTemplate.getForEntity("http://card-service/cards/"+cardId, MetroCard.class).getBody();
		}
		catch(HttpStatusCodeException e) {
			return new MetroCard();
		}
	}
	public MetroCard fallbackForGetCardById(Exception e) {
//		System.out.println("/n-------fallbackForGetCardById--------/n");
		return new MetroCard(500500500,500500500,100); //dummy card
	}

	
	@Override
	@CircuitBreaker(name = "RechargeCard", fallbackMethod = "fallbackForRechargeCard")
	public boolean rechargeCard(MetroCard card, double amt) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<MetroCard> entity = new HttpEntity<MetroCard>(card);
		boolean recharged=restTemplate.exchange("http://card-service/cards/"+amt, HttpMethod.PUT, entity, Boolean.class).getBody();
		return recharged;
	}
	public boolean fallbackForRechargeCard(Exception e) {
//		System.out.println("/n-------fallbackForRechargeCard--------/n");
		return false;
	}

	
	@Override
	@CircuitBreaker(name="GetAllCardIds", fallbackMethod = "fallbackForGetAllCardIds")
	public MetroCardIdsList getAllCardIds() {
		return restTemplate.getForObject("http://card-service/cards/allIds", MetroCardIdsList.class);
	}
	public MetroCardIdsList fallbackForGetAllCardIds(Exception e) {
//		System.out.println("/n-------fallbackForGetAllCardIds--------/n");
		List<Long> dummyList=new ArrayList<>();
		dummyList.add((long)500500500);
		return new MetroCardIdsList(dummyList);
	}

	
	@Override
	@CircuitBreaker(name="GetStationNamesList", fallbackMethod = "fallbackForGetStationNamesList")
	public MetroStationNamesList getStationNamesList() {
		return restTemplate.getForObject("http://station-service/stations/allNames", MetroStationNamesList.class);
	}
	public MetroStationNamesList fallbackForGetStationNamesList(Exception e) {
		String[] arr= {"Mars"};      //dummy station
		return new MetroStationNamesList(Arrays.asList(arr));
	}

	
	
	@Override
	@CircuitBreaker(name = "SwipeIn", fallbackMethod = "fallbackForSwipeIn")
	public boolean swipeIn(MetroCard card, String stationName) {
		if(stationName.equals("Mars")) {
			return false;
		}
		return restTemplate.postForObject("http://transaction-service/transactions/swipeIn/"+stationName, card, Boolean.class);
	}
	public boolean fallbackForSwipeIn(Exception e) {
		return false;
	}
	
	
	
	@Override
	@CircuitBreaker(name = "SwipeOut", fallbackMethod = "fallbackForSwipeOut")
	public String swipeOut(MetroCard card, String stationName) {
		if(stationName.equals("Mars")) {
			return "Oops!!! Servers unreachable, please try again after some time...";
		}
		return restTemplate.postForObject("http://transaction-service/transactions/swipeOut/"+stationName, card, String.class);
		
	}
	public String fallbackForSwipeOut(Exception e) {
		return "Oops!!! Servers unreachable, please try again after some time...";
	}

	
	
	@Override
	@CircuitBreaker(name = "LastTransaction", fallbackMethod = "fallbackForLastTransaction")
	public Transaction lastTransaction(long cardId) {
		return restTemplate.getForObject("http://transaction-service//transactions/last/"+cardId, Transaction.class);
	}
	public Transaction fallbackForLastTransaction(Exception e) {
		return null;
	}

	
	
	@Override
	@CircuitBreaker(name = "TransactionHistory", fallbackMethod = "fallbackForTransactionHistory")
	public TransactionList transactionHistory(long cardId) {
		return restTemplate.getForObject("http://transaction-service//transactions/"+cardId, TransactionList.class);
	}
	public TransactionList fallbackForTransactionHistory(Exception e) {
		return new TransactionList(new ArrayList<>());
	}

}
