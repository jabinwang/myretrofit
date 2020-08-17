package com.jabin.myretrofit;

import com.jabin.jnretrofit.Call;
import com.jabin.jnretrofit.http.GET;
import com.jabin.jnretrofit.http.Query;


public interface IGetRequestJnRetrofit {
//    @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")
//    Call<GetModel> getCall();
    @GET("ajax.php")
    Call<GetModel> getCall(@Query("a")String a, @Query("f")String f, @Query("t")String t, @Query("w")String w);
}
