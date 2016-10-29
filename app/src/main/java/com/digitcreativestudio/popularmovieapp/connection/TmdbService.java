package com.digitcreativestudio.popularmovieapp.connection;

import com.digitcreativestudio.popularmovieapp.BuildConfig;
import com.digitcreativestudio.popularmovieapp.parser.MovieListParser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by faqiharifian on 23/09/16.
 */
public interface TmdbService {
    String API_KEY = BuildConfig.TMDB_API_KEY;
    
    @Headers("Content-Type: application/json")
    @GET("movie/popular?api_key="+API_KEY)
    Call<MovieListParser> getPopular();

    @Headers("Content-Type: application/json")
    @GET("movie/top_rated?api_key="+API_KEY)
    Call<MovieListParser> getTopRated();
}
