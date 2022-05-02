package com.kapilv.githubtrending.app;

import android.app.Application;

import com.kapilv.githubtrending.di.components.AppComponent;
import com.kapilv.githubtrending.di.components.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class GithubTrendingApp extends Application implements HasAndroidInjector {

    private AppComponent mAppComponent;

    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent
                .builder()
                .application(this)
                .build();
        mAppComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }
}
