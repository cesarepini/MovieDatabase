package com.example.cesarepini.moviedatabase;

/**
 * This java class provides the definition of a Movie for the movie database app of the udacity course.
 * Created by cesarepini on 10/03/16.
 */
public class Movie {
    public String posterPath;
    public String plot;
    public String releaseDate;
    public String originalTitle;
    public double voteAverage;

    /**
     * Constructs an object of type movie, which contains all the fundamental data fields requested by the app.
     * @param originalTitle is the original title of the movie.
     * @param releaseDate is the release date of the movie in string format.
     * @param plot is the plot of the movie.
     * @param posterPath is the poster path to get the image.
     * @param voteAverage is the average rating of the movie.
     */
    public Movie(String originalTitle, String releaseDate, String plot, String posterPath, double voteAverage){
        this.originalTitle = originalTitle;
        this.releaseDate = releaseDate;
        this.plot = plot;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
    }

}