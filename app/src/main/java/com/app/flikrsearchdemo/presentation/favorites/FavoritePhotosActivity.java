package com.app.flikrsearchdemo.presentation.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.flikrsearchdemo.R;
import com.app.flikrsearchdemo.presentation.PhotoDetailActivity;
import com.app.flikrsearchdemo.presentation.adapter.CustomItemDecoration;
import com.app.flikrsearchdemo.presentation.adapter.favorites.FavoritePhotoListAdapter;


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

        setupToolbar();
        setupFavoritesPhotoList();
        hideUnusedViews();

        presenter.setView(this);
        presenter.getFavoritePhotos();

    }

    private void setupFavoritesPhotoList() {
        photoRecyclerView = findViewById(R.id.photoRecyclerView);
        photoListAdapter = new FavoritePhotoListAdapter(presenter);
        photoRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        photoRecyclerView.setAdapter(photoListAdapter);
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.mainScreenToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Favorites");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void hideUnusedViews() {
        findViewById(R.id.pastTermsRecyclerView).setVisibility(View.GONE);
        findViewById(R.id.swipeRefresher).setEnabled(false);
        findViewById(R.id.statusMessageLayout).setVisibility(View.GONE);
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
    public void updatePhotoDeletedAt(int position, int listSize) {
        photoListAdapter.notifyItemRemoved(position);
        photoListAdapter.notifyItemRangeChanged(position, listSize);
    }
}
