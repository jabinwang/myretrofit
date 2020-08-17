package com.jabin.myretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jabin.jnretrofit.JnRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        testRetrofit2();
        testJnRetrofit();
    }

    private void testJnRetrofit(){
        JnRetrofit jnRetrofit = new JnRetrofit.Builder().setBaseUrl("http://fy.iciba.com/")
                .build();

        IGetRequestJnRetrofit request = jnRetrofit.create(IGetRequestJnRetrofit.class);
//        com.jabin.jnretrofit.Call<GetModel> call = request.getCall();
        com.jabin.jnretrofit.Call<GetModel> call = request.getCall("fy","auto","auto","hello%20world");
        call.enqueue(new com.jabin.jnretrofit.Callback<GetModel>() {
            @Override
            public void onResponse(com.jabin.jnretrofit.Call<GetModel> call, com.jabin.jnretrofit.Response<GetModel> response) {
                Log.e(TAG, "onResponse: " + " " + response.body.toString());

            }

            @Override
            public void onFailure(com.jabin.jnretrofit.Call<GetModel> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);

            }
        });
    }

    private void testRetrofit2(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://fy.iciba.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IGetRequest request = retrofit.create(IGetRequest.class);
        Call<GetModel> call = request.getCall();
        call.enqueue(new Callback<GetModel>() {
            @Override
            public void onResponse(Call<GetModel> call, Response<GetModel> response) {
                Log.e(TAG, "onResponse: " + response.code() + " " + response.body().toString());

            }

            @Override
            public void onFailure(Call<GetModel> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}