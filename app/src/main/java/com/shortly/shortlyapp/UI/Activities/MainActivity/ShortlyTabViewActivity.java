package com.shortly.shortlyapp.UI.Activities.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import com.shortly.shortlyapp.UI.Activities.CustomViewPager;
import com.shortly.shortlyapp.UI.Activities.SearchListFragment;
import com.shortly.shortlyapp.UI.Activities.VideoDetail.VideoDetailActivity;
import com.shortly.shortlyapp.UI.Activities.VideoListFragment;
import com.shortly.shortlyapp.UI.Activities.WatchLaterFragment;
import com.shortly.shortlyapp.model.VideoDetailResponse;
import com.shortly.shortlyapp.model.WatchLaterResponse;
import com.shortly.shortlyapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ShortlyTabViewActivity extends AppCompatActivity implements SearchListFragment.OnListFragmentInteractionListener, VideoListFragment.OnListFragmentInteractionListener, WatchLaterFragment.OnListFragmentInteractionListener {

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
    private CustomViewPager mViewPager;
    private int mSearchPageNumber = 1;
    private int mVideoListPageNumber = 1;
    private int mWatchLaterPageNumber = 1;
    private int mApiCallCount = 0;
    int mSelectedTabIndex = 0;

    private int mVideoListCount = 0;
    private int mWatchLaterListCount = 0;
    private int mSearchListCount = 0;

    List<WatchLaterResponse> mWatchLaterList;
    ArrayList<Object> mVideoListData;
    List<VideoDetailResponse> mSearchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        ProgressHandler.showProgressDialog(this, getString(R.string.app_name), "Loading...", 0, Constants.ProgressBarStyles.PROGRESS_BAR_ANIMATED, "", "");
        getVideoList(true);
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
        mViewPager = (CustomViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPagingEnabled(false);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition();
                mSelectedTabIndex = tabPosition;
                mViewPager.setCurrentItem(tabPosition, false);
                if (tabPosition == 0) {
                    getVideoList(false);
                } else if (tabPosition == 1) {
//                    ProgressHandler.showProgressDialog(ShortlyTabViewActivity.this, "", "An error occured on server.", 0, Constants.ProgressBarStyles.PROGRESS_BAR_NONE, getString(R.string.button_title_ok), "");
                    searchData();
                } else if (tabPosition == 2) {
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

    private void getVideoList(final boolean fetchWatchLaterData) {
        new Thread() {
            public void run() {
                APICalls.setSyncInterface(new SyncInterface() {
                    @Override
                    public void onAPIResult(int result, Object resultObject, int totalRecords) {
                        switch (result) {
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS:
//                                mVideoListData = (ArrayList<Object>) resultObject;
                                updateVideoListData((ArrayList<Object>) resultObject);
                                mVideoListCount = totalRecords;
                                mSectionsPagerAdapter.notifyDataSetChanged();
                                if (fetchWatchLaterData) {
                                    getWatchLaterList();
                                    hideLoader();
                                }
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
//                                mWatchLaterList = (List<WatchLaterResponse>) resultObject;
                                updateWatchLaterListData((List<WatchLaterResponse>) resultObject);
                                mWatchLaterListCount = totalRecords;
                                //TODO: update recycler view
                                hideLoader();
                                Log.v("", "Watch Later Complete");
                                break;
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_UNAUTHORIZED_USER:
                                showError(result);
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
//                                mSearchList = (List<VideoDetailResponse>) resultObject;
                                updateSearchListData((List<VideoDetailResponse>) resultObject);
                                mSearchListCount = totalRecords;
                                Log.v("", "Search Complete");
                                break;
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_UNAUTHORIZED_USER:
                                showError(result);
                                break;
                            default:
                                ProgressHandler.hideProgressDialogue();
                                break;
                        }
                    }
                });
                APICalls.fetchSearchResults("", -1, -1, ShortlyTabViewActivity.this);
            }
        }.start();
    }

    private void showError(int errorType) {
        final String message;
        switch (errorType) {
            case Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY:
                message = getString(R.string.key_error_no_connectivity);
                break;
            case Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR:
            case Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE:
                message = getString(R.string.key_error_service_failure);
                break;
            default:
                message = getString(R.string.key_error_service_failure);
                break;
        }

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    ProgressHandler.upDateProgressDialog(ShortlyTabViewActivity.this, "", message, 0, Constants.ProgressBarStyles.PROGRESS_BAR_NONE, getString(R.string.button_title_ok), "");
                }
            }
        };
        timerThread.start();
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
    public void onListFragmentInteraction(WatchLaterResponse item) {

        showVideoDetail(item.getVideoId());
        //TODO: watch later item click
    }

    @Override
    public void onListFragmentInteraction(VideoDetailResponse item) {
        showVideoDetail(item.getVideoId());
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
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if (position == 0) {
                return VideoListFragment.newInstance(1, mVideoListData);

            } else if (position == 1) {

                return SearchListFragment.newInstance(2, mSearchList);

            } else if (position == 2) {
                return WatchLaterFragment.newInstance(1, mWatchLaterList);
            } else {
                return PlaceholderFragment.newInstance(position + 1);
            }

        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
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

    private void hideLoader() {
        mApiCallCount++;
        if (mApiCallCount == 2) {

            ProgressHandler.hideProgressDialogue();
        }

    }

    private void showVideoDetail(int videoId) {
        Intent intent = new Intent(ShortlyTabViewActivity.this, VideoDetailActivity.class);
        intent.putExtra("videoId", videoId);
        startActivity(intent);
    }

    private void updateVideoListData(ArrayList<Object> newResult) {
        mVideoListData = newResult;
        /*if (mVideoListData == null || mVideoListData.size() == 0) {
            mVideoListData = newResult;
        } else {
            if (!mVideoListData.equals(newResult)) {
                newResult.remove(0);
                newResult.remove(1);
                mVideoListData.addAll(newResult);
                mSectionsPagerAdapter.notifyDataSetChanged();
            }
            //TODO: update recycler view
        }*/
    }

    private void updateWatchLaterListData(List<WatchLaterResponse> newResult) {
        mWatchLaterList = newResult;
        /*if (mWatchLaterList == null || mWatchLaterList.size() == 0) {
            //
            mWatchLaterList = newResult;
        } else {
            if (!mWatchLaterList.equals(newResult)) {
                mWatchLaterList.addAll(newResult);
            }
            mSectionsPagerAdapter.notifyDataSetChanged();
            //TODO: update recycler view
        }*/
    }

    private void updateSearchListData(List<VideoDetailResponse> newResult) {
        mSearchList = newResult;
        mSectionsPagerAdapter.notifyDataSetChanged();
        /*if (mSearchList == null || mSearchList.size() == 0) {
            //
            mSearchList = newResult;
        } else {
            mSearchList.addAll(newResult);
            mSectionsPagerAdapter.notifyDataSetChanged();
            //TODO: update recycler view
        }*/
    }

    private boolean isLoadMoreButtonRequired(int type) {
        boolean isRequired = false;

        if (type == 1) {
            isRequired = (mVideoListCount > (mVideoListPageNumber * 10));
        } else if (type == 2) {
            isRequired = (mWatchLaterListCount > (mWatchLaterPageNumber * 10));
        } else {
            isRequired = (mSearchListCount > (mSearchPageNumber * 12));
        }
        return isRequired;
    }
}
