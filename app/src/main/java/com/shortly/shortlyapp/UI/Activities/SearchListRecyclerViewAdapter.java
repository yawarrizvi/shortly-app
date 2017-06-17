package com.shortly.shortlyapp.UI.Activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.UI.Activities.SearchListFragment.OnListFragmentInteractionListener;
import com.shortly.shortlyapp.UI.Activities.dummy.DummyContent.DummyItem;
import com.shortly.shortlyapp.model.VideoDetailResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SearchListRecyclerViewAdapter extends RecyclerView.Adapter<SearchListRecyclerViewAdapter.ViewHolder> {

    private final List<VideoDetailResponse> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;

    public SearchListRecyclerViewAdapter(Context context, List<VideoDetailResponse> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mViewTitle.setText(holder.mItem.getTitle());
        holder.mViewGenre.setText(holder.mItem.getCategory());
        Picasso.with(mContext).load(holder.mItem.getThumbnails()).into(holder.mImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mValues == null){
            return  0;
        }
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mViewTitle;
        public final TextView mViewGenre;
        public final ImageView mImageView;
        public VideoDetailResponse mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mViewTitle = (TextView) view.findViewById(R.id.title);
            mViewGenre = (TextView) view.findViewById(R.id.video_genre);
            mImageView = (ImageView) view.findViewById(R.id.img_view_thumbnail);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mViewGenre.getText() + "'";
        }
    }
}
