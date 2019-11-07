package com.app.flikrsearchdemo.presentation.favorites;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.flikrsearchdemo.R;
import com.app.flikrsearchdemo.presentation.adapter.favorites.FavoritePhotoListAdapter;
import com.app.flikrsearchdemo.presentation.adapter.photos.PhotoListAdapter;
import com.app.flikrsearchdemo.presentation.adapter.search_term.SearchTermAdapter;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by Your name on 2019-11-07.
 */
public class FavoritePhotosActivity extends DaggerAppCompatActivity
        implements FavoritePhotoScreenContract.View {

    private Toolbar toolbar;
    private RecyclerView photoRecyclerView;
    private FavoritePhotoListAdapter photoListAdapter;

    @Inject
    FavoritePhotoPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_list_layout);

        toolbar = findViewById(R.id.mainScreenToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Favorites");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter.setView(this);

        photoRecyclerView = findViewById(R.id.photoRecyclerView);
        photoListAdapter = new FavoritePhotoListAdapter(presenter);
        photoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        photoRecyclerView.setAdapter(photoListAdapter);

        findViewById(R.id.pastTermsRecyclerView).setVisibility(View.GONE);
        findViewById(R.id.swipeRefresher).setEnabled(false);
        presenter.getFavoritePhotos();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateFavoriteList() {
        photoListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String localizedMessage) {
        Toast.makeText(this, localizedMessage, Toast.LENGTH_SHORT).show();
    }
}
