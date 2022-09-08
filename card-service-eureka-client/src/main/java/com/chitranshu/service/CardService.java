package com.chitranshu.service;

import java.util.List;

import com.chitranshu.bean.MetroCard;
import com.chitranshu.bean.MetroCardCredentials;

public interface CardService {
	
	List<MetroCard> getAllCards();
	MetroCard getCardById(long cardId);
	MetroCard addCard(MetroCardCredentials cardCredentials);
	boolean validateCard(MetroCardCredentials cardCredentials);
	boolean rechargeCard(MetroCard card, double addOn);

}
