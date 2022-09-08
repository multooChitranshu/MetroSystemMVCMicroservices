package com.chitranshu.persistence;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chitranshu.bean.Transaction;

public interface TransactionDao extends JpaRepository<Transaction, Long> {
	
	List<Transaction> findByCardId(long cardId);
	
	
	@Query(value ="SELECT * FROM transaction WHERE cardId=:cardId ORDER BY dateAndTimeOfBoarding DESC LIMIT 1", 
			nativeQuery = true)
	Transaction getLastTransaction(@Param("cardId") long cardId);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO transaction"
			+ "(cardId,sourceStationName,dateAndTimeOfBoarding)VALUES(?1,?2,CURRENT_TIMESTAMP)" , nativeQuery = true)
	int insertWhileSwipeIn(long cardId, String sourceStationName);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE transaction "
			+"SET destinationStationName=?2,fare=?3,dateAndTimeofExit=CURRENT_TIMESTAMP "
			+"WHERE cardId=?1 AND dateAndTimeofExit is null ",nativeQuery = true)
	int updateWhileSwipeOut(long cardId, String destination, double fare);
	

}
