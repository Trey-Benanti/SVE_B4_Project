package com.spring.project.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.project.models.Promotion;
import org.springframework.stereotype.Repository;

public interface PromoRepository extends JpaRepository<Promotion, Long>{
}
