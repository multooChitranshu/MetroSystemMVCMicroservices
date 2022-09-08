package com.chitranshu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chitranshu.bean.MetroStation;

@Repository
public interface StationDao extends JpaRepository<MetroStation, String> {
	
	List<MetroStation> findByStationName(String stationName);

}
