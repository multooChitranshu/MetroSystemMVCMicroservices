package com.chitranshu.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chitranshu.bean.MetroCardCredentials;

@Repository(value = "CardCredentialsDao")
public interface CardCredentialsDao extends JpaRepository<MetroCardCredentials, Long> {

}
