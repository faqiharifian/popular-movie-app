package com.digitcreativestudio.popularmovieapp;

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
    private MovieAdapter adapter;
    private ArrayList<Movie> movieList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        movieList = new ArrayList<>();

        for(int i = 0; i < 50; i++){
            Movie movie = new Movie();
            movie.setTitle("Title movie "+i);
            movie.setPosterPath("Poster path "+i);
            movie.setBackdropPath("Backdrop path "+i);
            movie.setOverview("Overview movie "+i);
            movie.setVoteCount(i);
            movie.setVoteAverage(i);
            movieList.add(movie);
        }

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        movieRV.setLayoutManager(mLayoutManager);
        movieRV.setItemAnimator(new DefaultItemAnimator());
        adapter = new MovieAdapter(MainActivity.this, movieList, new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                Toast.makeText(MainActivity.this, "Movie clicked "+movie.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        movieRV.setAdapter(adapter);
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
                Toast.makeText(this, "popular", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sort_toprated:
                Toast.makeText(this, "top rated", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }
}
