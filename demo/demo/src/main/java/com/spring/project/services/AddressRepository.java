package com.spring.project.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.project.users.userinfo.CardInfo;

public interface AddressRepository extends JpaRepository<CardInfo, Long> {

}
