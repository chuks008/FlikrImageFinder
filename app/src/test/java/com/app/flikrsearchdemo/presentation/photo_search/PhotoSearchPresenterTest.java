package com.app.flikrsearchdemo.presentation.photo_search;

import com.app.flikrsearchdemo.data.repository.favorites.FavoritePhotoRepository;
import com.app.flikrsearchdemo.data.repository.photos_search.PhotoSearchRepository;
import com.app.flikrsearchdemo.data.repository.photos_search.SearchPhoto;
import com.app.flikrsearchdemo.data.repository.photos_search.response.PhotoResultResponse;
import com.app.flikrsearchdemo.data.repository.photos_search.response.ResultPhoto;
import com.app.flikrsearchdemo.data.repository.photos_search.response.SearchResultResponse;
import com.app.flikrsearchdemo.data.repository.search_terms.SearchTermRepository;
import com.app.flikrsearchdemo.executors.AppTaskExecutor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(JUnit4.class)
public class PhotoSearchPresenterTest {

    private PhotoSearchPresenter sut;

    @Mock
    private PhotoSearchRepository mockPhotoSearchRepo;

    @Mock
    private SearchTermRepository mockSearchTermRepo;

    @Mock
    private FavoritePhotoRepository mockFavoritePhotoRepo;

    @Mock
    private SearchScreenContract.View view;

    private TestExecutor testExecutor;

    @Captor
    private ArgumentCaptor<SingleObserver<SearchResultResponse>> photoRequestCaptor;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        testExecutor = new TestExecutor();

        sut = new PhotoSearchPresenter(mockPhotoSearchRepo,
                mockSearchTermRepo,
                mockFavoritePhotoRepo,
                testExecutor,
                testExecutor);

        sut.setView(view);
    }

    @Test
    public void testOnNewPhotoSearchViewUpdatePhotoListOnSuccess() {
        String tags = "rabbits";
        sut.onNewPhotoSearch(tags);

        searchPhotoRequest(generateSearchPhotoResponse(eq(generatePhotoList()), eq("ok")), null);


        Mockito.verify(view).showStatusMessageError(eq(PhotoSearchPresenter.NO_RESULTS_FOUND));
    }

    private void searchPhotoRequest(SearchResultResponse photoResult, Throwable error) {

        Mockito.when(mockPhotoSearchRepo.queryImage(1, ""))
                .thenReturn(Single.just(photoResult));

        if(photoResult != null) {
            photoRequestCaptor.capture().onSuccess(photoResult);
        } else {
            photoRequestCaptor.capture().onError(error);
        }

    }

    private List<ResultPhoto> generatePhotoList() {
        ResultPhoto photo1 = new ResultPhoto(
                "54ee3",
                "77ess",
                "Photo 1",
                55,
                "4765",
                "secret1");

        ResultPhoto photo2 = new ResultPhoto(
                "s233s3",
                "9938ss",
                "Photo 2",
                55,
                "4765",
                "secret2");

        List<ResultPhoto> resultPhotos = new ArrayList<>();
        resultPhotos.add(photo1);
        resultPhotos.add(photo2);

        return resultPhotos;
    }

    private SearchResultResponse generateSearchPhotoResponse(List<ResultPhoto> photos,
                                                             String status)  {

        PhotoResultResponse photoResponse = new PhotoResultResponse(photos, 20);

        return new SearchResultResponse(photoResponse,
                status,
                "");
    }

    private class TestExecutor implements AppTaskExecutor {
        @Override
        public Scheduler getScheduler() {
            return Schedulers.trampoline();
        }
    }




}