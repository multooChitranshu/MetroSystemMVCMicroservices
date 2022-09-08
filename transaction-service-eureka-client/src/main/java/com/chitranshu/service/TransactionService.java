package com.chitranshu.service;

import com.chitranshu.bean.MetroCard;
import com.chitranshu.bean.Transaction;
import com.chitranshu.bean.TransactionList;

public interface TransactionService {
	
	TransactionList getAllTransactionsForId(long cardId);
	Transaction getLastTransaction(long cardId);
	boolean swipeIn(long cardId, String stationName);
	String swipeOut(MetroCard card, String stationName);
//	double calculateFare(int sourceStationId,int destinationStationId);
//	double calculateFine(long cardId,int sourceStationId,int destinationStationId);
	
}
