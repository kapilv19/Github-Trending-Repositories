package com.kapilv.githubtrending.utils.constants;

public class Constants {

    public enum Status {
        LOADING,
        LOADED,
        ERROR
    }

    public enum Sort {
        NONE,
        NAME,
        STARS
    }

    public static final String BASE_URL = "https://github-trending-api-wonder.herokuapp.com/";
}
