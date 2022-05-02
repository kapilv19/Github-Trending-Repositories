package com.kapilv.githubtrending.di.modules;

import com.kapilv.githubtrending.views.activities.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();
}
