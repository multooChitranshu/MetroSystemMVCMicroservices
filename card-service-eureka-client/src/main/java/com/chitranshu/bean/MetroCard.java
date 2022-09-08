package com.chitranshu.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Card")
public class MetroCard {
	
	@Id
	private long cardId;
	private long aadharId;
	private double balance;

}
