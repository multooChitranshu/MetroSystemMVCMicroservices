package com.chitranshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chitranshu.bean.MetroStation;
import com.chitranshu.persistence.StationDao;

@Service
public class StationServiceImpl implements StationService {

	@Autowired
	StationDao stationDao;
	
	@Override
	public List<MetroStation> getAllStations() {
		return stationDao.findAll();
	}

	@Override
	public long countStationsBetween(String source, String destination) {
		long sourceStationId=stationDao.findByStationName(source).get(0).getStationId();
		long destinationStationId=stationDao.findByStationName(destination).get(0).getStationId();
		long count=Math.abs(sourceStationId-destinationStationId);
		return count;
	}

}
