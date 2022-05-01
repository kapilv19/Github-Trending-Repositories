package com.kapilv.githubtrending.model.data;

import com.google.gson.annotations.SerializedName;

public class GithubUser {

    @SerializedName("username")
    private String username;

    @SerializedName("href")
    private String href;

    @SerializedName("avatar")
    private String avatar;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
