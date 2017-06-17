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
import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.Sync.APICalls;
import com.shortly.shortlyapp.model.WatchLaterResponse;
import com.shortly.shortlyapp.utils.Constants;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class WatchLaterFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_WATCH_LATER_RESPONE = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private static List<WatchLaterResponse> mItems;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;
    WatchLaterRecyclerViewAdapter mAdapter;

    boolean mIsVisibleToUser;

    int mTotalRecords = 0;
    int pageIndex = 1;
    int previousVisibleItems, visibleItemCount, totalItemCount; //infinite scroll

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WatchLaterFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static WatchLaterFragment newInstance(int columnCount, List<WatchLaterResponse> items) {
        WatchLaterFragment fragment = new WatchLaterFragment();
        Bundle args = new Bundle();
        mItems = items;
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);

        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watch_later_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mLinearLayoutManager = new LinearLayoutManager(context);
                mRecyclerView.setLayoutManager(mLinearLayoutManager);
                mRecyclerView.addOnScrollListener(mScrollListener);
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            //TODO: pass data here from api
            if (mAdapter == null) {
                mAdapter = new WatchLaterRecyclerViewAdapter(getContext(), mItems, mListener);
            }
            mRecyclerView.setAdapter(mAdapter);
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

            //fetch new data when only 3 items left at bottom
            if ((totalItemCount > 0 && totalItemCount - (visibleItemCount + previousVisibleItems) < Constants.ITEM_THRESHOLD) && (mTotalRecords > (pageIndex * 10))) {
                Toast.makeText(getContext(), "Fetch Next Page Data", Toast.LENGTH_SHORT).show();
                getWatchLaterList(false);
            }
        }

        @Override
        public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {
            super.onScrolled(mRecyclerView, dx, dy);
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mIsVisibleToUser = true;
            getWatchLaterList(true);
        } else {
            mIsVisibleToUser = false;
            // fragment is no longer visible
        }
    }


    private void getWatchLaterList(final boolean clearData) {
        if (mIsVisibleToUser) {
            new Thread() {
                public void run() {
                    APICalls.setSyncInterface(new SyncInterface() {
                        @Override
                        public void onAPIResult(int result, Object resultObject, int totalRecords) {
                            switch (result) {
                                case Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS:
                                    List<WatchLaterResponse> res = (List<WatchLaterResponse>) resultObject;

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
                                    break;
                            }
                        }
                    });
                    APICalls.getWatchLaterVideos(pageIndex, getContext(), true);
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
        void onListFragmentInteraction(WatchLaterResponse item);
    }
}
