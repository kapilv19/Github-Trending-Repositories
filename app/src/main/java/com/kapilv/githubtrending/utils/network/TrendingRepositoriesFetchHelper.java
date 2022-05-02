package com.kapilv.githubtrending.utils.network;

import com.kapilv.githubtrending.model.data.TrendingRepositoriesModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TrendingRepositoriesFetchHelper {
    @GET("/repositories")
    Call<List<TrendingRepositoriesModel>> getTrendingRepositories();
}
