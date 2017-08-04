package com.digitcreativestudio.popularmovieapp.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.digitcreativestudio.popularmovieapp.db.DBContract.FavoriteEntry;

import static com.digitcreativestudio.popularmovieapp.db.DBContract.CONTENT_URI;
import static com.digitcreativestudio.popularmovieapp.db.DBContract.PROVIDER_NAME;

/**
 * Created by faqih on 04/08/17.
 */

public class DBProvider extends ContentProvider {
    public static final int FAVORITE = 101;
    public static final int FAVORITE_ITEM = 102;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(PROVIDER_NAME, FavoriteEntry.TABLE_NAME, FAVORITE);
        sURIMatcher.addURI(PROVIDER_NAME, FavoriteEntry.TABLE_NAME+"/#", FAVORITE_ITEM);
    }

    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        switch (sURIMatcher.match(uri)) {
            case FAVORITE:
                retCursor = dbHelper.getReadableDatabase().query(
                        FavoriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITE_ITEM:
                retCursor = dbHelper.getReadableDatabase().query(
                        FavoriteEntry.TABLE_NAME,
                        projection,
                        FavoriteEntry._ID+" = ?",
                        new String[]{
                                FavoriteEntry.getId(uri)
                        },
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String type = "vnd.android.cursor.";
        switch (sURIMatcher.match(uri)){
            case FAVORITE:
                type = "vnd.android.cursor.dir/vnd."+PROVIDER_NAME+"."+ FavoriteEntry.TABLE_NAME;
                break;
            case FAVORITE_ITEM:
                type += "vnd.android.cursor.item/vnd."+PROVIDER_NAME+"."+ FavoriteEntry.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return type;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;

        long rowID;
        switch (sURIMatcher.match(uri)){
            case FAVORITE:
                rowID = db.insert(	FavoriteEntry.TABLE_NAME, null, values);
                if (rowID > 0)
                {
                    returnUri = ContentUris.withAppendedId(CONTENT_URI.buildUpon().appendPath(FavoriteEntry.TABLE_NAME).build(), rowID);
                }else {
                    throw new SQLException("Failed to add a record into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(returnUri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted;

        switch (sURIMatcher.match(uri)) {
            case FAVORITE:
                rowsDeleted = db.delete(FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE_ITEM:
                rowsDeleted = db.delete(FavoriteEntry.TABLE_NAME,
                        FavoriteEntry._ID + " = ?",
                        new String[]{
                                FavoriteEntry.getId(uri)
                        });
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated;

        switch (sURIMatcher.match(uri)) {
            case FAVORITE:
                rowsUpdated = db.update(FavoriteEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case FAVORITE_ITEM:
                rowsUpdated = db.update(FavoriteEntry.TABLE_NAME,
                        values,
                        FavoriteEntry._ID + " = ?",
                        new String[]{
                                FavoriteEntry.getId(uri)
                        });
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
