package com.shortly.shortlyapp.UI.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shortly.shortlyapp.model.VideoDetailResponse;

import java.util.List;

/**
 * Created by haroonyousuf on 6/15/17.
 */

public class MoviesViewPagerAdapter extends FragmentStatePagerAdapter {
    List<VideoDetailResponse> mItems;

    public MoviesViewPagerAdapter(FragmentManager fm, List<VideoDetailResponse> items) {
        super(fm);
        this.mItems = items;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

            return MovieViewPagerFragment.newInstance(position, mItems.get(position));
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return this.mItems.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SECTION 1";
            case 1:
                return "SECTION 2";
            case 2:
                return "SECTION 3";
            case 3:
                return "SECTION 4";
        }
        return null;
    }

    @Override
    public float getPageWidth(int position) {
        return 0.7f;
    }
}
