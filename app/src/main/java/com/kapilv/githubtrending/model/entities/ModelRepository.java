package com.kapilv.githubtrending.model.entities;

import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.kapilv.githubtrending.model.data.GitHubRepository;
import com.kapilv.githubtrending.utils.network.TrendingRepositoriesService;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;

//Model Layer of Application
public class ModelRepository {

    private static final String TAG = ModelRepository.class.getSimpleName();

    //retrofit Api service to do trending repositories network call
    TrendingRepositoriesService mTrendingRepositoriesForcedService;
    TrendingRepositoriesService mTrendingRepositoriesCachedService;

    @Inject
    public ModelRepository(TrendingRepositoriesService trendingRepositoriesForcedService, TrendingRepositoriesService trendingRepositoriesCachedService) {
        mTrendingRepositoriesForcedService = trendingRepositoriesForcedService;
        mTrendingRepositoriesCachedService = trendingRepositoriesCachedService;
    }

    //exposes method to fetch trending repositories
    public Single<List<GitHubRepository>> fetchTrendingRepositories(boolean forceRefresh) {
        if (forceRefresh) { //Force network call without cache
            return mTrendingRepositoriesForcedService.getTrendingRepositories();
        } else { //Network call with cache
            return mTrendingRepositoriesCachedService.getTrendingRepositories();
        }
    }
}
