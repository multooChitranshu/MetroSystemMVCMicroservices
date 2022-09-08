package com.chitranshu.bean;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {
	
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long transactionId;
	private long cardId;
	private String sourceStationName;
	@CreationTimestamp
	private LocalDateTime dateAndTimeOfBoarding;
	private String destinationStationName;
	@UpdateTimestamp
	private LocalDateTime dateAndTimeOfExit;
    private double fare;
}

