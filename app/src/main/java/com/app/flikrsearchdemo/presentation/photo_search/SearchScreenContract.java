package com.app.flikrsearchdemo.presentation.photo_search;

import com.app.flikrsearchdemo.data.repository.photos_search.SearchPhoto;

/**
 * Created by Your name on 2019-11-05.
 */
public interface SearchScreenContract {

    interface View {
        /**
         * Show an error message in the view when an operation fails
         *
         * @param errorMessage: error message
         */
        void showError(String errorMessage);

        /**
         * Show an error when there is currently no data stored in the presenter
         */
        void showErrorWithNoInitialResult();

        /**
         * Update the state of photo items on the view
         */
        void updatePhotoList();

        /**
         * Hide the loading indicators in the view
         */
        void hideLoading();

        /**
         * Show the loading indicators in the view
         */
        void showLoading();

        void showSelected(String photoTitle, String photoUrl);
    }

    interface UserActionListener {

        /**
         * Search the presenter's data source for pictures using the tags set
         *
         * @param tags: String
         */
        void searchForPhotos(String tags);
    }
}
