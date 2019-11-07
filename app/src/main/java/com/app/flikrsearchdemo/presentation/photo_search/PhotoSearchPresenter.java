package com.app.flikrsearchdemo.presentation.photo_search;

import com.app.flikrsearchdemo.Constants;
import com.app.flikrsearchdemo.data.file_management.OnImageDownloadComplete;
import com.app.flikrsearchdemo.data.repository.favorites.FavoritePhotoRepository;
import com.app.flikrsearchdemo.data.repository.photos_search.PhotoSearchRepository;
import com.app.flikrsearchdemo.data.repository.photos_search.SearchPhoto;
import com.app.flikrsearchdemo.data.repository.photos_search.response.ResultPhoto;
import com.app.flikrsearchdemo.data.repository.photos_search.response.SearchResultResponse;
import com.app.flikrsearchdemo.data.repository.search_terms.SearchTermRepository;
import com.app.flikrsearchdemo.executors.AppTaskExecutor;
import com.app.flikrsearchdemo.presentation.adapter.photos.PhotoConnector;
import com.app.flikrsearchdemo.presentation.adapter.photos.PhotoRow;
import com.app.flikrsearchdemo.presentation.adapter.search_term.SearchTermConnector;
import com.app.flikrsearchdemo.presentation.adapter.search_term.SearchTermRow;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by Your name on 2019-11-05.
 */
public class PhotoSearchPresenter implements SearchScreenContract.UserActionListener,
        PhotoConnector, SearchTermConnector, OnImageDownloadComplete {

    private SearchScreenContract.View view;
    private PhotoSearchRepository photoSearchRepository;
    private SearchTermRepository searchTermRepository;
    private AppTaskExecutor backgroundExecutor;
    private AppTaskExecutor postTaskExecutor;
    private FavoritePhotoRepository favoritePhotoRepository;
    public static final String NETWORK_ERROR = "Error during search. Check your internet connection";
    public static final String NO_RESULTS_FOUND = "No results found";

    private String currentTag = "";
    private int currentPhotoPosition = 0;

    private int currentPage = 1;

    private boolean isFirstLoad;
    private int totalPages = 1;

    public void setFirstLoad(boolean firstLoad) {
        isFirstLoad = firstLoad;
    }

    private List<SearchPhoto> photoSearchResults = new ArrayList<>();
    private LinkedList<String> searchTerms = new LinkedList<>();

    @Inject
    PhotoSearchPresenter(PhotoSearchRepository photoSearchRepository,
                         SearchTermRepository searchTermRepository,
                         FavoritePhotoRepository favoritePhotoRepository,
                         @Named(Constants.BACKGROUND_THREAD_KEY) AppTaskExecutor backgroundExecutor,
                         @Named(Constants.MAIN_THREAD_KEY) AppTaskExecutor postTaskExecutor) {

        this.photoSearchRepository = photoSearchRepository;
        this.searchTermRepository = searchTermRepository;
        this.favoritePhotoRepository = favoritePhotoRepository;
        this.backgroundExecutor = backgroundExecutor;
        this.postTaskExecutor = postTaskExecutor;

    }

    public void setView(SearchScreenContract.View view) {
        this.view = view;
    }

    @Override
    public boolean isLastPage() {
        return photoSearchResults.size() == totalPages;
    }

    /**
     *
     *
     * @param searchTermRow
     * @param position
     */
    @Override
    public void bind(@NotNull SearchTermRow searchTermRow, int position) {
        String currentSearchTerm = searchTerms.get(position);
        searchTermRow.setSearchText(currentSearchTerm);
        searchTermRow.setPosition(position);
    }

    @Override
    public int getCount() {
        return searchTerms.size();
    }

    @Override
    public void onSelectSearchTerm(int position) {
        onNewPhotoSearch(searchTerms.get(position));
    }

    @Override
    public void getSearchTerms() {

        searchTerms.clear();
        searchTerms.addAll(searchTermRepository.getSearchTerms());
    }

    private void resetSearch() {
        currentPage = 1;
        photoSearchResults.clear();
        view.updatePhotoList();
    }

    @Override
    public void onNewPhotoSearch(String tags) {

        resetSearch();

        if(!searchTerms.contains(tags)) {
            searchTerms.addFirst(tags);
            searchTermRepository.addNewSearchTerm(tags);
        } else {
            searchTerms.remove(tags);
            searchTerms.addFirst(tags);
        }

        view.updateSearchTerms();

        System.out.println("Searching for photos with tags "+ tags);

        currentTag = tags;
        searchForPhotos(currentTag);
    }

    @Override
    public void refreshPhotoList() {
        resetSearch();
        searchForPhotos(currentTag);
    }

    @Override
    public void loadMorePhotos() {
        searchForPhotos(currentTag);
    }

    private void searchForPhotos(String tags) {

        view.showLoading();

        System.out.println(backgroundExecutor.getScheduler());
        System.out.println(postTaskExecutor.getScheduler());

        photoSearchRepository.queryImage(currentPage, tags)
                .subscribeOn(backgroundExecutor.getScheduler())
                .observeOn(postTaskExecutor.getScheduler())
                .subscribe(new PhotoRequestObserver());
    }

    @Override
    public void bind(@NotNull PhotoRow photo, int position) {
        SearchPhoto currentItem = photoSearchResults.get(position);
        photo.setTitle(currentItem.getPhotoTitle());
        photo.setPosition(position);
        photo.setImage(generateImageUrl(currentItem));
    }

    private String generateImageUrl(SearchPhoto photo) {
        return String.format(Constants.IMAGE_LOAD_URL,
                photo.getFarmId(),
                photo.getServerId(),
                photo.getPhotoId(),
                photo.getSecret());
    }

    @Override
    public int getItemCount() {
        return photoSearchResults.size();
    }

    @Override
    public void onSelectItem(int position) {
        SearchPhoto photo = photoSearchResults.get(position);

        view.showSelectedPhoto(photo.getPhotoTitle(),
                generateImageUrl(photo));
    }

    @Override
    public void onActionPerformed(int position) {

        currentPhotoPosition = position;
        view.checkPermissions();
    }

    @Override
    public void saveCurrentPhoto() {
        SearchPhoto photo = photoSearchResults.get(currentPhotoPosition);

        System.out.println("Saving: "+photo.getPhotoTitle());

        favoritePhotoRepository.addPhoto(photo.getPhotoTitle(),
                generateImageUrl(photo),
                this);
    }

    /**
     * Save an image reference to the photo repository when it has completed saving to the local
     * storage
     *
     * @param error
     * @param message
     * @param savePhotoToDBTask
     */
    @Override
    public void onCompleteImageSave(boolean error, final @NotNull String message, Completable savePhotoToDBTask) {

        if(error) {
            view.showError(message);
        } else {
            savePhotoToDBTask
                    .subscribeOn(backgroundExecutor.getScheduler())
                    .observeOn(postTaskExecutor.getScheduler())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                           view.showBookmarkSuccess(message);
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showError(e.getLocalizedMessage());
                        }
                    });
        }

    }

    /**
     * An observer used to request for images from the network
     */
    private final class PhotoRequestObserver implements SingleObserver<SearchResultResponse> {

        /**
         *
         * @param photo
         * @return
         */
        private SearchPhoto generateSearchPhoto(ResultPhoto photo) {
            return new SearchPhoto.Builder()
                    .photoId(photo.getPhotoId())
                    .ownerId(photo.getOwnerId())
                    .photoTitle(photo.getPhotoTitle())
                    .farmId(photo.getFarmId())
                    .serverId(photo.getServerId())
                    .secret(photo.getSecret())
                    .build();
        }

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onSuccess(SearchResultResponse searchResultResponse) {

            view.hideLoading();

            if(searchResultResponse.getStatusMessage().equals("ok")){
                List<ResultPhoto> photos = searchResultResponse.getPhotoResult().getPhotoResultList();

                if(photos.size() < 1) {
                    view.showStatusMessageError(NO_RESULTS_FOUND);
                    return;
                }

                for(ResultPhoto photo: photos) {
                    photoSearchResults.add(generateSearchPhoto(photo));
                }

                if(isFirstLoad) {
                    totalPages = searchResultResponse
                            .getPhotoResult()
                            .getPageCount();

                    view.onFirstPhotoResultLoad(totalPages);

                    isFirstLoad = false;
                } else {
                    view.updatePhotoList();
                }
                System.out.println("Current page queried: "+ currentPage);
                currentPage += 1;

                return;
            }

            if(photoSearchResults.size() < 1) {
                view.showStatusMessageError(NETWORK_ERROR);
            } else {
                view.showError(searchResultResponse.getErrorMessage());
            }

            System.out.println("Current page queried: "+ currentPage);

        }

        @Override
        public void onError(Throwable e) {
            view.hideLoading();

            if(photoSearchResults.size() < 1) {
                view.showStatusMessageError(NETWORK_ERROR);
            } else {
                view.showError(e.getLocalizedMessage());
            }

            System.out.println("Current page queried: "+ currentPage);

        }
    }

    @Override
    public void dispose() {
        this.view = null;
    }


}
