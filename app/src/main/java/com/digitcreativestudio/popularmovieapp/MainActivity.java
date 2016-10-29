package com.digitcreativestudio.popularmovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.digitcreativestudio.popularmovieapp.adapter.MovieAdapter;
import com.digitcreativestudio.popularmovieapp.connection.TmdbClient;
import com.digitcreativestudio.popularmovieapp.connection.TmdbService;
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

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
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
        movieRV.setAdapter(movieAdapter);

        getMovieList();
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
                mType = "toprated";
                setTitle(topRatedTitle);
                getMovieList();
                break;
        }

        return true;
    }

    private void getMovieList(){
        movieList.clear();
        movieAdapter.notifyDataSetChanged();
        TmdbService tmdbService =
                TmdbClient.getClient().create(TmdbService.class);

        Call<MovieListParser> movieListCall;
        if (mType.equals("toprated")) {
            movieListCall = tmdbService.getTopRated();
        } else {
            movieListCall = tmdbService.getPopular();
        }

        movieListCall.enqueue(new Callback<MovieListParser>() {
            @Override
            public void onResponse(Call<MovieListParser> call, Response<MovieListParser> response) {
                if(response.code() == 200) {
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
