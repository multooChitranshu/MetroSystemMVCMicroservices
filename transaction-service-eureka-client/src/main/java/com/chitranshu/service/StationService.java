package com.chitranshu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class StationService {
	
	@Autowired
	RestTemplate restTemplate;
	
	@CircuitBreaker(name = "stationsCovered", fallbackMethod = "fallbackForGetNoOfStationsCovered")
	public long getNoOfStationsCovered(String source, String destination) {
		return restTemplate.getForObject("http://station-service/stations/"+source+"/"+destination, Long.class);
	}
	
	public long fallbackForGetNoOfStationsCovered(Exception e) {
		return -1;
	}
}
