package com.digitcreativestudio.popularmovieapp.db;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by faqih on 04/08/17.
 */

public final class DBContract {
    public static final String PROVIDER_NAME = "com.digitcreativestudio.popularmovieapp";
    public static final String URL = "content://" + PROVIDER_NAME;
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static class FavoriteEntry implements BaseColumns{
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static Uri getUri(){
            return CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        }

        public static Uri appendId(long id){
            return ContentUris.withAppendedId(CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build(), id);
        }

        public static String getId(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }
}
