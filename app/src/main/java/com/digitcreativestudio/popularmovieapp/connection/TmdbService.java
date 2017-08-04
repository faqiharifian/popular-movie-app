package com.digitcreativestudio.popularmovieapp.connection;

import com.digitcreativestudio.popularmovieapp.BuildConfig;
import com.digitcreativestudio.popularmovieapp.parser.MovieListParser;
import com.digitcreativestudio.popularmovieapp.parser.ReviewParser;
import com.digitcreativestudio.popularmovieapp.parser.VideoParser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

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

    @Headers("Content-Type: application/json")
    @GET("movie/{id}/videos?api_key="+API_KEY)
    Call<VideoParser> getVideos(@Path("id") long id);

    @Headers("Content-Type: application/json")
    @GET("movie/{id}/reviews?api_key="+API_KEY)
    Call<ReviewParser> getReviews(@Path("id") long id);
}
