package com.kapilv.githubtrending.di.modules;

import static com.kapilv.githubtrending.utils.constants.Constants.BASE_URL;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    Interceptor provideForceCacheInterceptor() {
        return new Interceptor() {
            @NonNull
            @Override
            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                okhttp3.Response response = chain.proceed(chain.request());
                int maxAge = 60 * 60 * 2; // read from cache for 2 hours even if there is internet connection
                return response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")
                        .build();
            }
        };
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Interceptor interceptor, Cache cache) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addNetworkInterceptor(interceptor);
        client.cache(cache);
        return client.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build();
    }

}