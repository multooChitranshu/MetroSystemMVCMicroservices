package com.chitranshu.persistence;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chitranshu.bean.MetroCard;

@Repository("CardDao")
public interface CardDao extends JpaRepository<MetroCard, Long> {
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE MetroCard SET balance=:newBal WHERE cardId=:id")
	int updateCardBalanceById(@Param("id") long id, @Param("newBal") double newBalance);
}
