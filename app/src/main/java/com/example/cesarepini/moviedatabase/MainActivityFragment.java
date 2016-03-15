package com.example.cesarepini.moviedatabase;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) root.findViewById(R.id.movies_list_view);
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                this.getContext(),
                R.layout.movies_listview_text_element,
                R.id.movies_listview_text_element,
                new ArrayList()
        );
        listView.setAdapter(
                arrayAdapter
        );
        
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
