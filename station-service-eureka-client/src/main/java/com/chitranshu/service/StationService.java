package com.chitranshu.service;

import java.util.List;

import com.chitranshu.bean.MetroStation;

public interface StationService {
	
	List<MetroStation> getAllStations();
	long countStationsBetween(String source, String destination);

}
