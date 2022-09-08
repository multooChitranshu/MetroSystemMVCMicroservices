package com.chitranshu.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chitranshu.bean.MetroCard;
import com.chitranshu.bean.MetroCardCredentials;
import com.chitranshu.bean.MetroCardIdsList;
import com.chitranshu.service.CardService;

@RestController
public class CardResource {
	
	@Autowired
	CardService cardService;
	
	@GetMapping(path = "/cards/allIds", produces = MediaType.APPLICATION_JSON_VALUE )
	public MetroCardIdsList getAllCardsResource() {
		MetroCardIdsList list=new MetroCardIdsList();
		list.setCardIds(cardService.getAllCards().stream()
				.map(MetroCard::getCardId)
				.collect(Collectors.toList()));
		return list;
	}
	
	@GetMapping(path="/cards/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MetroCard> getCardByIdResource(@PathVariable("id") long cardId){
		MetroCard card=cardService.getCardById(cardId);
		if(card.getCardId()==0) {
			return new ResponseEntity<MetroCard>(card, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<MetroCard>(card, HttpStatus.FOUND);
	}
	
	@PostMapping(path="/cards/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MetroCard> loginCardResource(@RequestBody MetroCardCredentials cardCredentials) {
		if(cardService.validateCard(cardCredentials))
			return new ResponseEntity<MetroCard>(cardService.getCardById(cardCredentials.getCardId()), HttpStatus.ACCEPTED);
		else
			return new ResponseEntity<MetroCard>(new MetroCard(), HttpStatus.NOT_ACCEPTABLE);
	}
	
	@PostMapping(path="/cards/register", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MetroCard> registerCardResource(@RequestBody MetroCardCredentials cardCredentials){
		long id=cardCredentials.getCardId();
		MetroCard card=cardService.getCardById(id);
		if(card.getCardId()==0) { 	//card not present, so register it
			cardService.addCard(cardCredentials);
			return new ResponseEntity<MetroCard>(cardService.getCardById(id), HttpStatus.ACCEPTED);
		}
		else {
			return new ResponseEntity<MetroCard>(new MetroCard(), HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@PutMapping(path="/cards/{amount}", consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public boolean rechargeCardResource(@RequestBody MetroCard card, @PathVariable("amount") double amount){
		return cardService.rechargeCard(card, amount);
	}
	
}
