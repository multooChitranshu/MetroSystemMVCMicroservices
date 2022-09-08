package com.chitranshu.bean;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
	
	private long transactionId;
	private long cardId;
	private String sourceStationName;
	private LocalDateTime dateAndTimeOfBoarding;
	private String destinationStationName;
	private LocalDateTime dateAndTimeOfExit;
    private double fare;
}

