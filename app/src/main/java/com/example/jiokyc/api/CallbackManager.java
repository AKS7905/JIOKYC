package com.example.jiokyc.api;

import android.content.Intent;
import android.util.Log;

import java.net.UnknownHostException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public abstract class CallbackManager<T> implements Callback<T> {

    private HashMap<String, String> mRequestHeaderMap = new HashMap<>();
private CallbackManager callbackManager;
    protected CallbackManager() {
        initHeaders();
    }


    private void initHeaders() {
        mRequestHeaderMap.put("app-type", "M");
        mRequestHeaderMap.put("Content-Type", "application/json");
        mRequestHeaderMap.put("Authorization", "");
    }


    @Override
    public void onResponse(Call call, Response response) {

        try {

            if (response.isSuccessful()) {
                if (response.body() != null) {
                    onSuccess(response.body());
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                } else {
                    onSuccess(response.headers());
                    Log.i(TAG, "post submitted to API." + response.headers().toString());
                }

            } else {
                onError(new RetroError(RetroError.Kind.HTTP, "Unable to connect to server. Try after sometime.", -999));
            }
        } catch (Exception e) {
            Log.i(TAG, "post submitted to API." + response.headers().toString());

        }
    }


    @Override
    public void onFailure(Call call, Throwable throwable) {
        if (throwable instanceof UnknownHostException) {
            onError(new RetroError(RetroError.Kind.NETWORK, "Unable to connect to server.", -999));
        } else {
            onError(new RetroError(RetroError.Kind.UNEXPECTED, throwable.getMessage(), -999));
        }
    }

    protected abstract void onSuccess(Object o);

    protected abstract void onError(RetroError retroError);


}
