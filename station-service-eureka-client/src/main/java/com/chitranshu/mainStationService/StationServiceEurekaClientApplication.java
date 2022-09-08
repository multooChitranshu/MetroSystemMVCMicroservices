package com.chitranshu.mainStationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.chitranshu.bean.MetroStation;
import com.chitranshu.persistence.StationDao;

@SpringBootApplication(scanBasePackages = "com.chitranshu")
@EnableJpaRepositories(basePackages = "com.chitranshu.persistence")
@EnableEurekaClient
@EntityScan(basePackages = "com.chitranshu.bean")
public class StationServiceEurekaClientApplication /*implements CommandLineRunner*/{

//	@Autowired
//	StationDao stationDao;
	
	public static void main(String[] args) {
		SpringApplication.run(StationServiceEurekaClientApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		
//		stationDao.save(new MetroStation(1,"Chandni Chowk",0,2));
//		stationDao.save(new MetroStation(2,"Rajiv Chowk",1,3));
//		stationDao.save(new MetroStation(3,"Nizamuddin",2,4));
//		stationDao.save(new MetroStation(4,"Karol Bagh",3,5));
//		stationDao.save(new MetroStation(5,"Preet Vihar",4,0));
//	}

}
