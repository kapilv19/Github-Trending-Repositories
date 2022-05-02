package com.kapilv.githubtrending.model.data;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.SerializedName;
import com.kapilv.githubtrending.R;

import java.util.List;
import java.util.Objects;

public class TrendingRepository {

    @SerializedName("author")
    private String author;

    @SerializedName("name")
    private String name;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("url")
    private String url;

    @SerializedName("description")
    private String description;

    @SerializedName("language")
    private String language;

    @SerializedName("languageColor")
    private String languageColor;

    @SerializedName("stars")
    private int stars;

    @SerializedName("forks")
    private int forks;

    @SerializedName("currentPeriodStars")
    private int currentPeriodStars;

    @SerializedName("builtBy")
    private List<GithubUser> builtBy = null;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguageColor() {
        return languageColor;
    }

    public void setLanguageColor(String languageColor) {
        this.languageColor = languageColor;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Integer getForks() {
        return forks;
    }

    public void setForks(Integer forks) {
        this.forks = forks;
    }

    public Integer getCurrentPeriodStars() {
        return currentPeriodStars;
    }

    public void setCurrentPeriodStars(Integer currentPeriodStars) {
        this.currentPeriodStars = currentPeriodStars;
    }

    public List<GithubUser> getBuiltBy() {
        return builtBy;
    }

    public void setBuiltBy(List<GithubUser> builtBy) {
        this.builtBy = builtBy;
    }

    @BindingAdapter({ "avatar" })
    public static void loadImage(ImageView imageView, String imageURL) {
        Glide.with(imageView.getContext())
                .setDefaultRequestOptions(new RequestOptions()
                        .circleCrop())
                .load(imageURL)
                .placeholder(R.drawable.drawable_icon_avatar)
                .into(imageView);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrendingRepository that = (TrendingRepository) o;
        return stars == that.stars &&
                forks == that.forks &&
                currentPeriodStars == that.currentPeriodStars &&
                author.equals(that.author) &&
                name.equals(that.name) &&
                avatar.equals(that.avatar) &&
                url.equals(that.url) &&
                description.equals(that.description) &&
                language.equals(that.language) &&
                languageColor.equals(that.languageColor) &&
                builtBy.equals(that.builtBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, name, url, description, language);
    }
}
