package com.jabin.jnretrofit;

public interface CallAdapter<T> {

   <R> T adapt(Call<R> call);

   abstract class Factory{

   }
}
