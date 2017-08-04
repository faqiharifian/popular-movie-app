package com.digitcreativestudio.popularmovieapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.digitcreativestudio.popularmovieapp.R;
import com.digitcreativestudio.popularmovieapp.entity.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by faqih on 04/08/17.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>{
    private ArrayList<Video> videos;
    private Context context;
    private OnItemClickListener listener;

    public VideoAdapter(Context context, ArrayList<Video> videos, VideoAdapter.OnItemClickListener listener) {
        this.context = context;
        this.videos = videos;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Video video = videos.get(position);
        Picasso.with(context)
                .load(video.getImageUrl())
                .into(holder.videoImageView);
        holder.bind(video, listener);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Video video);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View parentView;
        @BindView(R.id.video_imageview) ImageView videoImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            parentView = itemView;
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void bind(final Video item, final OnItemClickListener listener) {
            parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
