package com.kapilv.githubtrending.di.components;

import com.kapilv.githubtrending.di.modules.AppModule;
import com.kapilv.githubtrending.di.modules.NetworkModule;
import com.kapilv.githubtrending.viewModel.MainViewModel;
import com.kapilv.githubtrending.views.activities.MainActivity;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(
    modules = {
        AppModule.class,
        NetworkModule.class
    }
)
public interface AppComponent {

    void inject(MainActivity mainActivity);
    void inject(MainViewModel mainViewModel);
}
