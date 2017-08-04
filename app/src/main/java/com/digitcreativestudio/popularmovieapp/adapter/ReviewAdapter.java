package com.digitcreativestudio.popularmovieapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digitcreativestudio.popularmovieapp.R;
import com.digitcreativestudio.popularmovieapp.entity.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by faqih on 04/08/17.
 */

public class ReviewAdapter extends  RecyclerView.Adapter<ReviewAdapter.ViewHolder>{
    ArrayList<Review> reviews;
    Context context;

    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.author_textview)
        TextView author;
        @BindView(R.id.content_textview)
        TextView content;
        public ViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
