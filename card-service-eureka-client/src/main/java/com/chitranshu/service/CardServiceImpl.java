package com.chitranshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.chitranshu.bean.MetroCard;
import com.chitranshu.bean.MetroCardCredentials;
import com.chitranshu.persistence.CardCredentialsDao;
import com.chitranshu.persistence.CardDao;
@Service
public class CardServiceImpl implements CardService {

	@Autowired
//	@Qualifier("CardDao")
	CardDao cardDao;
	
	@Autowired
//	@Qualifier("CardCredentialsDao")
	CardCredentialsDao cardCredentialsDao;
	
	@Override
	public List<MetroCard> getAllCards() {
		return cardDao.findAll();
	}

	@Override
	public MetroCard getCardById(long cardId) {
		return cardDao.findById(cardId).orElse(new MetroCard());
	}

	@Override
	public MetroCard addCard(MetroCardCredentials cardCredentials) {
		long id=cardCredentials.getCardId();
		String password=cardCredentials.getPassword();
		MetroCard newCard=new MetroCard(id, id, 100);
		MetroCardCredentials newCardCredentials=new MetroCardCredentials(id, password);
		cardDao.save(newCard);
		cardCredentialsDao.save(newCardCredentials);
		return newCard;
	}

	@Override
	public boolean validateCard(MetroCardCredentials cardCredentials) {
//		WARNING! CALL THE getCardById FIRST IN THE RESTCONTROLLER TO AVOID
//		NoSuchElementException GETTING THROWN FROM HERE.
		long id=cardCredentials.getCardId();
		String password=cardCredentials.getPassword();
		if(cardCredentialsDao.findById(id).get().getPassword().equals(password))
			return true;
		return false;
	}

	@Override
	public boolean rechargeCard(MetroCard card, double addOn) {
		int rows=cardDao.updateCardBalanceById(card.getCardId(), card.getBalance()+addOn);
		if(rows>0) {
			return true;
		}
		return false;
	}

}
