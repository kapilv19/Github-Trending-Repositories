package com.kapilv.githubtrending.model.entities;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.kapilv.githubtrending.model.data.TrendingRepository;
import com.kapilv.githubtrending.utils.network.TrendingRepositoriesService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ModelRepository {

    private static final String TAG = ModelRepository.class.getSimpleName();

    Retrofit mRetrofit;

    private SingleObserver<List<TrendingRepository>> mObserver;

    private TrendingRepositoriesService trendingRepositoriesService;
    private Single<List<TrendingRepository>> mObservable;

    private MutableLiveData<List<TrendingRepository>> mTrendingRepositories;
    private MutableLiveData<Boolean> mError;

    @Inject
    public ModelRepository(Retrofit retrofit) {
        mRetrofit = retrofit;
        init();
    }

    public void subscribeToTrendingRepositories(MediatorLiveData<List<TrendingRepository>> mediator, Observer<List<TrendingRepository>> observer) {
        mediator.addSource(mTrendingRepositories, observer);
    }

    public void subscribeToError(MediatorLiveData<Boolean> mediator, Observer<Boolean> observer) {
        mediator.addSource(mError, observer);
    }

    public void fetchTrendingRepositories(boolean forceRefresh) {
        if (forceRefresh) {
            trendingRepositoriesService = mRetrofit.create(TrendingRepositoriesService.class);
            mObservable = trendingRepositoriesService.getTrendingRepositories();
            mObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mObserver);
        } else {

        }
    }

    private void init() {
        mTrendingRepositories = new MutableLiveData<>();
        mError = new MutableLiveData<>();

        mObserver = new SingleObserver<List<TrendingRepository>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e(TAG, "Subscribed to Retrofit Service");
            }

            @Override
            public void onSuccess(@NonNull List<TrendingRepository> trendingRepositories) {
                Log.e(TAG, "Retrofit Service fetch Successful");
                mTrendingRepositories.postValue(trendingRepositories);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "Retrofit Service Error: " + e.getMessage());
                mError.postValue(true);
            }
        };
    }
}
