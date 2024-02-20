package com.spring.project.services;

import com.spring.project.models.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServices {

    @Autowired
    private MoviesRepository repo;

    public List<Movie> search(String keyword) {
        return repo.search(keyword);
    } // search

} // MovieServices
