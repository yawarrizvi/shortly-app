package com.shortly.shortlyapp.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.UI.Activities.VideoDetail.VideoDetailActivity;
import com.shortly.shortlyapp.model.VideoDetailResponse;
import com.squareup.picasso.Picasso;


public class MovieViewPagerFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_PAGER_ITEM = "pager_item";



    public MovieViewPagerFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MovieViewPagerFragment newInstance(int sectionNumber, VideoDetailResponse item) {
        MovieViewPagerFragment fragment = new MovieViewPagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putParcelable(ARG_PAGER_ITEM, item);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final VideoDetailResponse mItem = getArguments().getParcelable(ARG_PAGER_ITEM);
        View rootView = inflater.inflate(R.layout.fragment_movie_pager_view, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.img_view_thumbnail);
        TextView textView = (TextView) rootView.findViewById(R.id.title);
        TextView credits = (TextView) rootView.findViewById(R.id.cast);
        if (mItem != null) {
            textView.setText(mItem.getTitle());
            credits.setText(mItem.getCasts());
            Picasso.with(getContext()).load(mItem.getThumbnails()).into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), VideoDetailActivity.class);
                    intent.putExtra("videoId", mItem.getVideoId());
                    startActivity(intent);
                }
            });
        }
        return rootView;
    }
}