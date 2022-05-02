package com.kapilv.githubtrending.model.entities;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Retrofit;

public class ModelRepository {

    Retrofit mRetrofit;

    @Inject
    public ModelRepository(Retrofit retrofit) {
        mRetrofit = retrofit;
    }

    public void getTrendingRepositories(boolean forceRefresh) {
        if (forceRefresh) {

        } else {

        }
    }
}
