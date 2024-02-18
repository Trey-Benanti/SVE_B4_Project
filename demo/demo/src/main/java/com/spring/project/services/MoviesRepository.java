package com.spring.project.services;

import com.spring.project.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoviesRepository extends JpaRepository<Movie, Integer> {




} // MoviesRepository
