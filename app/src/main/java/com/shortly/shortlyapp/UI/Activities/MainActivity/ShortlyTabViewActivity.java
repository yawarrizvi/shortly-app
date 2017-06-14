package com.shortly.shortlyapp.UI.Activities.MainActivity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.shortly.shortlyapp.Interfaces.SyncInterface;
import com.shortly.shortlyapp.Logic.ProgressHandler.ProgressHandler;
import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.Sync.APICalls;
import com.shortly.shortlyapp.UI.Activities.ItemFragment;
import com.shortly.shortlyapp.UI.Activities.dummy.DummyContent;
import com.shortly.shortlyapp.model.VideoDetailResponse;
import com.shortly.shortlyapp.model.WatchLaterResponse;
import com.shortly.shortlyapp.utils.Constants;

import java.util.HashMap;
import java.util.List;

public class ShortlyTabViewActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private int mSearchPageNumber = 1;
    private int mVideoListPageNumber = 1;
    private int mWatchLaterPageNumber = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortly_tab_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setSelectedTabIndicatorHeight(0);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.selector_tab_list));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.selector_tab_search));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.selector_tab_watch_later));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.selector_tab_settings));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition();
                mViewPager.setCurrentItem(tabPosition);
                if (tabPosition == 0) {
                    searchData();
                } else if (tabPosition == 1) {
                    //get video list
                    getVideoList();
                } else if (tabPosition == 2) {
                    //get watch later videos
                    getWatchLaterList();
                } else {
//                    Intent intent = new Intent(ShortlyTabViewActivity.this, VideoDetailActivity.class);
//                    startActivity(intent);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void getVideoList() {
        new Thread() {
            public void run() {
                APICalls.setSyncInterface(new SyncInterface() {
                    @Override
                    public void onAPIResult(int result, Object resultObject, int totalRecords) {
                        switch (result) {
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS:
                                HashMap<String, Object> videoListData = (HashMap<String, Object>) resultObject;
                                Log.v("", "Video List Complete");
                                break;
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_UNAUTHORIZED_USER:
                                ProgressHandler.hideProgressDialogue();
                                break;
                            default:

                                break;
                        }
                    }
                });
                APICalls.getFirstVideoData(mVideoListPageNumber, ShortlyTabViewActivity.this);

            }


        }.start();
    }

    private void getWatchLaterList() {
        new Thread() {
            public void run() {
                APICalls.setSyncInterface(new SyncInterface() {
                    @Override
                    public void onAPIResult(int result, Object resultObject, int totalRecords) {
                        switch (result) {
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS:
                                List<WatchLaterResponse> resultData = (List<WatchLaterResponse>) resultObject;
                                Log.v("", "Watch Later Complete");
                                break;
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_UNAUTHORIZED_USER:
                                ProgressHandler.hideProgressDialogue();
                                break;
                            default:

                                break;
                        }
                    }
                });
                APICalls.getWatchLaterVideos(mWatchLaterPageNumber, ShortlyTabViewActivity.this);

            }


        }.start();
    }

    private void searchData() {

        new Thread() {
            public void run() {
                APICalls.setSyncInterface(new SyncInterface() {
                    @Override
                    public void onAPIResult(int result, Object resultObject, int totalRecords) {
                        switch (result) {
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS:
                                List<VideoDetailResponse> resultData = (List<VideoDetailResponse>) resultObject;
                                Log.v("", "Search Complete");
                                break;
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_UNAUTHORIZED_USER:
                                ProgressHandler.hideProgressDialogue();
                                break;
                            default:

                                break;
                        }
                    }
                });
                APICalls.fetchSearchResults("", -1, -1, ShortlyTabViewActivity.this);
            }


        }.start();
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shortly_tab_view, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_shortly_tab_view, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 1) {
                return ItemFragment.newInstance(2);
            } else {
                return PlaceholderFragment.newInstance(position + 1);
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
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
    }
}
