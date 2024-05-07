package com.spring.project.models.promos.promotionservices;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.project.models.promos.Promotion;

public interface PromoRepository extends JpaRepository<Promotion, Long>{
}
