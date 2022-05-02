package com.kapilv.githubtrending.di.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.kapilv.githubtrending.model.entities.ModelRepository;
import com.kapilv.githubtrending.viewModel.MainViewModel;
import com.kapilv.githubtrending.viewModel.ViewModelFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import javax.inject.Provider;

import dagger.Binds;
import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class ViewModelModule {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }

    @Provides
    ViewModelFactory provideViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap) {
        return new ViewModelFactory(providerMap);
    }
    @Provides
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    ViewModel provideViewModel(ModelRepository modelRepository) {
        return new MainViewModel(modelRepository);
    }
}
