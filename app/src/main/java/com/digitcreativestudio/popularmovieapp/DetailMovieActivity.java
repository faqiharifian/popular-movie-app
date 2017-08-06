package com.digitcreativestudio.popularmovieapp;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitcreativestudio.popularmovieapp.adapter.ReviewAdapter;
import com.digitcreativestudio.popularmovieapp.adapter.VideoAdapter;
import com.digitcreativestudio.popularmovieapp.connection.TmdbClient;
import com.digitcreativestudio.popularmovieapp.connection.TmdbService;
import com.digitcreativestudio.popularmovieapp.db.DBContract;
import com.digitcreativestudio.popularmovieapp.entity.Movie;
import com.digitcreativestudio.popularmovieapp.entity.Review;
import com.digitcreativestudio.popularmovieapp.entity.Video;
import com.digitcreativestudio.popularmovieapp.parser.ReviewParser;
import com.digitcreativestudio.popularmovieapp.parser.VideoParser;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMovieActivity extends AppCompatActivity {
    Movie mMovie;
    ArrayList<Video> videos = new ArrayList<>();
    ArrayList<Review> reviews = new ArrayList<>();
    VideoAdapter videoAdapter;
    ReviewAdapter reviewAdapter;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.backdrop_imageview) ImageView backdropImageView;
    @BindView(R.id.poster_imageview) ImageView posterImageView;
    @BindView(R.id.title_textview) TextView titleTextView;
    @BindView(R.id.release_textview) TextView releaseTextView;
    @BindView(R.id.rate_textview) TextView rateTextView;
    @BindView(R.id.overview_textview) TextView overviewTextView;
    @BindView(R.id.videos_recyclerview) RecyclerView videosRecyclerView;
    @BindView(R.id.review_recyclerview) RecyclerView reviewRecyclerView;

    @BindColor(android.R.color.transparent) int transparent;

    RecyclerView.LayoutManager videoLayoutManager, reviewLayoutManager;

    boolean isFavorite = false;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMovie = getIntent().getParcelableExtra("movie");

        if(savedInstanceState != null){
            videos = (ArrayList<Video>) savedInstanceState.getSerializable("videos");
            reviews = (ArrayList<Review>) savedInstanceState.getSerializable("reviews");
        }

        ButterKnife.bind(this);

        collapsingToolbarLayout.setTitle(mMovie.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(transparent);

        titleTextView.setText(mMovie.getTitle());
        rateTextView.setText(String.valueOf(mMovie.getVoteAverage()));
        releaseTextView.setText(dateFormat.format(mMovie.getReleaseDate()));
        overviewTextView.setText(mMovie.getOverview());

        videoLayoutManager = new GridLayoutManager(this, 3);
        if(getResources().getConfiguration().orientation == 1){
            videoLayoutManager = new GridLayoutManager(this, 2);
        }
        videosRecyclerView.setLayoutManager(videoLayoutManager);
        videoAdapter = new VideoAdapter(this, videos, new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Video video) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video.getKey()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            }
        });
        videosRecyclerView.setAdapter(videoAdapter);
        videosRecyclerView.addItemDecoration(new SpaceItemDecorator(this, getResources().getDimensionPixelSize(R.dimen.small_margin), 3));
        if(getResources().getConfiguration().orientation == 1) {
            videosRecyclerView.addItemDecoration(new SpaceItemDecorator(this, getResources().getDimensionPixelSize(R.dimen.small_margin), 2));
        }

        reviewLayoutManager = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);
        reviewAdapter = new ReviewAdapter(this, reviews);
        reviewRecyclerView.setAdapter(reviewAdapter);

        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w92"+mMovie.getPosterPath())
                .into(posterImageView);

        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w300"+mMovie.getBackdropPath())
                .fit()
                .centerCrop()
                .into(backdropImageView);

        if(savedInstanceState == null) {
            getVideos();
            getReviews();
        }

        Cursor cursor = getContentResolver().query(DBContract.FavoriteEntry.appendId(mMovie.getId()), null, null, null, null);
        if(cursor != null) {
            isFavorite = cursor.getCount() > 0;
            cursor.close();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movie", mMovie);
        outState.putSerializable("videos", videos);
        outState.putSerializable("reviews", reviews);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        this.menu = menu;
        menu.getItem(0).setVisible(isFavorite);
        menu.getItem(1).setVisible(!isFavorite);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_favorite_on:
                getContentResolver().delete(DBContract.FavoriteEntry.appendId(mMovie.getId()), null, null);
                isFavorite = false;
                break;
            case R.id.action_favorite_off:
                ContentValues cv = new ContentValues();
                cv.put(DBContract.FavoriteEntry._ID, mMovie.getId());
                cv.put(DBContract.FavoriteEntry.COLUMN_TITLE, mMovie.getTitle());
                cv.put(DBContract.FavoriteEntry.COLUMN_POSTER_PATH, mMovie.getPosterPath());
                cv.put(DBContract.FavoriteEntry.COLUMN_BACKDROP_PATH, mMovie.getBackdropPath());
                cv.put(DBContract.FavoriteEntry.COLUMN_RELEASE_DATE, mMovie.getOriginalReleaseDate());
                cv.put(DBContract.FavoriteEntry.COLUMN_OVERVIEW, mMovie.getOverview());
                cv.put(DBContract.FavoriteEntry.COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());
                getContentResolver().insert(DBContract.FavoriteEntry.getUri(), cv);
                isFavorite = true;
                break;
        }
        menu.getItem(0).setVisible(isFavorite);
        menu.getItem(1).setVisible(!isFavorite);
        return true;
    }

    private void getVideos(){
        TmdbService service = TmdbClient.getClient().create(TmdbService.class);

        Call<VideoParser> call = service.getVideos(mMovie.getId());
        call.enqueue(new Callback<VideoParser>() {
            @Override
            public void onResponse(Call<VideoParser> call, Response<VideoParser> response) {
                if(response.code() == 200) {
                    List<Video> videos = response.body().getVideos();
                    DetailMovieActivity.this.videos.clear();
                    DetailMovieActivity.this.videos.addAll(videos);

                    videoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<VideoParser> call, Throwable t) {

            }
        });
    }

    private void getReviews(){
        TmdbService service = TmdbClient.getClient().create(TmdbService.class);

        Call<ReviewParser> call = service.getReviews(mMovie.getId());
        call.enqueue(new Callback<ReviewParser>() {
            @Override
            public void onResponse(Call<ReviewParser> call, Response<ReviewParser> response) {
                if(response.code() == 200) {
                    List<Review> reviews = response.body().getReviews();
                    DetailMovieActivity.this.reviews.clear();
                    DetailMovieActivity.this.reviews.addAll(reviews);

                    reviewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ReviewParser> call, Throwable t) {

            }
        });
    }
}
