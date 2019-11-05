package com.app.flikrsearchdemo.presentation.photo_search;

import com.app.flikrsearchdemo.Constants;
import com.app.flikrsearchdemo.data.repository.photos_search.PhotoSearchRepository;
import com.app.flikrsearchdemo.data.repository.photos_search.SearchPhoto;
import com.app.flikrsearchdemo.data.repository.photos_search.response.PhotoResult;
import com.app.flikrsearchdemo.data.repository.photos_search.response.ResultPhoto;
import com.app.flikrsearchdemo.data.repository.photos_search.response.SearchResultResponse;
import com.app.flikrsearchdemo.executors.AppTaskExecutor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by Your name on 2019-11-05.
 */
public class PhotoSearchPresenter implements SearchScreenContract.UserActionListener {

    private SearchScreenContract.View view;
    private PhotoSearchRepository photoSearchRepository;
    private AppTaskExecutor backgroundExecutor;
    private AppTaskExecutor postTaskExecutor;

    private int currentPage = 1;
    private List<SearchPhoto> photoSearchResults = new ArrayList<>();

    @Inject
    PhotoSearchPresenter(PhotoSearchRepository photoSearchRepository,
                                @Named(Constants.BACKGROUND_THREAD_KEY) AppTaskExecutor backgroundExecutor,
                                @Named(Constants.MAIN_THREAD_KEY) AppTaskExecutor postTaskExecutor) {

        this.photoSearchRepository = photoSearchRepository;
        this.backgroundExecutor = backgroundExecutor;
        this.postTaskExecutor = postTaskExecutor;
    }

    public void setView(SearchScreenContract.View view) {
        this.view = view;
    }

    @Override
    public void searchForPhotos(String tags) {
        view.showLoading();

        System.out.println("Searching for photos with tags "+ tags);

        photoSearchRepository.queryImage(currentPage, 25, tags)
                .subscribeOn(backgroundExecutor.getScheduler())
                .observeOn(postTaskExecutor.getScheduler())
                .subscribe(new PhotoRequestObserver());
    }

    private final class PhotoRequestObserver implements SingleObserver<SearchResultResponse> {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onSuccess(SearchResultResponse searchResultResponse) {

            view.hideLoading();

            if(searchResultResponse.getStatusMessage().equals("ok")){
                List<ResultPhoto> photos = searchResultResponse.getPhotoResult().getPhotoResultList();
                for(ResultPhoto photo: photos) {
                    photoSearchResults.add(new SearchPhoto(photo.getPhotoId(),
                            photo.getOwnerId(),
                            photo.getPhotoTitle()));
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


}
