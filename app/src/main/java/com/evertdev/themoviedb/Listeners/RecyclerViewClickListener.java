package com.evertdev.themoviedb.Listeners;

import android.view.View;

import com.evertdev.themoviedb.Models.Movie;

public interface RecyclerViewClickListener {
    void recyclerViewListClicked(View v, Movie currentMovie);
}
