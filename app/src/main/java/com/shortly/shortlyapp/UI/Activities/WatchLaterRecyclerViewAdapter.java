package com.shortly.shortlyapp.UI.Activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.UI.Activities.ItemFragment.OnListFragmentInteractionListener;
import com.shortly.shortlyapp.UI.Activities.dummy.DummyContent.DummyItem;
import com.shortly.shortlyapp.model.WatchLaterResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class WatchLaterRecyclerViewAdapter extends RecyclerView.Adapter<WatchLaterRecyclerViewAdapter.ViewHolder> {

    private final List<WatchLaterResponse> mValues;
    private static Context mContext;
    private final WatchLaterFragment.OnListFragmentInteractionListener mListener;

    public WatchLaterRecyclerViewAdapter(Context context, List<WatchLaterResponse> items, WatchLaterFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mContext = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_watch_later_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //TODO: get data by position and load in cell
        WatchLaterResponse videoResponse = mValues.get(position);
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(videoResponse.getTitle());
        holder.mContentView.setText("by: " + videoResponse.getCasts());
        Picasso.with(mContext).load(videoResponse.getThumbnails()).into(holder.mImageView);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //TODO: set ids in layout and cast here and set data.
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;

        public WatchLaterResponse mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.title);
            mImageView = (ImageView) view.findViewById(R.id.img_view_thumbnail);
            mContentView = (TextView) view.findViewById(R.id.cast);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
