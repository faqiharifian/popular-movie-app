package com.digitcreativestudio.popularmovieapp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by faqih on 04/08/17.
 */

public class Review {
    @SerializedName("id")
    String id;
    @SerializedName("author")
    String author;
    @SerializedName("content")
    String content;
    @SerializedName("url")
    String url;

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
