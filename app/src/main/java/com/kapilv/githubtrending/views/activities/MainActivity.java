package com.kapilv.githubtrending.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.kapilv.githubtrending.R;
import com.kapilv.githubtrending.databinding.ActivityMainBinding;
import com.kapilv.githubtrending.viewModel.MainViewModel;
import com.kapilv.githubtrending.viewModel.ViewModelFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mBinding;

    @Inject
    ViewModelFactory mViewModelFactory;

    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mViewModel = new ViewModelProvider(this, mViewModelFactory).get(MainViewModel.class);

        mBinding.btnSortMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = getLayoutInflater().inflate(R.layout.layout_sort_popup, null);
                final PopupWindow popup = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
                popup.setOutsideTouchable(true);
                popup.showAsDropDown(view);
                popup.update();
            }
        });
    }
}