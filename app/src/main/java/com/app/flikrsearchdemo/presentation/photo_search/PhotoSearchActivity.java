package com.app.flikrsearchdemo.presentation.photo_search;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.app.flikrsearchdemo.R;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by Your name on 2019-11-05.
 */
public class PhotoSearchActivity extends DaggerAppCompatActivity implements SearchScreenContract.View {

    private static final String TAG = PhotoSearchActivity.class.getSimpleName();

    @Inject
    PhotoSearchPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter.setView(this);

        Log.e(TAG, "Starting search activities");
        presenter.searchForPhotos("cats,kittens,dogs");

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

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoading() {

    }
}
