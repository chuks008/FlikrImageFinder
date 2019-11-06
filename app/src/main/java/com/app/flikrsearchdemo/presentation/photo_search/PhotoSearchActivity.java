package com.app.flikrsearchdemo.presentation.photo_search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.flikrsearchdemo.R;
import com.app.flikrsearchdemo.data.repository.photos_search.SearchPhoto;
import com.app.flikrsearchdemo.presentation.PhotoDetailActivity;
import com.app.flikrsearchdemo.presentation.adapter.photos.PhotoListAdapter;
import com.app.flikrsearchdemo.presentation.adapter.search_term.SearchTermAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by Your name on 2019-11-05.
 */
public class PhotoSearchActivity extends DaggerAppCompatActivity implements SearchScreenContract.View {

    private static final String TAG = PhotoSearchActivity.class.getSimpleName();

    private Toolbar toolbar;
    private RecyclerView photoRecyclerView;
    private PhotoListAdapter photoListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchTermAdapter searchTermAdapter;
    private RecyclerView searchTermRecyclerView;
    private SearchView searchView;

    @Inject
    PhotoSearchPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.photo_list_layout);

        toolbar = findViewById(R.id.mainScreenToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        presenter.setView(this);

        photoRecyclerView = findViewById(R.id.photoRecyclerView);
        photoListAdapter = new PhotoListAdapter(presenter);
        photoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        photoRecyclerView.setAdapter(photoListAdapter);

        searchTermRecyclerView = findViewById(R.id.pastTermsRecyclerView);
        searchTermAdapter = new SearchTermAdapter(presenter);
        searchTermRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.HORIZONTAL,
                false));
        searchTermRecyclerView.setAdapter(searchTermAdapter);

        presenter.getSearchTerms();

        swipeRefreshLayout = findViewById(R.id.swipeRefresher);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshPhotoList();
            }
        });
        Log.e(TAG, "Starting search activities");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchIcon = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchIcon.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.onNewPhotoSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorites:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showError(String errorMessage) {
        Log.e(TAG, errorMessage);
    }

    @Override
    public void showErrorWithNoInitialResult() {
        Log.e(TAG, "Error getting photo results");
    }

    @Override
    public void updatePhotoList() {
        photoListAdapter.notifyDataSetChanged();
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showSelected(String photoTitle, String photoUrl) {
        Bundle detailBundle = new Bundle();
        detailBundle.putString("title", photoTitle);
        detailBundle.putString("image_url", photoUrl);
        detailBundle.putBoolean("from_search", true);

        Intent detailIntent = new Intent(this, PhotoDetailActivity.class);
        detailIntent.putExtras(detailBundle);
        startActivity(detailIntent);
    }

    @Override
    public void updateSearchTerms() {
        searchTermAdapter.notifyDataSetChanged();
        searchTermRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.saveSearchTerms();

    }

}
