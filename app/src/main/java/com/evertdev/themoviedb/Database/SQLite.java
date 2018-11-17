package com.evertdev.themoviedb.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.evertdev.themoviedb.Models.Movie;

import java.util.ArrayList;
import java.util.List;

public class SQLite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";

    private static final int DATABASE_VERSION = 1;

    public static final String LOGTAG = "DATABASE";

    SQLiteOpenHelper dbhandler;
    android.database.sqlite.SQLiteDatabase db;

    public SQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open(){
        Log.i(LOGTAG, "Database Opened");
        db = dbhandler.getWritableDatabase();
    }

    public void close(){
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteMovie.FavoriteEntry.TABLE_NAME + " (" +
                FavoriteMovie.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteMovie.FavoriteEntry.COLUMN_MOVIEID + " INTEGER, " +
                FavoriteMovie.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteMovie.FavoriteEntry.COLUMN_USERRATING + " REAL NOT NULL, " +
                FavoriteMovie.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteMovie.FavoriteEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL" +
                "); ";

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + FavoriteMovie.MovieEntry.TABLE_NAME + " (" +
                FavoriteMovie.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteMovie.FavoriteEntry.COLUMN_MOVIEID + " INTEGER, " +
                FavoriteMovie.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteMovie.FavoriteEntry.COLUMN_USERRATING + " REAL NOT NULL, " +
                FavoriteMovie.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteMovie.FavoriteEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMovie.FavoriteEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMovie.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addFavorite(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoriteMovie.FavoriteEntry.COLUMN_MOVIEID, movie.getId());
        values.put(FavoriteMovie.FavoriteEntry.COLUMN_TITLE, movie.getOriginalTitle());
        values.put(FavoriteMovie.FavoriteEntry.COLUMN_USERRATING, movie.getVoteAverage());
        values.put(FavoriteMovie.FavoriteEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(FavoriteMovie.FavoriteEntry.COLUMN_PLOT_SYNOPSIS, movie.getOverview());

        db.insert(FavoriteMovie.FavoriteEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void addMovie(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoriteMovie.MovieEntry.COLUMN_MOVIEID, movie.getId());
        values.put(FavoriteMovie.MovieEntry.COLUMN_TITLE, movie.getOriginalTitle());
        values.put(FavoriteMovie.MovieEntry.COLUMN_USERRATING, movie.getVoteAverage());
        values.put(FavoriteMovie.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(FavoriteMovie.MovieEntry.COLUMN_PLOT_SYNOPSIS, movie.getOverview());

        db.insert(FavoriteMovie.MovieEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteFavorite(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FavoriteMovie.FavoriteEntry.TABLE_NAME, FavoriteMovie.FavoriteEntry.COLUMN_MOVIEID+ "=" + id, null);
    }

    public List<Movie> getAllFavorite(){
        String[] columns = {
                FavoriteMovie.FavoriteEntry._ID,
                FavoriteMovie.FavoriteEntry.COLUMN_MOVIEID,
                FavoriteMovie.FavoriteEntry.COLUMN_TITLE,
                FavoriteMovie.FavoriteEntry.COLUMN_USERRATING,
                FavoriteMovie.FavoriteEntry.COLUMN_POSTER_PATH,
                FavoriteMovie.FavoriteEntry.COLUMN_PLOT_SYNOPSIS

        };
        String sortOrder =
                FavoriteMovie.FavoriteEntry._ID + " ASC";
        List<Movie> favoriteList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(FavoriteMovie.FavoriteEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst()){
            do {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteMovie.FavoriteEntry.COLUMN_MOVIEID))));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(FavoriteMovie.FavoriteEntry.COLUMN_TITLE)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavoriteMovie.FavoriteEntry.COLUMN_USERRATING))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteMovie.FavoriteEntry.COLUMN_POSTER_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteMovie.FavoriteEntry.COLUMN_PLOT_SYNOPSIS)));

                favoriteList.add(movie);

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return favoriteList;
    }

    public List<Movie> getAllMovie(){
        String[] columns = {
                FavoriteMovie.MovieEntry._ID,
                FavoriteMovie.MovieEntry.COLUMN_MOVIEID,
                FavoriteMovie.MovieEntry.COLUMN_TITLE,
                FavoriteMovie.MovieEntry.COLUMN_USERRATING,
                FavoriteMovie.MovieEntry.COLUMN_POSTER_PATH,
                FavoriteMovie.MovieEntry.COLUMN_PLOT_SYNOPSIS

        };
        String sortOrder =
                FavoriteMovie.MovieEntry._ID + " ASC";
        List<Movie> moviesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(FavoriteMovie.MovieEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst()){
            do {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteMovie.MovieEntry.COLUMN_MOVIEID))));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(FavoriteMovie.MovieEntry.COLUMN_TITLE)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavoriteMovie.MovieEntry.COLUMN_USERRATING))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteMovie.MovieEntry.COLUMN_POSTER_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteMovie.MovieEntry.COLUMN_PLOT_SYNOPSIS)));

                moviesList.add(movie);

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return moviesList;
    }
}
