package com.digitcreativestudio.popularmovieapp.parser;

import com.digitcreativestudio.popularmovieapp.entity.Review;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by faqih on 04/08/17.
 */

public class ReviewParser {
    @SerializedName("page")
    int page;
    @SerializedName("total_pages")
    int totalPages;
    @SerializedName("total_results")
    int totalResults;
    @SerializedName("results")
    List<Review> reviews;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<Review> getReviews() {
        return reviews;
    }
}
