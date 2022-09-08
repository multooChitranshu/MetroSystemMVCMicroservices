package com.chitranshu.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chitranshu.bean.MetroCard;
import com.chitranshu.bean.Transaction;
import com.chitranshu.bean.TransactionList;
import com.chitranshu.service.TransactionService;

@RestController
public class TransactionResource {
	
	@Autowired
	TransactionService transactionService;
	
	@GetMapping(path = "/transactions/{id}")
	public TransactionList getTransactionList(@PathVariable("id") long cardId) {
		return transactionService.getAllTransactionsForId(cardId);
	}
	
	@GetMapping(path = "/transactions/last/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Transaction getLastTransaction(@PathVariable("id") long cardId) {
//		if null then ... what?
		return transactionService.getLastTransaction(cardId);
	}
	
	@PostMapping(path = "/transactions/swipeIn/{stationName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean swipeInResource(@RequestBody MetroCard card, @PathVariable("stationName") String stationName) {
		return transactionService.swipeIn(card.getCardId(), stationName);
	}
	
	@PostMapping(path = "/transactions/swipeOut/{stationName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String swipeOutResource(@RequestBody MetroCard card, @PathVariable("stationName") String stationName) {
		return transactionService.swipeOut(card, stationName);
	}
}
