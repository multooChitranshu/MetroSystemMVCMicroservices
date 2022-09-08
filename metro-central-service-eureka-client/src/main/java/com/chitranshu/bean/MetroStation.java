package com.chitranshu.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetroStation {
	
	private long stationId;
	private String stationName;
	private long nextStationId;
	private long previousStationId;

}
