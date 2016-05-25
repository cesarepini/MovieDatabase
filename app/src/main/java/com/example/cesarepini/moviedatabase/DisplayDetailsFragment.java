package com.example.cesarepini.moviedatabase;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DisplayDetailsFragment extends Fragment {

    public DisplayDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_details, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            String posterPath = intent.getStringExtra(Intent.EXTRA_TEXT);
            ImageView imageView = (ImageView) view.findViewById(R.id.poster_visualisation);
            Picasso.with(getActivity()).load(posterPath).into(imageView);
        }
        return view;
    }
}
