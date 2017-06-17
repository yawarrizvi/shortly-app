package com.shortly.shortlyapp.UI.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.shortly.shortlyapp.Interfaces.SyncInterface;
import com.shortly.shortlyapp.Logic.ProgressHandler.ProgressHandler;
import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.Sync.APICalls;
import com.shortly.shortlyapp.model.DurationResponse;
import com.shortly.shortlyapp.model.GenreListResponse;
import com.shortly.shortlyapp.model.VideoDetailResponse;
import com.shortly.shortlyapp.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SearchListFragment extends Fragment implements SearchView.OnQueryTextListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<VideoDetailResponse> mItems;
    SearchListRecyclerViewAdapter mAdapter;
    SearchView mSearchView;
    String mSearchTerm = "";

    int mTotalRecords = 0;
    int pageIndex = 1;
    int previousVisibleItems, visibleItemCount, totalItemCount; //infinite scroll

    boolean mIsViewVisible;

    LinearLayoutManager mLinearLayoutManager;

    List<DurationResponse> mDurationList;
    List<GenreListResponse> mGenreList;

    Spinner mGenreSpinner;
    Spinner mDurationSpinner;

    int mSelectedGenreId;
    int mSelectedDurationId;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SearchListFragment newInstance(int columnCount, List<VideoDetailResponse> items) {
        SearchListFragment fragment = new SearchListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItems = new ArrayList<>();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        fetchSurveyFilters();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_item_list, container, false);


        // Set the adapter
        //if (view instanceof RecyclerView) {
        Context context = view.getContext();

        //search view
        mSearchView = (SearchView) view.findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Enter search term here...");

        /**
         * Code For Spinner
         */

        mGenreSpinner = (Spinner) view.findViewById(R.id.genre);
        mDurationSpinner = (Spinner) view.findViewById(R.id.duration);

        /**
         * Code for Recycler view
         */
        //list items
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        ;
        if (mColumnCount <= 1) {
            mLinearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(mLinearLayoutManager);
            recyclerView.addOnScrollListener(mScrollListener);
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        if (mAdapter == null) {
            mAdapter = new SearchListRecyclerViewAdapter(getContext(), mItems, mListener);
        }
        recyclerView.setAdapter(mAdapter);


        //}
        return view;
    }


    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView mRecyclerView, int newState) {
            super.onScrollStateChanged(mRecyclerView, newState);

            visibleItemCount = mRecyclerView.getChildCount(); // total items visible
            totalItemCount = mLinearLayoutManager.getItemCount(); //total current items
            previousVisibleItems = mLinearLayoutManager.findFirstVisibleItemPosition(); //scrolled item count

            //fetch new data when only 10 items left at bottom
            if (mItems.size() > 0 && (totalItemCount > 0 && totalItemCount - (visibleItemCount + previousVisibleItems) < Constants.ITEM_THRESHOLD) && (mTotalRecords > (pageIndex * 12))) {
                //getVideoList(pageIndex);
                Toast.makeText(getContext(), "Fetch Next Page Data", Toast.LENGTH_SHORT).show();
                searchData(false);
            }

        }

        @Override
        public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {
            super.onScrolled(mRecyclerView, dx, dy);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsViewVisible = isVisibleToUser;
        if (isVisibleToUser) {
            fetchSurveyFilters();
        } else {
            // fragment is no longer visible
        }
    }

    private void searchData(final boolean clearData) {
        if (mIsViewVisible) {
            new Thread() {
                public void run() {
                    APICalls.setSyncInterface(new SyncInterface() {
                        @Override
                        public void onAPIResult(int result, Object resultObject, int totalRecords) {
                            switch (result) {
                                case Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS:
//                                mSearchList = (List<VideoDetailResponse>) resultObject;
                                    List<VideoDetailResponse> res = (List<VideoDetailResponse>) resultObject;
                                    mTotalRecords = totalRecords;
                                    if (clearData) {
                                        mItems.clear();
                                        pageIndex = 1;
                                    } else {
                                        pageIndex++;
                                    }
                                    if (res.size() > 0) {
                                        mItems.addAll(res);
                                        mAdapter.notifyDataSetChanged();
                                    }

                                    break;
                                case Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY:
                                case Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE:
                                case Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR:
                                case Constants.ServiceResponseCodes.RESPONSE_CODE_UNAUTHORIZED_USER:
                                    break;
                                default:
                                    ProgressHandler.hideProgressDialogue();
                                    break;
                            }
                        }
                    });
                    //TODO: use this call for search
                    if (mSearchTerm != null) {
                        APICalls.fetchSearchResults(mSearchTerm, -1, -1, getContext(), pageIndex);
                    }
                }
            }.start();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchTerm = query;
        searchData(true);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
//        mSearchTerm = newText;
//        searchData(true);
        return false;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(VideoDetailResponse item);
    }

    private void fetchSurveyFilters() {
        if (mIsViewVisible) {
            if (mGenreList == null || mGenreList.size() == 0 || mDurationList == null || mDurationList.size() == 0) {
                ProgressHandler.showProgressDialog(getContext(), getString(R.string.app_name), "Loading...", 0, Constants.ProgressBarStyles.PROGRESS_BAR_ANIMATED, "", "");
            }
            new Thread() {
                public void run() {
                    APICalls.setSyncInterface(new SyncInterface() {
                        @Override
                        public void onAPIResult(int result, Object resultObject, int totalRecords) {
                            switch (result) {
                                case Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS:
                                    HashMap<String, Object> searchOptions = new HashMap<>();
                                    mGenreList = (List<GenreListResponse>) searchOptions.get("categories");
                                    mDurationList = (List<DurationResponse>) searchOptions.get("durations");
                                    setSearchFilters();
                                    ProgressHandler.hideProgressDialogue();
                                    break;
                                case Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY:
                                case Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE:
                                case Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR:
                                case Constants.ServiceResponseCodes.RESPONSE_CODE_UNAUTHORIZED_USER:
                                    break;
                                default:
                                    ProgressHandler.hideProgressDialogue();
                                    break;
                            }
                        }
                    });
                    //TODO: use this call for search
                    if (mSearchTerm != null) {
                        APICalls.getCategoryList(getContext());
                    }
                }
            }.start();
        }
    }

    private void setSearchFilters() {

        // Create an ArrayAdapter using the string array and a default mGenreSpinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.genre_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the mGenreSpinner
        mGenreSpinner.setAdapter(adapter);
//        mGenreSpinner.setLayoutMode(La);
        mGenreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.duration_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDurationSpinner.setAdapter(adapter2);
        mDurationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
