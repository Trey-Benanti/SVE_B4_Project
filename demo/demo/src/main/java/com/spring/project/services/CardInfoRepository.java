package com.spring.project.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.project.users.userinfo.CardInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface CardInfoRepository extends JpaRepository<CardInfo, Long> {



}
