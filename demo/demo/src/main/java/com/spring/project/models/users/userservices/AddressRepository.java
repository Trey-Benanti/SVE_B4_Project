package com.spring.project.models.users.userservices;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.project.users.userinfo.CardInfo;

public interface AddressRepository extends JpaRepository<CardInfo, Long> {

}
