package com.digitcreativestudio.popularmovieapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.digitcreativestudio.popularmovieapp.adapter.MovieAdapter;
import com.digitcreativestudio.popularmovieapp.connection.TmdbClient;
import com.digitcreativestudio.popularmovieapp.connection.TmdbService;
import com.digitcreativestudio.popularmovieapp.db.DBContract;
import com.digitcreativestudio.popularmovieapp.entity.Movie;
import com.digitcreativestudio.popularmovieapp.parser.MovieListParser;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.movie_recyclerview) RecyclerView movieRV;

    @BindString(R.string.popular_title) String popularTitle;
    @BindString(R.string.toprated_title) String topRatedTitle;
    @BindString(R.string.favorite_title) String favoriteTitle;

    private RecyclerView.LayoutManager mLayoutManager;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieList= new ArrayList<>();

    private String mType = "popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        movieList = new ArrayList<>();

        if(savedInstanceState != null){
            movieList = (ArrayList<Movie>) savedInstanceState.getSerializable("movies");
            mType = savedInstanceState.getString("type");
            Log.e("type", mType);
        }

        mLayoutManager = new GridLayoutManager(this, 3);
        if(getResources().getConfiguration().orientation == 1){
            mLayoutManager = new GridLayoutManager(this, 2);
        }
        movieRV.setLayoutManager(mLayoutManager);
        movieRV.setItemAnimator(new DefaultItemAnimator());
        movieAdapter = new MovieAdapter(MainActivity.this, movieList, new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                Intent intent = new Intent(MainActivity.this, DetailMovieActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });
        movieRV.addItemDecoration(new SpaceItemDecorator(this, getResources().getDimensionPixelSize(R.dimen.small_margin), 2));
        movieRV.setAdapter(movieAdapter);

        if(savedInstanceState == null){
            getMovieList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mType.equals("favorite"))
            getMovieList();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("movies", movieList);
        outState.putString("type", mType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort_popular:
                mType = "popular";
                setTitle(popularTitle);
                getMovieList();
                break;
            case R.id.sort_toprated:
                mType = "top_rated";
                setTitle(topRatedTitle);
                getMovieList();
                break;
            case R.id.sort_favorite:
                mType = "favorite";
                setTitle(favoriteTitle);
                getMovieList();
                break;
        }

        return true;
    }

    private void getMovieList(){
        movieList.clear();
        movieAdapter.notifyDataSetChanged();
        if(mType.equals("favorite")){
            Cursor cursor = getContentResolver().query(DBContract.FavoriteEntry.getUri(), null, null, null, null);
            if(cursor != null){
                while (cursor.moveToNext()){
                    movieList.add(new Movie(
                            cursor.getInt(cursor.getColumnIndex(DBContract.FavoriteEntry._ID)),
                            cursor.getString(cursor.getColumnIndex(DBContract.FavoriteEntry.COLUMN_TITLE)),
                            cursor.getString(cursor.getColumnIndex(DBContract.FavoriteEntry.COLUMN_POSTER_PATH)),
                            cursor.getString(cursor.getColumnIndex(DBContract.FavoriteEntry.COLUMN_BACKDROP_PATH)),
                            cursor.getString(cursor.getColumnIndex(DBContract.FavoriteEntry.COLUMN_RELEASE_DATE)),
                            cursor.getString(cursor.getColumnIndex(DBContract.FavoriteEntry.COLUMN_OVERVIEW)),
                            cursor.getFloat(cursor.getColumnIndex(DBContract.FavoriteEntry.COLUMN_VOTE_AVERAGE))
                    ));
                }
                cursor.close();
            }
            movieAdapter.notifyDataSetChanged();
        }else {
            TmdbService tmdbService =
                    TmdbClient.getClient().create(TmdbService.class);

            Call<MovieListParser> movieListCall;
            if (mType.equals("top_rated")) {
                movieListCall = tmdbService.getTopRated();
            } else {
                movieListCall = tmdbService.getPopular();
            }

            movieListCall.enqueue(new Callback<MovieListParser>() {
                @Override
                public void onResponse(Call<MovieListParser> call, Response<MovieListParser> response) {
                    if (response.code() == 200) {
                        movieList.addAll(response.body().getMovies());

                        movieAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<MovieListParser> call, Throwable t) {
                }
            });
        }
    }
}
