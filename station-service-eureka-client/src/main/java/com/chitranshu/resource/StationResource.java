package com.chitranshu.resource;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.chitranshu.bean.MetroStation;
import com.chitranshu.bean.MetroStationNamesList;
import com.chitranshu.service.StationService;

@RestController
public class StationResource {

	@Autowired
	StationService stationService;
	
	@GetMapping(path = "/stations/allNames", produces = MediaType.APPLICATION_JSON_VALUE)
	public MetroStationNamesList getAllStationNames(){
		MetroStationNamesList stations=new MetroStationNamesList();
		stations.setAllStationNames(stationService.getAllStations().stream()
				.map(MetroStation::getStationName)
				.collect(Collectors.toList()));
		return stations;
	}
	
	@GetMapping(path = "/stations/{source}/{destination}", produces = MediaType.APPLICATION_JSON_VALUE)
	public long countStationsResource(@PathVariable("source") String source, @PathVariable("destination") String destination) {
		return stationService.countStationsBetween(source, destination);
	}
	
}
