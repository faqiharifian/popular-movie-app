package com.digitcreativestudio.popularmovieapp.parser;

import com.digitcreativestudio.popularmovieapp.entity.Movie;
import com.digitcreativestudio.popularmovieapp.entity.Video;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by faqiharifian on 30/09/16.
 */
public class MovieParser extends Movie {
    @SerializedName("videos")
    private VideoParser videos;

    public List<Video> getVideos() {
        return videos.getVideos();
    }

}
