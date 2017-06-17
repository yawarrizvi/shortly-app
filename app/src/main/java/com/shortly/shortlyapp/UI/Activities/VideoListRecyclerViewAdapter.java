package com.shortly.shortlyapp.UI.Activities;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
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

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class VideoListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    FragmentManager mFM;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_VIEW_PAGER = 1;
    private static final int TYPE_LIST_ITEM_TOP = 2;
    private static final int TYPE_LIST_ITEM = 3;

    private final ArrayList<Object>  mValues;
    private final VideoListFragment.OnListFragmentInteractionListener mListener;
    private static Context mContext;

    public VideoListRecyclerViewAdapter(Context context, FragmentManager fm, ArrayList<Object> items, VideoListFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mContext = context;
        mFM = fm;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case TYPE_HEADER:
                View headerView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_video_header, parent, false);
                viewHolder = new ListItemHeader(headerView);
                break;
            case TYPE_VIEW_PAGER:
                View pagerView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_video_pager, parent, false);
                viewHolder = new ListItemPager(pagerView);
                break;
            case TYPE_LIST_ITEM_TOP:
                View listItemTop = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_video_item_top, parent, false);
                viewHolder = new ListItemTop(listItemTop);
                break;
            default:
                View listItem = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_video_item, parent, false);
                viewHolder = new ListItemHolder(listItem);
                break;
        }

        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case TYPE_HEADER:
                ListItemHeader itemHeader = (ListItemHeader) holder;
                bindListItemHeader(itemHeader,position);
                break;
            case TYPE_VIEW_PAGER:
                ListItemPager itemPager = (ListItemPager) holder;
                bindListPager(itemPager,position);
                break;
            case TYPE_LIST_ITEM_TOP:
                ListItemTop itemListTop = (ListItemTop) holder;
                bindListItemTop(itemListTop,position);
                break;
            default:
                ListItemHolder itemList = (ListItemHolder) holder;
                bindListItem(itemList,position);
                break;
        }

        //final ListItemHolder itemHolder = (ListItemHolder) holder;

    }

    @Override
    public int getItemCount() {
        if(mValues == null){
          return  0;
        }

        return mValues.size();
    }


    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_HEADER;
        } if(position == 1){
            return  TYPE_VIEW_PAGER;
        } if(position == 2){
            return  TYPE_LIST_ITEM_TOP;
        } else {
            return  TYPE_LIST_ITEM;
        }
    }
    /**
     * Item Holder for header
     */
    public class ListItemHeader extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;
        public VideoDetailResponse mItem;

        public ListItemHeader(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.title);
            mContentView = (TextView) view.findViewById(R.id.cast);
            mImageView = (ImageView) view.findViewById(R.id.img_view_thumbnail);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    private void bindListItemHeader(final ListItemHeader holder, int position) {

        holder.mItem = (VideoDetailResponse) mValues.get(position);
        holder.mIdView.setText(holder.mItem.getTitle());
        if (holder.mItem.getCasts() != null && !holder.mItem.getCasts().isEmpty()) {
            holder.mContentView.setText("by: " + holder.mItem.getCasts());
        }
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


    /**
     * Item Holder for pager
     */
    public class ListItemPager extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ViewPager mPager;
        public final ImageView mImageView;
        public List<VideoDetailResponse> mItem;

        public ListItemPager(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mPager = (ViewPager) view.findViewById(R.id.pager_Item);
            mImageView = (ImageView) view.findViewById(R.id.img_view_thumbnail);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    private void bindListPager(final ListItemPager holder, int position) {

        holder.mItem = (List<VideoDetailResponse>) mValues.get(position);
//        holder.mViewTitle.setText(mValues.get(position).id);
//        holder.mViewGenre.setText(mValues.get(position).content);
        holder.mPager.setAdapter(new MoviesViewPagerAdapter(mFM, holder.mItem));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    //mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

    }


    /**
     * Item Holder for topItem
     */
    public class ListItemTop extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;
        public VideoDetailResponse mItem;

        public ListItemTop(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.title);
            mContentView = (TextView) view.findViewById(R.id.cast);
            mImageView = (ImageView) view.findViewById(R.id.img_view_thumbnail);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    private void bindListItemTop(final ListItemTop holder, int position) {

        holder.mItem = (VideoDetailResponse) mValues.get(position);
        holder.mIdView.setText(holder.mItem.getTitle());
        if (holder.mItem.getCasts() != null && !holder.mItem.getCasts().isEmpty()) {
            holder.mContentView.setText("by: " + holder.mItem.getCasts());
        }
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

    /**
     * Item Holder for list/default item
     */
    public class ListItemHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public VideoDetailResponse mItem;
        public final ImageView mImageView;

        public ListItemHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.title);
            mContentView = (TextView) view.findViewById(R.id.cast);
            mImageView = (ImageView) view.findViewById(R.id.img_view_thumbnail);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    private void bindListItem(final ListItemHolder holder, int position) {
        holder.mItem = (VideoDetailResponse) mValues.get(position);
        holder.mIdView.setText(holder.mItem.getTitle());
        if (holder.mItem.getCasts() != null && !holder.mItem.getCasts().isEmpty()) {
            holder.mContentView.setText("by: " + holder.mItem.getCasts());
        }
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
}
