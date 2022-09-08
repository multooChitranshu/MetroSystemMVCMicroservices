package com.chitranshu.model.service;

import com.chitranshu.bean.MetroCard;
import com.chitranshu.bean.MetroCardCredentials;
import com.chitranshu.bean.MetroCardIdsList;
import com.chitranshu.bean.MetroStationNamesList;
import com.chitranshu.bean.Transaction;
import com.chitranshu.bean.TransactionList;

public interface MetroSystemService {
	
	MetroCard loginCard(MetroCardCredentials cardCredentials); 
	MetroCard registerCard(MetroCardCredentials cardCredentials);
	MetroCard getCardById(long cardId);
	boolean rechargeCard(MetroCard card,double amt);
	MetroCardIdsList getAllCardIds();
	
	MetroStationNamesList getStationNamesList();
	
	boolean swipeIn(MetroCard card, String stationName);
	String swipeOut(MetroCard card, String stationName);
	Transaction lastTransaction(long cardId);
	TransactionList transactionHistory(long cardId);
}
