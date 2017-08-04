package com.digitcreativestudio.popularmovieapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.digitcreativestudio.popularmovieapp.db.DBContract.*;

/**
 * Created by faqih on 04/08/17.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movie.db";

    private static final String SQL_CREATE_FAVORITE =
            "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +
                    FavoriteEntry._ID + " INTEGER PRIMARY KEY," +
                    FavoriteEntry.COLUMN_TITLE + " TEXT," +
                    FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NULLABLE," +
                    FavoriteEntry.COLUMN_BACKDROP_PATH + " TEXT NULLABLE," +
                    FavoriteEntry.COLUMN_RELEASE_DATE + " TEXT NULLABLE," +
                    FavoriteEntry.COLUMN_OVERVIEW + " TEXT NULLABLE," +
                    FavoriteEntry.COLUMN_VOTE_AVERAGE + " TEXT NULLABLE)";

    private static final String SQL_DELETE_FAVORITE =
            "DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_FAVORITE);
        onCreate(db);
    }
}
