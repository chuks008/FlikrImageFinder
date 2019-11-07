package com.app.flikrsearchdemo.presentation.photo_search;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.flikrsearchdemo.R;
import com.app.flikrsearchdemo.presentation.PhotoDetailActivity;
import com.app.flikrsearchdemo.presentation.adapter.photos.PhotoListAdapter;
import com.app.flikrsearchdemo.presentation.adapter.search_term.SearchTermAdapter;
import com.app.flikrsearchdemo.presentation.favorites.FavoritePhotosActivity;

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
    private static final int WRITE_STORAGE_REQUEST = 101;

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
        SearchView searchView = (SearchView) searchIcon.getActionView();
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
                startActivity(new Intent(this, FavoritePhotosActivity.class));
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
    public void showBookmarkSuccess(String message) {
        Log.e(TAG, message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSelectedPhoto(String photoTitle, String photoUrl) {
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
    public void checkPermissions() {
        if(Build.VERSION.SDK_INT < 23) {
            presenter.saveCurrentPhoto();
            return;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_REQUEST);
        } else {
            presenter.saveCurrentPhoto();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WRITE_STORAGE_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.saveCurrentPhoto();
                } else {
                    Toast.makeText(this,
                            "Please grant write storage access to save photos",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.saveSearchTerms();

    }

}
