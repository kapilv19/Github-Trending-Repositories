package com.kapilv.githubtrending.di.components;

import android.app.Application;

import com.kapilv.githubtrending.app.GithubTrendingApp;
import com.kapilv.githubtrending.di.modules.ActivityModule;
import com.kapilv.githubtrending.di.modules.AppModule;
import com.kapilv.githubtrending.di.modules.ModelRepositoryModule;
import com.kapilv.githubtrending.di.modules.NetworkModule;
import com.kapilv.githubtrending.di.modules.ViewModelModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(
    modules = {
        AndroidInjectionModule.class,
        ActivityModule.class,
        AppModule.class,
        NetworkModule.class,
        ViewModelModule.class,
        ModelRepositoryModule.class
    }
)
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(GithubTrendingApp app);
}
