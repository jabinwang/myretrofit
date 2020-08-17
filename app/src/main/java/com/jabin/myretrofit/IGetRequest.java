package com.jabin.myretrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IGetRequest {
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")
    Call<GetModel> getCall();
}
