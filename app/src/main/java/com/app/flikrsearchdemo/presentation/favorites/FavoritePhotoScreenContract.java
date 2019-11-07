package com.app.flikrsearchdemo.presentation.favorites;

/**
 * Created by Your name on 2019-11-07.
 */
public interface FavoritePhotoScreenContract {

    interface View {
        void updateFavoriteList();
        void showError(String localizedMessage);
    }

    interface UserActionListener {
        void getFavoritePhotos();
    }
}
