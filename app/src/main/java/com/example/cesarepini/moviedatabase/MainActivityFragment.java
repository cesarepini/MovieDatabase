package com.example.cesarepini.moviedatabase;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */

/**
 * TODO: implement a way to return the values to the MainActivity.fragment
 */

public class MainActivityFragment extends Fragment{

    /**
     * Log tag for this activity.
     */
    final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    public ArrayAdapter<String> moviesTitlesArrayAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_update_movies) {
            getMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) root.findViewById(R.id.movies_list_view);
        moviesTitlesArrayAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.movies_listview_text_element,
                R.id.movies_listview_text_element,
                new ArrayList<String>()
        );
        listView.setAdapter(
                moviesTitlesArrayAdapter
        );
        
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        getMovies();
    }

    public void getMovies(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String popOrRated =
                sharedPreferences.getString(
                        getString(R.string.get_key),
                        getString(R.string.get_default)
                );
        UpdateMoviesTask updateMovies = new UpdateMoviesTask();
        updateMovies.execute(popOrRated);
    }


    public class UpdateMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        /**
         * TAG for the logs.
         */
        final String LOG_TAG = UpdateMovies.class.getSimpleName();

        final String mostPopular = "popular?";
        final String topRated = "top_rated?";


        public UpdateMoviesTask(){
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
                final String APP_ID_KEY = "";
                final String API_KEY_DECLARATION = "api_key";
                final String BASIC_URL = "http://api.themoviedb.org/3/movie/";

                //determining whether popular or best rated movies are to download.
                String popularOrRated;
                if (params.length != 0) {
                    if (params[0].equals(getString(R.string.get_most_popular))) {
                        popularOrRated = mostPopular;
                    } else if (params[0].equals(getString(R.string.get_top_ranked))) {
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
                moviesTitlesArrayAdapter.clear();
                for (Movie movie : result) {
                    moviesTitlesArrayAdapter.add(movie.originalTitle);
                }
            }
        }
    }
}
