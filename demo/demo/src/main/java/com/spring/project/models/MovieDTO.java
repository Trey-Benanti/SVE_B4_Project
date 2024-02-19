package com.spring.project.models;

import jakarta.validation.constraints.*;

public class MovieDTO {

    @NotEmpty(message = "Movie title is required")
    private String movieTitle;

    @NotEmpty(message = "Cast is required")
    private String cast;

    @NotEmpty(message = "Director is required")
    private String director;

    @NotEmpty(message = "Producer is required")
    private String producer;

    @NotEmpty(message = "Synopsis is required")
    private String synop;

    @NotEmpty(message = "Reviews are required")
    private String reviews;

    @NotEmpty(message = "Trailer photo link is required")
    private String trailerPhoto;

    @NotEmpty(message = "Trailer video link is required")
    private String trailerVideo;

    @NotEmpty(message = "Rating is required")
    private String rating;

    @Min(0)
    private int nowPlaying;

    public int getNowPlaying() {
        return nowPlaying;
    }

    public void setNowPlaying(int nowPlaying) {
        this.nowPlaying = nowPlaying;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getSynop() {
        return synop;
    }

    public void setSynop(String synop) {
        this.synop = synop;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getTrailerPhoto() {
        return trailerPhoto;
    }

    public void setTrailerPhoto(String trailerPhoto) {
        this.trailerPhoto = trailerPhoto;
    }

    public String getTrailerVideo() {
        return trailerVideo;
    }

    public void setTrailerVideo(String trailerVideo) {
        this.trailerVideo = trailerVideo;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
} // MovieDTO
