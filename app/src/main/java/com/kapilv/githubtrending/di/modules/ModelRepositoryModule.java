package com.kapilv.githubtrending.di.modules;

import com.kapilv.githubtrending.model.entities.ModelRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ModelRepositoryModule {

    @Provides
    @Singleton
    ModelRepository provideModelRepository(Retrofit retrofit) {
        return new ModelRepository(retrofit);
    }
}
