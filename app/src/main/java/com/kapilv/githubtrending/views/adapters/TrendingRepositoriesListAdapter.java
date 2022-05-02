package com.kapilv.githubtrending.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kapilv.githubtrending.databinding.LayoutRepositoryListItemBinding;
import com.kapilv.githubtrending.model.data.TrendingRepository;

public class TrendingRepositoriesListAdapter extends ListAdapter<TrendingRepository, TrendingRepositoriesListAdapter.TrendingRepositoriesViewHolder> {


    public static final DiffUtil.ItemCallback<TrendingRepository> DIFF_CALLBACK = new DiffUtil.ItemCallback<TrendingRepository>() {
        @Override
        public boolean areItemsTheSame(@NonNull TrendingRepository oldItem, @NonNull TrendingRepository newItem) {
            return oldItem.getUrl().equals(newItem.getUrl());
        }

        @Override
        public boolean areContentsTheSame(@NonNull TrendingRepository oldItem, @NonNull TrendingRepository newItem) {
            return oldItem.equals(newItem);
        }
    };

    public TrendingRepositoriesListAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public TrendingRepositoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new TrendingRepositoriesViewHolder(LayoutRepositoryListItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingRepositoriesViewHolder holder, int position) {
        holder.mBinding.setRepo(getItem(position));

        holder.mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.mBinding.containerRepoInfo.getVisibility() == View.VISIBLE) holder.mBinding.containerRepoInfo.setVisibility(View.GONE);
                else holder.mBinding.containerRepoInfo.setVisibility(View.VISIBLE);
            }
        });
    }

    static class TrendingRepositoriesViewHolder extends RecyclerView.ViewHolder {

        LayoutRepositoryListItemBinding mBinding;

        public TrendingRepositoriesViewHolder(@NonNull LayoutRepositoryListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
