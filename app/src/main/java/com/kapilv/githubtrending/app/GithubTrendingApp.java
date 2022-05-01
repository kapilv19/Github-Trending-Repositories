package com.kapilv.githubtrending.app;

import android.app.Application;

import com.kapilv.githubtrending.di.components.AppComponent;
import com.kapilv.githubtrending.di.components.DaggerAppComponent;
import com.kapilv.githubtrending.di.modules.AppModule;
import com.kapilv.githubtrending.di.modules.NetworkModule;

public class GithubTrendingApp extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger%COMPONENT_NAME%
        mAppComponent = DaggerAppComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(new AppModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .networkModule(new NetworkModule())
                .build();

        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead:
        //  mAppComponent = com.codepath.dagger.components.DaggerAppComponent.create();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
