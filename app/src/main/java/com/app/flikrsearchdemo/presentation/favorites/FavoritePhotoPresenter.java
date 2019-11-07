package com.app.flikrsearchdemo.presentation.favorites;

import com.app.flikrsearchdemo.Constants;
import com.app.flikrsearchdemo.data.db.favorites.FavoritePhoto;
import com.app.flikrsearchdemo.data.repository.favorites.FavoritePhotoRepository;
import com.app.flikrsearchdemo.data.repository.photos_search.SearchPhoto;
import com.app.flikrsearchdemo.executors.AppTaskExecutor;
import com.app.flikrsearchdemo.presentation.adapter.photos.PhotoConnector;
import com.app.flikrsearchdemo.presentation.adapter.photos.PhotoRow;
import com.app.flikrsearchdemo.presentation.photo_search.SearchScreenContract;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by Your name on 2019-11-07.
 */
public class FavoritePhotoPresenter implements FavoritePhotoScreenContract.UserActionListener,
        PhotoConnector {

    private FavoritePhotoScreenContract.View view;
    private AppTaskExecutor backgroundExecutor;
    private AppTaskExecutor postTaskExecutor;
    private FavoritePhotoRepository favoritePhotoRepository;

    private int currentPhotoPosition = 0;
    private List<FavoritePhoto> favoritePhotos = new ArrayList<>();

    @Inject
    FavoritePhotoPresenter(@Named(Constants.BACKGROUND_THREAD_KEY) AppTaskExecutor backgroundExecutor,
                           @Named(Constants.MAIN_THREAD_KEY) AppTaskExecutor postTaskExecutor,
                           FavoritePhotoRepository favoritePhotoRepository) {

        this.backgroundExecutor = backgroundExecutor;
        this.postTaskExecutor = postTaskExecutor;
        this.favoritePhotoRepository = favoritePhotoRepository;
    }

    public void setView(FavoritePhotoScreenContract.View view) {
        this.view = view;
    }

    @Override
    public void getFavoritePhotos() {
        favoritePhotoRepository.getPhotos()
                .subscribeOn(backgroundExecutor.getScheduler())
                .observeOn(postTaskExecutor.getScheduler())
                .subscribe(new SingleObserver<List<FavoritePhoto>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<FavoritePhoto> acquiredFavoritePhotos) {

                        if(acquiredFavoritePhotos.size() == 0) {
                            view.showError("No photos have currently been saved");
                        } else {
                            favoritePhotos.clear();
                            favoritePhotos.addAll(acquiredFavoritePhotos);
                            view.updateFavoriteList();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError(e.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void bind(@NotNull PhotoRow photo, int position) {
        FavoritePhoto currentItem = favoritePhotos.get(position);
        photo.setTitle(currentItem.getPhotoTitle());
        photo.setPosition(position);
        photo.setImage(currentItem.getPhotoLocation());
    }

    @Override
    public int getItemCount() {
        return favoritePhotos.size();
    }

    @Override
    public void onSelectItem(int position) {

    }

    @Override
    public void onActionPerformed(int position) {

    }
}
