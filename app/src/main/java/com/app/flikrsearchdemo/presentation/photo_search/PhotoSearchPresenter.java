package com.app.flikrsearchdemo.presentation.photo_search;

import com.app.flikrsearchdemo.Constants;
import com.app.flikrsearchdemo.data.repository.photos_search.PhotoSearchRepository;
import com.app.flikrsearchdemo.data.repository.photos_search.SearchPhoto;
import com.app.flikrsearchdemo.data.repository.photos_search.response.ResultPhoto;
import com.app.flikrsearchdemo.data.repository.photos_search.response.SearchResultResponse;
import com.app.flikrsearchdemo.data.repository.search_terms.SearchTermRepository;
import com.app.flikrsearchdemo.data.repository.search_terms.SearchTermRepositoryImpl;
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
import javax.inject.Singleton;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by Your name on 2019-11-05.
 */
public class PhotoSearchPresenter implements SearchScreenContract.UserActionListener,
        PhotoConnector, SearchTermConnector {

    private SearchScreenContract.View view;
    private PhotoSearchRepository photoSearchRepository;
    private SearchTermRepository searchTermRepository;
    private AppTaskExecutor backgroundExecutor;
    private AppTaskExecutor postTaskExecutor;

    private int currentPage = 1;
    private String currentTag = "";
    private List<SearchPhoto> photoSearchResults = new ArrayList<>();
    private LinkedList<String> searchTerms = new LinkedList<>();

    @Inject
    PhotoSearchPresenter(PhotoSearchRepository photoSearchRepository,
                                SearchTermRepository searchTermRepository,
                                @Named(Constants.BACKGROUND_THREAD_KEY) AppTaskExecutor backgroundExecutor,
                                @Named(Constants.MAIN_THREAD_KEY) AppTaskExecutor postTaskExecutor) {

        this.photoSearchRepository = photoSearchRepository;
        this.searchTermRepository = searchTermRepository;
        this.backgroundExecutor = backgroundExecutor;
        this.postTaskExecutor = postTaskExecutor;

    }

    public void setView(SearchScreenContract.View view) {
        this.view = view;

    }

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
        currentPage += 1;
        searchForPhotos(currentTag);
    }


    private void searchForPhotos(String tags) {

        view.showLoading();

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

        view.showSelected(photo.getPhotoTitle(),
                generateImageUrl(photo));
    }

    private final class PhotoRequestObserver implements SingleObserver<SearchResultResponse> {

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
                for(ResultPhoto photo: photos) {
                    photoSearchResults.add(generateSearchPhoto(photo));
                }

                System.out.println("Total results: "+ photoSearchResults.size());

                view.updatePhotoList();
                return;
            }

            view.showError(searchResultResponse.getErrorMessage());

//            if(photoSearchResults.size() < 1) {
//                view.showErrorWithNoInitialResult();
//            } else {
//                view.showError(searchResultResponse.getErrorMessage());
//            }
        }

        @Override
        public void onError(Throwable e) {
            view.hideLoading();
            view.showError(e.getLocalizedMessage());
        }
    }

    @Override
    public void saveSearchTerms() {
        this.view = null;
        searchTermRepository.saveSearchTerms();
    }
}
