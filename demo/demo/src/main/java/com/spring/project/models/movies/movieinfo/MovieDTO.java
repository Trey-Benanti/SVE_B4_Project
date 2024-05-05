package com.spring.project.models.movies.movieinfo;

import jakarta.validation.constraints.*;

public class MovieDTO {

    @NotEmpty(message = "Movie title is required")
    private String movieTitle;
    @NotEmpty(message = "Category is required")
    private String category;
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
    @NotEmpty(message = "C.T Price is required")
    private String childTicket;
    @NotEmpty(message = "A.T price is required")
    private String adultTicket;
    @NotEmpty(message = "S.T price is required")
    private String seniorTicket;

    @NotEmpty
    private String nowPlaying;

    public String getNowPlaying() {
        return nowPlaying;
    }

    public void setNowPlaying(String nowPlaying) {
        this.nowPlaying = nowPlaying;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getChildTicket() {
        return childTicket;
    }

    public void setChildTicket(String childTicket) {
        this.childTicket = childTicket;
    }
    public String getAdultTicket() {
        return adultTicket;
    }

    public void setAdultTicket(String adultTicket) {
        this.adultTicket = adultTicket;
    }
    public String getSeniorTicket() {
        return seniorTicket;
    }

    public void setSeniorTicket(String seniorTicket) {
        this.seniorTicket = seniorTicket;
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
