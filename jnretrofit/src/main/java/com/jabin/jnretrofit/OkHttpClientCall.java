package com.jabin.jnretrofit;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

final class OkHttpClientCall<T> implements Call<T>{

    private boolean executed = false;
    private final ServiceMethod<T> serviceMethod;
    private Object[] args;

    OkHttpClientCall(ServiceMethod<T> serviceMethod, Object[] args){
        this.serviceMethod = serviceMethod;
        this.args = args;
    }
    @Override
    public void enqueue(final Callback<T> callback) {
        okhttp3.Call call = createRawCall(args);
        Throwable failure;
        synchronized (this){
            if (executed) {
                throw new IllegalStateException("Already executed");
            }
            executed = true;
        }
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (callback != null){
                    callback.onFailure(OkHttpClientCall.this,e);
                }
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (callback != null) {
                    com.jabin.jnretrofit.Response jnResponse = new com.jabin.jnretrofit.Response();
                    int code = response.code();
//                    String content = response.body().string();
                    jnResponse.body = serviceMethod.toResponse(response.body());
                    callback.onResponse(OkHttpClientCall.this, jnResponse);
                }
            }
        });
    }

    private okhttp3.Call createRawCall(Object[] args){
        Request request = serviceMethod.toRequest(args);
        okhttp3.Call call = serviceMethod.callFactory().newCall(request);
        return call;
    }
}
