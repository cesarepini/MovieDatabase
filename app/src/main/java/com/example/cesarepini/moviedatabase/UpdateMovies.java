package com.example.cesarepini.moviedatabase;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by cesarepini on 15/03/16.
 * Async Task for updating the movies.
 */

/**
 * TODO: implement a way to return the values to the MainActivity.fragment
 */

public class UpdateMovies extends AsyncTask <String, Void, ArrayList<Movie>> {

    public AsyncResponse delegate = null;

    private Context context;

    /**
     * TAG for the logs.
     */
    final String LOG_TAG = UpdateMovies.class.getSimpleName();

    final String mostPopular = "popular?";
    final String topRated = "top_rated?";


    public UpdateMovies(Context context){
        this.context = context;
    }

    /**
     *
     * @param params most popular or top ranked movies
     * @return an ArrayList of movies.
     */
    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJSONString =null;

        try{
            final String APP_ID_KEY = "f1c3d674f73c91903ee2c6a65692df34";
            final String API_KEY_DECLARATION = "api_key";
            final String BASIC_URL = "http://api.themoviedb.org/3/movie/";

            //determining whether popular or best rated movies are to download.
            String popularOrRated;
            if (params.length != 0) {
                if (params[0].equals(context.getString(R.string.get_most_popular))) {
                    popularOrRated = mostPopular;
                } else if (params[0].equals(context.getString(R.string.get_top_ranked))) {
                    popularOrRated = topRated;
                } else {
                    popularOrRated = mostPopular;
                }
            } else {
                popularOrRated = mostPopular;
            }

            //build the URI and the URL for downloading the movies
            Uri uri = Uri.parse(
                    BASIC_URL.concat(popularOrRated)).buildUpon()
                    .appendQueryParameter(API_KEY_DECLARATION, APP_ID_KEY)
                    .build();
            URL url = new URL(uri.toString());

            // Create the request to tmdb, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            //appending the lines to the buffer
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            moviesJSONString = buffer.toString();

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error", e);
        } finally {
            //terminate the exiting connection.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            //close the input reader.
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }

        }

        try {
            JSONDataParser jsonDataParser = new JSONDataParser(moviesJSONString);
            return jsonDataParser.parseMovie();
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> result){
        if (result != null) {
            delegate.processFinish(result);
        }
    }
}
