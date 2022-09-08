package com.chitranshu.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="station")
public class MetroStation {
	
	@Id
	private long stationId;
	private String stationName;
	private long nextStationId;
	private long previousStationId;

}
