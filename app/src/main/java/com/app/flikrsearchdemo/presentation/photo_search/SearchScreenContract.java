package com.app.flikrsearchdemo.presentation.photo_search;

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

        void showSelectedPhoto(String photoTitle, String photoUrl);
        void updateSearchTerms();

        void showBookmarkSuccess(String message);

        void checkPermissions();

        /**
         * Method used to initialize pagination
         *
         * @param pageSize
         */
        void onFirstPhotoResultLoad(int pageSize);

        /**
         * Show an error when no results are found either due to a network or keyword error
         *
         * @param errorMessage
         */
        void showStatusMessageError(String errorMessage);
    }

    interface UserActionListener {

        /**
         * Search the presenter's data source for pictures using the tags set
         *
         */
        void onNewPhotoSearch(String tags);

        /**
         * Load more photos from the network
         */
        void loadMorePhotos();

        /**
         * Get all the past keywords searched by the user
         */
        void getSearchTerms();

        /**
         * Reset the photo query search parameters to their initial state
         */
        void refreshPhotoList();

        /**
         * Save the current photo chosen by the user to the device
         */
        void saveCurrentPhoto();

        /**
         * Remove the reference to the view in the presenter
         */
        void dispose();

        /**
         * Returns if the current page in the search query is the last page to fetch
         * @return
         */
        boolean isLastPage();
    }
}
