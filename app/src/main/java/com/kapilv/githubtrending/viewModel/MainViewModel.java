package com.kapilv.githubtrending.viewModel;

import android.util.Log;

import androidx.databinding.Bindable;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kapilv.githubtrending.model.data.GitHubRepository;
import com.kapilv.githubtrending.model.entities.ModelRepository;
import com.kapilv.githubtrending.utils.constants.Constants;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    ModelRepository mRepository;

    //uses mutable live data to post updates to the activity and to data bindings
    private final MutableLiveData<List<GitHubRepository>> mTrendingRepositories = new MutableLiveData<>();
    private final MutableLiveData<String> mError = new MutableLiveData<>();
    public final MutableLiveData<Constants.Status> mStatus = new MutableLiveData<>(Constants.Status.LOADING);

    //Observer to observe changes in trending repo list from Model repository (Model Repository is project's model layer)
    private SingleObserver<List<GitHubRepository>> mTrendingRepositoriesObserver;

    //Observer to observe changes in trending repo list from sorting operation
    private SingleObserver<List<GitHubRepository>> mSortObserver;
    private Single<List<GitHubRepository>> mSortObservable;

    @Inject
    public MainViewModel(ModelRepository modelRepository) {
        mRepository = modelRepository;
        init();
    }

    //Provide trending repo list to subscribers
    public MutableLiveData<List<GitHubRepository>> trendingRepositoriesSubscription() {
        return mTrendingRepositories;
    }

    //Provide error message to subscribers
    public MutableLiveData<String> errorSubscription() {
        return mError;
    }

    //Fetches repos asynchronously from model layer (using Rxjava observer observable pattern) and updates trending repos observable when result is ready
    public void fetchTrendingRepositories(boolean forceRefresh) {
        Log.e(TAG, "Fetching...");
        mRepository.fetchTrendingRepositories(forceRefresh)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mTrendingRepositoriesObserver);
    }

    //Sorts repos asynchronously on a new thread and updates sortobserver when results are ready
    public void sortTrendingRepositories(Constants.Sort sortType) {
        switch (Objects.requireNonNull(mStatus.getValue())) {
            case LOADED:
                mSortObservable = Single.fromCallable(new Callable<List<GitHubRepository>>() {
                    @Override
                    public List<GitHubRepository> call() throws Exception {
                        return sort(sortType);
                    }
                });
                mSortObservable.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mSortObserver);
                break;
            case LOADING:
                mError.postValue("Please wait while data is being fetched!");
                break;
            case ERROR:
                mError.postValue("Please fetch data before Sorting!");
                break;
        }
    }

    //provides method for retyr button on error screen to fetch repositories
    public void retry() {
        fetchTrendingRepositories(false);
    }

    //Sort trending repos list using java's inbuilt comparator class
    private List<GitHubRepository> sort(Constants.Sort sortType) {
        List<GitHubRepository> trendingRepositories = mTrendingRepositories.getValue();
        if (trendingRepositories != null) {

            if (sortType == Constants.Sort.NAME) {
                Collections.sort(trendingRepositories, new Comparator<GitHubRepository>() {
                    @Override
                    public int compare(GitHubRepository t1, GitHubRepository t2) {
                        return t1.getName().toLowerCase(Locale.ROOT).compareTo(t2.getName().toLowerCase(Locale.ROOT));
                    }
                });
            } else if (sortType == Constants.Sort.STARS) {
                Collections.sort(trendingRepositories, new Comparator<GitHubRepository>() {
                    @Override
                    public int compare(GitHubRepository t1, GitHubRepository t2) {
                        return t2.getStars().compareTo(t1.getStars());
                    }
                });
            }
        }
        return trendingRepositories;
    }

    //Initialize View Model
    private void init() {
        initObservers();
    }

    //Initialize Observers
    private void initObservers() {
        //Initialize Trending repos Observer
        mTrendingRepositoriesObserver = new SingleObserver<List<GitHubRepository>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e(TAG, "Subscribed to Model: Trending Repos");
                //Set Loading state for UI when fetching starts
                mStatus.postValue(Constants.Status.LOADING);
            }

            @Override
            public void onSuccess(@NonNull List<GitHubRepository> gitHubRepositories) {
                Log.e(TAG, "Update from Model: Fetched!");
                if (gitHubRepositories.size() > 0) {
                    //Post newly fetched repo list to live data
                    mTrendingRepositories.postValue(gitHubRepositories);
                }
                //Set Loaded state for UI when fetching finishes
                mStatus.postValue(Constants.Status.LOADED);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "Update from Model: Error");
                //Set Error state for UI when error occurs
                mStatus.postValue(Constants.Status.ERROR);
            }
        };

        //Initialize Sort repos Observer
        mSortObserver = new SingleObserver<List<GitHubRepository>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e(TAG, "Subscribed to Sort Service!");
                //Set Loading state for UI when sorting starts
                mStatus.postValue(Constants.Status.LOADING);

            }

            @Override
            public void onSuccess(@NonNull List<GitHubRepository> trendingRepositories) {
                Log.e(TAG, "Sort Successful!");
                //Post sorted repo list to live data
                mTrendingRepositories.postValue(trendingRepositories);
                //Set Loaded state for UI when sorting finishes
                mStatus.postValue(Constants.Status.LOADED);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "Sort Error: " + e.getMessage());
                //Post error message to live data
                mError.postValue("Error while sorting: " + e.getMessage());
                //Set Error state for UI when error occurs
                mStatus.postValue(Constants.Status.LOADED);
            }
        };
    }
}
