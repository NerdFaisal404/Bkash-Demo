package com.faisal.bkashdemo.rest;


import org.json.JSONObject;

import io.reactivex.Flowable;
import retrofit2.http.POST;

public interface Endpoints {

    @POST("checkout")
    Flowable<JSONObject> registerToServices();

    @POST("payment/create")
    Flowable<JSONObject> createPayment();

    @POST("payment/execute/")
    Flowable<JSONObject> excuteTransaction();

    @POST("payment/capture/")
    Flowable<JSONObject> captureTransaction();

    @POST("token/grant")
    Flowable<JSONObject> tokenGrant();

    @POST("token/refresh")
    Flowable<JSONObject> tokenRefresh();
}
