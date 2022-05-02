package com.kapilv.githubtrending.utils.network;

import com.kapilv.githubtrending.model.data.TrendingRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface TrendingRepositoriesService {
    @GET("/repositories")
    Single<List<TrendingRepository>> getTrendingRepositories();
}
