package com.kapilv.githubtrending.di.modules;

import android.app.Application;
import android.content.Context;

import com.kapilv.githubtrending.viewModel.MainViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    public MainViewModel provideMainViewModel() {
        return new MainViewModel();
    }
}
