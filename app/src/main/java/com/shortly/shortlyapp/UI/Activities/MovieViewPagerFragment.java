package com.shortly.shortlyapp.UI.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.UI.Activities.MainActivity.ShortlyTabViewActivity;

/**
 * Created by haroonyousuf on 6/15/17.
 */

public class MovieViewPagerFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public MovieViewPagerFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ShortlyTabViewActivity.PlaceholderFragment newInstance(int sectionNumber) {
        ShortlyTabViewActivity.PlaceholderFragment fragment = new ShortlyTabViewActivity.PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_pager_view, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}