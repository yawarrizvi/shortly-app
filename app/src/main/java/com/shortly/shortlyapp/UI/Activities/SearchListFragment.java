package com.shortly.shortlyapp.UI.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shortly.shortlyapp.Interfaces.SyncInterface;
import com.shortly.shortlyapp.Logic.ProgressHandler.ProgressHandler;
import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.Sync.APICalls;
import com.shortly.shortlyapp.model.VideoDetailResponse;
import com.shortly.shortlyapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SearchListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private  List<VideoDetailResponse> mItems;
    SearchListRecyclerViewAdapter mAdapter;

    int mTotalRecords = 0;
    int pageIndex = 2;
    int previousVisibleItems, visibleItemCount, totalItemCount; //infinite scroll

    LinearLayoutManager mLinearLayoutManager;


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
//        searchData();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
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

        }
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
            if ((totalItemCount > 0 && totalItemCount - (visibleItemCount + previousVisibleItems) < Constants.ITEM_THRESHOLD) && (mTotalRecords > (pageIndex * 12))) {
                //getVideoList(pageIndex);
                Toast.makeText(getContext(), "Fetch Next Page Data", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {
            super.onScrolled(mRecyclerView, dx, dy);
        }
    };

    private void searchData(final String searchTerm) {

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
                                if (res.size() > 0) {
                                    mItems.addAll(res);
                                    pageIndex++;
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
                if (searchTerm != null) {
                    APICalls.fetchSearchResults(searchTerm, -1, -1, getContext(), 1);
                }
            }
        }.start();
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
}
