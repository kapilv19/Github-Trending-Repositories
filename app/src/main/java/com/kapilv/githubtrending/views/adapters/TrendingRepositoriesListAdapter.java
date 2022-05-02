package com.kapilv.githubtrending.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kapilv.githubtrending.R;
import com.kapilv.githubtrending.databinding.LayoutRepositoryListItemBinding;
import com.kapilv.githubtrending.model.data.GitHubRepository;

public class TrendingRepositoriesListAdapter extends ListAdapter<GitHubRepository, TrendingRepositoriesListAdapter.TrendingRepositoriesViewHolder> {


    public static final DiffUtil.ItemCallback<GitHubRepository> DIFF_CALLBACK = new DiffUtil.ItemCallback<GitHubRepository>() {
        //Terms repos same if their URL is same
        @Override
        public boolean areItemsTheSame(@NonNull GitHubRepository oldItem, @NonNull GitHubRepository newItem) {
            return oldItem.getUrl().equals(newItem.getUrl());
        }

        @Override
        public boolean areContentsTheSame(@NonNull GitHubRepository oldItem, @NonNull GitHubRepository newItem) {
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
        //Generate List Item View Holder using data binding to list item layout
        return new TrendingRepositoriesViewHolder(LayoutRepositoryListItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingRepositoriesViewHolder holder, int position) {
        //Binds repo data to the current list item view holder
        holder.bind(getItem(position));
    }

    static class TrendingRepositoriesViewHolder extends RecyclerView.ViewHolder {

        LayoutRepositoryListItemBinding mBinding;

        public TrendingRepositoriesViewHolder(@NonNull LayoutRepositoryListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(GitHubRepository item) {
            //Sets binding variable as current item's repo object
            mBinding.setRepo(item);
            mBinding.executePendingBindings();

            // Sets listener to toggle the list item on click anywhere on it
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mBinding.containerRepoInfo.getVisibility() == View.VISIBLE) mBinding.containerRepoInfo.setVisibility(View.GONE);
                    else mBinding.containerRepoInfo.setVisibility(View.VISIBLE);
                }
            });

            //Loads the avatar of author in background using glide and assigns it to avatar image view
            if (item.getAvatar() != null) {
                try {
                    Glide.with(mBinding.ivAvatar.getContext())
                            .setDefaultRequestOptions(new RequestOptions()
                                    .circleCrop())
                            .load(item.getAvatar())
                            .placeholder(R.drawable.drawable_icon_avatar)
                            .into(mBinding.ivAvatar);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //Changes the language colour according to fetched color
            if (item.getLanguageColor() != null) {
                try {
                    mBinding.ivLang.setColorFilter(Color.parseColor(item.getLanguageColor()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
