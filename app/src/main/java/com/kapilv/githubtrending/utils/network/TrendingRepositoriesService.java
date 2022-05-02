package com.kapilv.githubtrending.utils.network;

import com.kapilv.githubtrending.model.data.GitHubRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

//retrofit Api service to fetch trending repositories
public interface TrendingRepositoriesService {
    @GET("/repositories")
    Single<List<GitHubRepository>> getTrendingRepositories();
}
