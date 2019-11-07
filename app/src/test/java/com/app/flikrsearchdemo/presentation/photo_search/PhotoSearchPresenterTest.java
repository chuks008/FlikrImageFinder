package com.app.flikrsearchdemo.presentation.photo_search;

import com.app.flikrsearchdemo.data.repository.favorites.FavoritePhotoRepository;
import com.app.flikrsearchdemo.data.repository.photos_search.PhotoSearchRepository;
import com.app.flikrsearchdemo.data.repository.photos_search.SearchPhoto;
import com.app.flikrsearchdemo.data.repository.photos_search.response.SearchResultResponse;
import com.app.flikrsearchdemo.data.repository.search_terms.SearchTermRepository;
import com.app.flikrsearchdemo.executors.AppTaskExecutor;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class PhotoSearchPresenterTest {

    private PhotoSearchPresenter sut;

    @Mock
    private PhotoSearchRepository mockPhotoSearchRepo;

    @Mock
    private SearchTermRepository mockSearchTermRepo;

    @Mock
    private FavoritePhotoRepository mockFavoritePhotoRepo;

    private TestExecutor testExecutor;

    @Mock
    private SearchScreenContract.View view;

    @Captor
    private ArgumentCaptor<SingleObserver<SearchResultResponse>> photoRequestCaptor;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        sut = new PhotoSearchPresenter(mockPhotoSearchRepo,
                mockSearchTermRepo,
                mockFavoritePhotoRepo,
                testExecutor,
                testExecutor);

        sut.setView(view);
    }

    private void searchPhotoRequest(SearchResultResponse photoResult, Throwable error) {

        Mockito.verify(mockPhotoSearchRepo.queryImage(1, ""))
                .subscribeOn(testExecutor.getScheduler())
                .observeOn(testExecutor.getScheduler())
                .subscribe(photoRequestCaptor.capture());

        if(photoResult != null) {
            photoRequestCaptor.capture().onSuccess(photoResult);
        } else {
            photoRequestCaptor.capture().onError(error);
        }

    }

    private class TestExecutor implements AppTaskExecutor {
        @Override
        public Scheduler getScheduler() {
            return Schedulers.trampoline();
        }
    }




}