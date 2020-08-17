package com.jabin.jnretrofit;

public interface Call<T> {

    void enqueue(Callback<T> callback);
}
