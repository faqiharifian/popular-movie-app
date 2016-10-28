package com.digitcreativestudio.popularmovieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.movie_recyclerview) RecyclerView movieRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
