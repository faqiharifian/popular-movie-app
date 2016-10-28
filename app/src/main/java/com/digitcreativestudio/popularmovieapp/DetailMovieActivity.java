package com.digitcreativestudio.popularmovieapp;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitcreativestudio.popularmovieapp.entity.Movie;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailMovieActivity extends AppCompatActivity {
    Movie mMovie;

    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.backdrop_imageview) ImageView backdropImageView;
    @BindView(R.id.title_textview) TextView titleTextView;
    @BindView(R.id.rate_textview) TextView rateTextView;
    @BindView(R.id.overview_textview) TextView overviewTextView;

    @BindColor(android.R.color.transparent) int transparent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMovie = getIntent().getParcelableExtra("movie");

        ButterKnife.bind(this);

        collapsingToolbarLayout.setTitle(mMovie.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(transparent);

        titleTextView.setText(mMovie.getTitle());
        rateTextView.setText(String.valueOf(mMovie.getVoteAverage()));
        overviewTextView.setText(mMovie.getOverview());
    }

}
