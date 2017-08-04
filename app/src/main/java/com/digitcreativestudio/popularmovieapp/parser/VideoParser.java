package com.digitcreativestudio.popularmovieapp.parser;

import com.digitcreativestudio.popularmovieapp.entity.Video;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by faqiharifian on 29/09/16.
 */
public class VideoParser {
    @SerializedName("results")
    private List<Video> videos;

    public List<Video> getVideos() {
        return videos;
    }
}
