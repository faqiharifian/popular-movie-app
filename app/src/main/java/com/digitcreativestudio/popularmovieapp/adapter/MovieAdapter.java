package com.digitcreativestudio.popularmovieapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.digitcreativestudio.popularmovieapp.R;
import com.digitcreativestudio.popularmovieapp.entity.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by faqiharifian on 28/10/16.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private ArrayList<Movie> arrayList;
    private OnItemClickListener listener;
    Context context;

    public MovieAdapter() {
    }


    public MovieAdapter(Context context, ArrayList<Movie> arrayList, OnItemClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Movie item = arrayList.get(position);
        holder.bind(arrayList.get(position), listener);

        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w92"+item.getPosterPath())
                .into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View parentView;
        @BindView(R.id.movie_poster_imageview) ImageView poster;

        public MyViewHolder(View view) {
            super(view);
            parentView = view;
            try {
                ButterKnife.bind(this, view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void bind(final Movie item, final OnItemClickListener listener) {
            parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

        @Override
        public void onClick(View v) {
        }
    }
}