package com.chitranshu.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetroCard {
	
	private long cardId;
	private long aadharId;
	private double balance;

}
