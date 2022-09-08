package com.chitranshu.model.service;

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

@Service
public class MetroSystemServiceImpl implements MetroSystemService {
	
	@Autowired
	RestTemplate restTemplate;

	@Override
	public MetroCard loginCard(MetroCardCredentials cardCredentials) {
//		ResponseEntity<MetroCard> entity=restTemplate.postForEntity("http://card-service/cards/login", cardCredentials , MetroCard.class);
//		if(entity.getStatusCode()==HttpStatus.NOT_ACCEPTABLE) {
//			return new MetroCard();
//		}
//		return entity.getBody();
		try {
			return restTemplate.postForEntity("http://card-service/cards/login", cardCredentials , MetroCard.class).getBody();
		}
		catch(RestClientException e) {
			return new MetroCard();
		}
	}

	@Override
	public MetroCard registerCard(MetroCardCredentials cardCredentials) {
//		ResponseEntity<MetroCard> entity=restTemplate.postForEntity("http://card-service/cards/register", cardCredentials , MetroCard.class);
//		if(entity.getStatusCode()==HttpStatus.NOT_ACCEPTABLE) {
//			return new MetroCard();
//		}
//		return entity.getBody();
		try {
			return restTemplate.postForEntity("http://card-service/cards/register", cardCredentials , MetroCard.class).getBody();
		}
		catch(HttpClientErrorException e) {
			return new MetroCard();
		}
	}
	
	@Override
	public MetroCard getCardById(long cardId) {
//		ResponseEntity<MetroCard> entity=restTemplate.getForEntity("http://card-service/cards/"+cardId, MetroCard.class);
//		if(entity.getStatusCode()==HttpStatus.NOT_FOUND) {
//			return new MetroCard();
//		}
//		return entity.getBody();
		try {
			return restTemplate.getForEntity("http://card-service/cards/"+cardId, MetroCard.class).getBody();
		}
		catch(HttpStatusCodeException e) {
			return new MetroCard();
		}
	}

	@Override
	public boolean rechargeCard(MetroCard card, double amt) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<MetroCard> entity = new HttpEntity<MetroCard>(card);
		boolean recharged=restTemplate.exchange("http://card-service/cards/"+amt, HttpMethod.PUT, entity, Boolean.class).getBody();
		return recharged;
	}

	@Override
	public MetroCardIdsList getAllCardIds() {
		return restTemplate.getForObject("http://card-service/cards/allIds", MetroCardIdsList.class);
	}

	@Override
	public MetroStationNamesList getStationNamesList() {
		return restTemplate.getForObject("http://station-service/stations/allNames", MetroStationNamesList.class);
	}

	@Override
	public boolean swipeIn(MetroCard card, String stationName) {
		return restTemplate.postForObject("http://transaction-service/transactions/swipeIn/"+stationName, card, Boolean.class);
	}

	@Override
	public String swipeOut(MetroCard card, String stationName) {
		return restTemplate.postForObject("http://transaction-service/transactions/swipeOut/"+stationName, card, String.class);
		
	}

	@Override
	public Transaction lastTransaction(long cardId) {
		return restTemplate.getForObject("http://transaction-service//transactions/last/"+cardId, Transaction.class);
	}

	@Override
	public TransactionList transactionHistory(long cardId) {
		return restTemplate.getForObject("http://transaction-service//transactions/"+cardId, TransactionList.class);
	}

}
