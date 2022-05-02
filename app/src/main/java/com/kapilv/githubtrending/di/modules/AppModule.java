package com.kapilv.githubtrending.di.modules;

import android.app.Application;
import android.content.Context;

import com.kapilv.githubtrending.viewModel.MainViewModel;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

//    Application mApplication;
//
//    public AppModule(Application application) {
//        mApplication = application;
//    }

    @Provides
    @Singleton
    Context providesContext(Application application) {
        return application.getApplicationContext();
    }
}
