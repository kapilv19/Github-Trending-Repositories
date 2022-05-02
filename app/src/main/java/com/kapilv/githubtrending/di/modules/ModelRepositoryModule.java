package com.kapilv.githubtrending.di.modules;

import com.kapilv.githubtrending.model.entities.ModelRepository;
import com.kapilv.githubtrending.utils.network.TrendingRepositoriesService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ModelRepositoryModule {

    @Provides
    @Singleton
    @Named("forced_service")
    TrendingRepositoriesService provideTrendingRepositoriesForcedService(@Named("forced") Retrofit retrofit) {
        return retrofit.create(TrendingRepositoriesService.class);
    }

    @Provides
    @Singleton
    @Named("cached_service")
    TrendingRepositoriesService provideTrendingRepositoriesCachedService(@Named("cached") Retrofit retrofit) {
        return retrofit.create(TrendingRepositoriesService.class);
    }

    @Provides
    @Singleton
    ModelRepository provideModelRepository(@Named("forced_service") TrendingRepositoriesService trendingRepositoriesForcedService, @Named("cached_service") TrendingRepositoriesService trendingRepositoriesCachedService) {
        return new ModelRepository(trendingRepositoriesForcedService, trendingRepositoriesCachedService);
    }
}
