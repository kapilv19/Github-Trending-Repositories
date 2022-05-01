package com.kapilv.githubtrending.di.components;

import com.kapilv.githubtrending.di.modules.AppModule;
import com.kapilv.githubtrending.di.modules.NetworkModule;
import com.kapilv.githubtrending.views.activities.MainActivity;

import javax.inject.Singleton;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(
    modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        NetworkModule.class
    }
)
public interface AppComponent {

    void inject(MainActivity mainActivity);

}
