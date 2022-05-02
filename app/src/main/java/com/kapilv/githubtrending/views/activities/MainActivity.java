package com.kapilv.githubtrending.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.kapilv.githubtrending.R;
import com.kapilv.githubtrending.databinding.ActivityMainBinding;
import com.kapilv.githubtrending.model.data.GitHubRepository;
import com.kapilv.githubtrending.utils.constants.Constants;
import com.kapilv.githubtrending.viewModel.MainViewModel;
import com.kapilv.githubtrending.viewModel.ViewModelFactory;
import com.kapilv.githubtrending.views.adapters.TrendingRepositoriesListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mBinding;

    @Inject
    ViewModelFactory mViewModelFactory;

    //Main View Model
    private MainViewModel mViewModel;

    //List Adapter to populate repos in repository recycler view
    private TrendingRepositoriesListAdapter mAdapter;

    //Observer to observe updates in trending repository list from view model
    private Observer<List<GitHubRepository>> mTrendingRepositoriesObserver;

    //Observer to observe updates in errors from view model
    private Observer<String> mErrorObserver;

    //Flag to check if network call has been done once after application start or not
    private boolean isFetchedOnce = false;

    private Parcelable mRVState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Inject Dependencies using Android Injector provided in Dagger Android
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        //Create Main Layout Bindings using Data Binding Utility
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //Check if any instance (either landscape or portrait) was created before current
        if (savedInstanceState != null) {
            //Since there was an instance before then network call shouldn't be made again so setting isFetchedOnce to saved value
            isFetchedOnce = savedInstanceState.getBoolean("isFetchedOnce", false);
            mRVState = savedInstanceState.getParcelable("rvState");
        }

        //Initialize Everything
        init();
    }

    private void init() {
        //Initialize ViewModel
        mViewModel = new ViewModelProvider(this, mViewModelFactory).get(MainViewModel.class);

        //Bind Main Activity Layout Binding variable to Main View Model
        mBinding.setLifecycleOwner(this);
        mBinding.setViewModel(mViewModel);

        //Bind Error View Layout Binding variable to Main View Model
        mBinding.mainErrorView.setLifecycleOwner(this);
        mBinding.mainErrorView.setViewModel(mViewModel);

        //Initialize Views
        initViews();

        //Initialize Observers
        initObservers();

        //Observe Trending Repositories List
        mViewModel.trendingRepositoriesSubscription().observe(this, mTrendingRepositoriesObserver);
        mViewModel.errorSubscription().observe(this, mErrorObserver);

        //Fetch Trending Repositories Initially
        if (!isFetchedOnce) mViewModel.fetchTrendingRepositories(false);
    }

    private void initViews() {
        // Setup Trending Repositories RecyclerView Layout manager and Adapter (as our custom List Adapter TrendingRepositoriesListAdapter)
        mBinding.mainRepoListView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TrendingRepositoriesListAdapter();
        mBinding.mainRepoListView.setAdapter(mAdapter);
        if (mRVState != null) mBinding.mainRepoListView.getLayoutManager().onRestoreInstanceState(mRVState);

        // Setup OnClickListener for Sort Menu
        mBinding.btnSortMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Inflate custom popup view from given layout
                View popupView = getLayoutInflater().inflate(R.layout.layout_sort_popup, null);

                //Create a new popup window to display our custom popup menu
                final PopupWindow popup = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

                //Set popup to close when clicked outside view bounds
                popup.setOutsideTouchable(true);
                popup.showAsDropDown(view);
                popup.update();

                //Set OnClick listeners for Popup buttons (sort by name and sort by stars buttons)
                popupView.findViewById(R.id.btn_sort_name).setOnClickListener(MainActivity.this);
                popupView.findViewById(R.id.btn_sort_stars).setOnClickListener(MainActivity.this);
            }
        });

        //Set listener for case when swipe refresh is pulled down by user
        mBinding.mainSwipeRefresh.setOnRefreshListener(this);
    }

    //Init Observers to observe data from view model
    private void initObservers() {
        //Observes trending repositories list from the view model and update our list adapter with fresh list of trending repos
        mTrendingRepositoriesObserver = new Observer<List<GitHubRepository>>() {
            @Override
            public void onChanged(List<GitHubRepository> gitHubRepositories) {
                Log.e(TAG, "Fetched!");
                mAdapter.submitList(new ArrayList<>(gitHubRepositories));
            }
        };

        //Observes Error messages from view model and display them as toasts
        mErrorObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "Error! " + s);
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        };
    }

    //Handles click events from sort popup menu
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_sort_name) {
            //Asks view model to sort repo according to name
            mViewModel.sortTrendingRepositories(Constants.Sort.NAME);
        } else {
            //Asks view model to sort repo according to stars
            mViewModel.sortTrendingRepositories(Constants.Sort.STARS);
        }
    }

    //Handles swipe refresh event from swipe refresh view
    @Override
    public void onRefresh() {
        //Triggers view model to force fetch repositories
        mViewModel.fetchTrendingRepositories(true);
        mBinding.mainSwipeRefresh.setRefreshing(false);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save state that repos have already been fetched once since application start
        outState.putBoolean("isFetchedOnce", true);
        outState.putParcelable("rvState", Objects.requireNonNull(mBinding.mainRepoListView.getLayoutManager()).onSaveInstanceState());
    }
}