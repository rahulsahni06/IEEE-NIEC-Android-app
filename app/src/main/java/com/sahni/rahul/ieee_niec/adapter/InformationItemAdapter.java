package com.sahni.rahul.ieee_niec.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.target.Target;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.glide.GlideApp;
import com.sahni.rahul.ieee_niec.interfaces.OnSharedElementClickListener;
import com.sahni.rahul.ieee_niec.models.Information;

import java.util.ArrayList;

/**
 * Created by sahni on 27-Aug-17.
 */

public class InformationItemAdapter extends RecyclerView.Adapter<InformationItemAdapter.InfoViewHolder> {

    private static final String TAG = "InformationItemAdapter";
    private Context mContext;
    private ArrayList<Information> mArrayList;
    private OnSharedElementClickListener mListener;

    public InformationItemAdapter(Context mContext, ArrayList<Information> mArrayList, OnSharedElementClickListener listener) {
        this.mContext = mContext;
        this.mArrayList = mArrayList;
        this.mListener = listener;
    }

    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.info_item_layout, parent, false);
        return new InfoViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(final InfoViewHolder holder, int position) {
        Information info = mArrayList.get(position);
        holder.textView.setText(info.getTitle());
        holder.dateTextView.setText(info.getDate());
        ViewCompat.setTransitionName(holder.cardView, info.getTitle());
        if (info.getImageList() != null) {
            String firstImageUrl = info.getImageList().get(0);
            if(firstImageUrl != null &&
                    (!firstImageUrl.equals("null"))) {
                holder.imageView.setVisibility(View.VISIBLE);
                GlideApp.with(mContext)
                        .load(firstImageUrl)
                        .placeholder(R.drawable.place)
                        .error(R.drawable.place)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .into(holder.imageView);
            } else {
                holder.imageView.setVisibility(View.GONE);
            }
        } else {
            holder.imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    static class InfoViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;
        private TextView dateTextView;
        private CardView cardView;


        public InfoViewHolder(View itemView, final OnSharedElementClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.info_image_view);
            textView = itemView.findViewById(R.id.info_text_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            cardView = itemView.findViewById(R.id.info_card_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onSharedElementClicked(view, cardView);
                    }
                }
            });
        }
    }
}
