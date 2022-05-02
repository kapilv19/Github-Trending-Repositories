package com.kapilv.githubtrending.viewModel;

import androidx.lifecycle.ViewModel;

import com.kapilv.githubtrending.model.entities.ModelRepository;
import com.kapilv.githubtrending.utils.constants.Constants;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {

    ModelRepository mRepository;

    @Inject
    public MainViewModel(ModelRepository modelRepository) {
        mRepository = modelRepository;
    }

    public void fetchTrendingRepositories(boolean forceRefresh) {

    }


}
