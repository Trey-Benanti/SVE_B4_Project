package com.spring.project.models.users.userservices;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.project.models.users.userinfo.CardInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface CardInfoRepository extends JpaRepository<CardInfo, Long> {

}
