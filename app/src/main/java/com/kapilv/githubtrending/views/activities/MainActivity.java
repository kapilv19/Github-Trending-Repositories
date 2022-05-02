package com.kapilv.githubtrending.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.kapilv.githubtrending.R;
import com.kapilv.githubtrending.databinding.ActivityMainBinding;
import com.kapilv.githubtrending.utils.constants.Constants;
import com.kapilv.githubtrending.viewModel.MainViewModel;
import com.kapilv.githubtrending.viewModel.ViewModelFactory;
import com.kapilv.githubtrending.views.adapters.TrendingRepositoriesListAdapter;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mBinding;

    @Inject
    ViewModelFactory mViewModelFactory;

    private MainViewModel mViewModel;

    private TrendingRepositoriesListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        init();
    }

    private void init() {
        //Initialize ViewModel
        mViewModel = new ViewModelProvider(this, mViewModelFactory).get(MainViewModel.class);

        //Initialize Views
        initViews();

        //Observe Trending Repositories List


        //Fetch Trending Repositories Initially
        mViewModel.fetchTrendingRepositories(false);
    }

    private void initViews() {
        // Setup Trending Repositories RecyclerView
        mBinding.mainRepoListView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TrendingRepositoriesListAdapter();
        mBinding.mainRepoListView.setAdapter(mAdapter);

        // Setup OnClickListener for Sort Menu
        mBinding.btnSortMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = getLayoutInflater().inflate(R.layout.layout_sort_popup, null);
                final PopupWindow popup = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
                popup.setOutsideTouchable(true);
                popup.showAsDropDown(view);
                popup.update();
                popupView.findViewById(R.id.btn_sort_name).setOnClickListener(MainActivity.this);
                popupView.findViewById(R.id.btn_sort_stars).setOnClickListener(MainActivity.this);
            }
        });



        mBinding.mainSwipeRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_sort_name) {
            mViewModel.sortTrendingRepositories(Constants.Sort.NAME);
        } else {
            mViewModel.sortTrendingRepositories(Constants.Sort.STARS);
        }
    }

    @Override
    public void onRefresh() {
        mViewModel.fetchTrendingRepositories(true);
    }
}