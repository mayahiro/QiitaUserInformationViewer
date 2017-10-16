package xyz.mayahiro.qiitauserinformationviewer.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mayahiro on 10/16/17.
 */

public class Network {
    public static Network instance = new Network();
    private Retrofit adapter;

    public static Network instance() {
        return instance;
    }

    private Network() {
        createAdapter();
    }

    private void createAdapter() {
        adapter = new Retrofit.Builder()
                .client(new OkHttpClient.Builder().build())
                .baseUrl("https://qiita.com/api/v2/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()))
                .build();
    }

    public QiitaApiService qiitaApiService() {
        return adapter.create(QiitaApiService.class);
    }
}
