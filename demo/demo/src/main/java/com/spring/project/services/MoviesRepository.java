package com.spring.project.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.project.models.Movie;

import java.util.List;

public interface MoviesRepository extends JpaRepository<Movie, Integer> {

    @Query(value = "SELECT * FROM movies WHERE "
    + "MATCH(movie_title, cast, director, producer) "
    + "AGAINST (?1)",
    nativeQuery = true)
    public List<Movie> search(String keyword);

} // MoviesRepository
