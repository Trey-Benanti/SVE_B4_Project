package com.spring.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.project.models.Movie;

import java.util.List;

@Service
public class MovieServices {

    @Autowired
    private MoviesRepository repo;

    public List<Movie> search(String keyword) {
        return repo.search(keyword);
    } // search

} // MovieServices
