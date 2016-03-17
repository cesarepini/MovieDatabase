package com.example.cesarepini.moviedatabase;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */


public class MainActivityFragment extends Fragment implements AsyncResponse{

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
        
        return root;
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
        UpdateMovies updateMovies = new UpdateMovies(getActivity());
        updateMovies.delegate = this;
        updateMovies.execute(popOrRated);
    }

    public void processFinish(ArrayList<Movie> result){
        moviesTitlesArrayAdapter.clear();
        for (Movie movie : result) {
            moviesTitlesArrayAdapter.add(movie.originalTitle);
        }
    }
}
