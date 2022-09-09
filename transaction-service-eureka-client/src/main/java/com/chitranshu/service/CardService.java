package com.chitranshu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.chitranshu.bean.MetroCard;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CardService {
	
	@Autowired
	RestTemplate restTemplate;
	
	@CircuitBreaker(name = "cardService", fallbackMethod = "fallbackForRechargeCard")
	public boolean rechargeCard(MetroCard card, double totalCharge) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<MetroCard> entity = new HttpEntity<MetroCard>(card);
		return restTemplate.exchange("http://card-service/cards/"+(-totalCharge), HttpMethod.PUT, entity, Boolean.class).getBody();
	}
	
	public boolean fallbackForRechargeCard(Exception e) {
		return false;
	}

}
