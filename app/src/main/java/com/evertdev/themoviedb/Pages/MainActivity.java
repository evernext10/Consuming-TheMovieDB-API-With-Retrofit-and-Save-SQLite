package com.evertdev.themoviedb.Pages;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.evertdev.themoviedb.Adapters.MoviesAdapter;
import com.evertdev.themoviedb.Common.Common;
import com.evertdev.themoviedb.Database.SQLite;
import com.evertdev.themoviedb.Models.Movie;
import com.evertdev.themoviedb.Models.MoviesResponse;
import com.evertdev.themoviedb.R;
import com.evertdev.themoviedb.Retrofit.RetrofitClient;
import com.evertdev.themoviedb.Service.MovieDBApi;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    ProgressDialog pd;
    private SwipeRefreshLayout swipeContainer;
    private SQLite favoriteDbHelper;
    private AppCompatActivity activity = MainActivity.this;
    public static final String LOG_TAG = MoviesAdapter.class.getName();
    private FloatingActionMenu fab_menu;
    FloatingActionButton fab_fav, fab_most, fab_vote;
    private Movie favorite;
    Movie movie;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("The Movie DB");
        setSupportActionBar(toolbar);

        initViews();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


    }

    public Activity getActivity(){
        Context context = this;
        while (context instanceof ContextWrapper){
            if (context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;

    }

    private void initViews(){

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        movieList = new ArrayList<>();
        adapter = new MoviesAdapter(this, movieList);

        fab_menu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        fab_fav = (FloatingActionButton)findViewById(R.id.fab_favorito);
        fab_most = (FloatingActionButton)findViewById(R.id.fab_mas_popular);
        fab_vote = (FloatingActionButton)findViewById(R.id.fab_voto_promedio);

        fab_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String sortOrder = preferences.getString(
                        getString(R.string.pref_sort_order_key),
                        getString(R.string.pref_most_popular)
                );
                Log.d(LOG_TAG, "Sorting by favorite");
                initViews2();
            }
        });
        fab_most.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String sortOrder = preferences.getString(
                        getString(R.string.pref_sort_order_key),
                        getString(R.string.pref_most_popular)
                );

                Log.d(LOG_TAG, "Sorting by most popular");
                loadJSON();

            }
        });

        fab_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String sortOrder = preferences.getString(
                        getString(R.string.pref_sort_order_key),
                        getString(R.string.pref_most_popular)
                );

                Log.d(LOG_TAG, "Sorting by vote average");
                loadJSON1();

            }
        });


        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        favoriteDbHelper = new SQLite(activity);


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                Toast.makeText(MainActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
                initViews();
            }
        });

        checkSortOrder();

    }

    private void initViews2(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        movieList = new ArrayList<>();
        adapter = new MoviesAdapter(this, movieList);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        favoriteDbHelper = new SQLite(activity);

        getAllFavorite();
    }

    private void loadJSON(){

        try{
            if (Common.MOVIEDB_API_KEY.isEmpty()){
                Toast.makeText(getApplicationContext(), "Por favor obtener una KEY en themoviedb.org", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }
            if(Common.isNetworkAvailable(getBaseContext())){
                RetrofitClient Client = new RetrofitClient();
                MovieDBApi apiService =
                        Client.getClient().create(MovieDBApi.class);
                Call<MoviesResponse> call = apiService.getPopularMovies(Common.MOVIEDB_API_KEY);
                call.enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        List<Movie> movies = response.body().getResults();
                        Collections.sort(movies, Movie.BY_NAME_ALPHABETICAL);
                        recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                        recyclerView.smoothScrollToPosition(0);
                        if (swipeContainer.isRefreshing()){
                            swipeContainer.setRefreshing(false);
                        }

                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                        getAllMovies();

                    }
                });
            }else{
                getAllMovies();
            }

        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadJSON1(){

        try{
            if (Common.MOVIEDB_API_KEY.isEmpty()){
                Toast.makeText(getApplicationContext(), "Por favor obtener una KEY en themoviedb.org", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }

            RetrofitClient Client = new RetrofitClient();
            MovieDBApi apiService =
                    Client.getClient().create(MovieDBApi.class);
            Call<MoviesResponse> call = apiService.getTopRatedMovies(Common.MOVIEDB_API_KEY);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    List<Movie> movies = response.body().getResults();
                    saveFavorite(movies);
                    recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);
                    if (swipeContainer.isRefreshing()){
                        swipeContainer.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    getAllMovies();

                }
            });
        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void saveFavorite(List<Movie> movie){
        favoriteDbHelper = new SQLite(activity);
        favorite = new Movie();

        for(Movie m : movie){
            Double rate = m.getVoteAverage();

            favorite.setId(m.getId());
            favorite.setOriginalTitle(m.getOriginalTitle());
            favorite.setPosterPath(m.getPosterPath());
            favorite.setVoteAverage(rate);
            favorite.setOverview(m.getOverview());

            favoriteDbHelper.addMovie(favorite);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
       // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s){
        checkSortOrder();
    }

    private void checkSortOrder(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = preferences.getString(
                this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_most_popular)
        );
        if (sortOrder.equals(this.getString(R.string.pref_most_popular))) {
            loadJSON();
        } else if (sortOrder.equals(this.getString(R.string.favorite))){
            Log.d(LOG_TAG, "Sorting by favorite");
            initViews2();
        } else{
            Log.d(LOG_TAG, "Sorting by vote average");
            loadJSON1();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (movieList.isEmpty()){
            checkSortOrder();
        }else{

            checkSortOrder();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void getAllFavorite(){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params){
                movieList.clear();
                movieList.addAll(favoriteDbHelper.getAllFavorite());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void getAllMovies(){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params){
                movieList.clear();
                movieList.addAll(favoriteDbHelper.getAllMovie());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
