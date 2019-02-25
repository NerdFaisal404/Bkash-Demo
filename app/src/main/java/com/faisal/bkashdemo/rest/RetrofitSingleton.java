package com.faisal.bkashdemo.rest;

import android.content.Context;
import android.util.Log;

import com.faisal.bkashdemo.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {

    private static Retrofit mRetrofit;
    private static String HOST = "https://checkout.sandbox.bka.sh/v1.0.0-beta/";



    public synchronized static Retrofit getInstance(Context context) {
        if (mRetrofit == null) {
            createRetrofit(context);
        }
        return mRetrofit;
    }



    public synchronized static Retrofit getInstance() {
        return mRetrofit;
    }





    private static void createRetrofit(Context context) {

        OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();

        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder()
                        .header("Content-Type", "application/json")
                        /*.header("Authorization", SharedPreferenceManager.getInstance().getString(Constants.SharedPreferenceKey.SHAREDPREF_AUTHENTICATION_TOKEN))*/;

                if (Utils.internetCheck(context) ) {
                    requestBuilder
                            .method(request.method(), request.body());
                    return chain.proceed(requestBuilder.build());
                }
                else {
                    throw new IOException("No Internet Connectivity");
                }

            }
        });
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                message -> Log.d("Message_Retrofit", "message: " + message)
        );
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientBuilder.addInterceptor(loggingInterceptor);

        OkHttpClient client = clientBuilder.readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


}
