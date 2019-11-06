package com.app.flikrsearchdemo.presentation.photo_search;

import com.app.flikrsearchdemo.Constants;
import com.app.flikrsearchdemo.data.repository.photos_search.PhotoSearchRepository;
import com.app.flikrsearchdemo.data.repository.photos_search.SearchPhoto;
import com.app.flikrsearchdemo.data.repository.photos_search.response.ResultPhoto;
import com.app.flikrsearchdemo.data.repository.photos_search.response.SearchResultResponse;
import com.app.flikrsearchdemo.executors.AppTaskExecutor;
import com.app.flikrsearchdemo.presentation.adapter.photos.PhotoConnector;
import com.app.flikrsearchdemo.presentation.adapter.photos.PhotoRow;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by Your name on 2019-11-05.
 */
public class PhotoSearchPresenter implements SearchScreenContract.UserActionListener,
        PhotoConnector {

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

    @Override
    public void bind(@NotNull PhotoRow photo, int position) {
        SearchPhoto currentItem = photoSearchResults.get(position);
        photo.setTitle(currentItem.getPhotoTitle());
        photo.setPosition(position);
        photo.setImage(String.format(Constants.IMAGE_LOAD_URL,
                currentItem.getFarmId(),
                currentItem.getServerId(),
                currentItem.getPhotoId(),
                currentItem.getSecret()));
    }

    @Override
    public int getItemCount() {
        return photoSearchResults.size();
    }

    @Override
    public void onSelectItem(int position) {
        view.showSelected(photoSearchResults.get(position).getPhotoTitle());
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


}
