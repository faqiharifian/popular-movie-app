package com.digitcreativestudio.popularmovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.digitcreativestudio.popularmovieapp.adapter.MovieAdapter;
import com.digitcreativestudio.popularmovieapp.entity.Movie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.movie_recyclerview) RecyclerView movieRV;

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
                Toast.makeText(MainActivity.this, "Movie clicked "+movie.getTitle(), Toast.LENGTH_SHORT).show();
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
                getMovieList();
                break;
            case R.id.sort_toprated:
                mType = "toprated";
                getMovieList();
                break;
        }

        return true;
    }

    private void getMovieList(){
        movieList.clear();
        movieAdapter.notifyDataSetChanged();
        if(mType.equals("toprated")){
            for(int i = 0; i < 50; i++){
                Movie movie = new Movie();
                movie.setTitle("Title movie "+i+" TOP RATED");
                movie.setPosterPath("Poster path "+i+" TOP RATED");
                movie.setBackdropPath("Backdrop path "+i+" TOP RATED");
                movie.setOverview("Overview movie "+i+" TOP RATED");
                movie.setVoteCount(i);
                movie.setVoteAverage(i);
                movieList.add(movie);
            }
        }else{
            for(int i = 0; i < 50; i++){
                Movie movie = new Movie();
                movie.setTitle("Title movie "+i+" POPULAR");
                movie.setPosterPath("Poster path "+i+" POPULAR");
                movie.setBackdropPath("Backdrop path "+i+" POPULAR");
                movie.setOverview("Overview movie "+i+" POPULAR");
                movie.setVoteCount(i);
                movie.setVoteAverage(i);
                movieList.add(movie);
            }
        }
        movieAdapter.notifyDataSetChanged();
    }
}
