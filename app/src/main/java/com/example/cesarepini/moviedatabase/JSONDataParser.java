package com.example.cesarepini.moviedatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This java class provides a data parser for json objects.
 * Created by cesarepini on 10/03/16.
 */
public class JSONDataParser {

    public String JSONDataParserString;

    /**
     * It constructs an object of type JSONDataParserString.
     * @param JSONDataParserString is the string forming up the object.
     */
    public JSONDataParser(String JSONDataParserString){
        this.JSONDataParserString = JSONDataParserString;
    }

    /**
     *It provides a method to parse movies for the popular movies app of udacity course.
     *
     * @return an array list of movies with the following data for each movie:
     *              1. the original title
     *              2. the release date
     *              3. the plot
     *              4. the url of the poster
     *              5. the average vote
 *              Each element of the array is an object of type Movie.
     * @throws JSONException if the input string is not readable
     */
    public ArrayList<Movie> parseMovie() throws JSONException{
        ArrayList<Movie> movieArrayList = new ArrayList<>();
        final String OWM_MOVIE_OBJECT = "results";
        final String OWM_MOVIE_VOTE_AVERAGE = "vote_average";
        final String OWM_MOVIE_ORIGINAL_TITLE = "original_title";
        final String OWM_MOVIE_PLOT = "overview";
        final String OWM_MOVIE_RELEASE_DATE = "release_date";
        final String OWM_MOVIE_POSTER_PATH = "poster_path";

        final String POSTER_PATH_BASIC_URL = "http://image.tmdb.org/t/p/";
        final String POSTER_PATH_FORMAT = "w185/";


        JSONObject moviesJson = new JSONObject(JSONDataParserString);
        JSONArray movieJsonArray = moviesJson.getJSONArray(OWM_MOVIE_OBJECT);

        int resultsStrsLength;
//        if (numOfMovies > movieJsonArray.length()) {
//            resultsStrsLength = movieJsonArray.length();
//        } else {
//            resultsStrsLength = numOfMovies;
//        }
        resultsStrsLength = movieJsonArray.length();

        for(int i = 0; i < resultsStrsLength; i++) {
            JSONObject movieObject = movieJsonArray.getJSONObject(i);
            movieArrayList.add(new Movie(
                    movieObject.getString(OWM_MOVIE_ORIGINAL_TITLE),
                    movieObject.getString(OWM_MOVIE_RELEASE_DATE),
                    movieObject.getString(OWM_MOVIE_PLOT),
                    POSTER_PATH_BASIC_URL.concat(POSTER_PATH_FORMAT).concat(movieObject.getString(OWM_MOVIE_POSTER_PATH)),
                    movieObject.getDouble(OWM_MOVIE_VOTE_AVERAGE)
            ));
            }
        return movieArrayList;
    }
}
