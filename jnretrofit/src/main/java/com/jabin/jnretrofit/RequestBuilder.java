package com.jabin.jnretrofit;

import android.accounts.Account;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class RequestBuilder {

    private String baseUrl;
    private String relativeUrl;
    private String httpMethod;
    private Object[] args;
    private HttpUrl.Builder httpUrlBuilder;
    ParameterHandler<Object>[] parameterHandlers;

    RequestBuilder(String baseUrl, String relativeUrl, String httpMethod,ParameterHandler<?>[] parameterHandlers,  Object[] args){
        this.baseUrl = baseUrl;
        this.relativeUrl = relativeUrl;
        this.httpMethod = httpMethod;
        this.args = args;
        httpUrlBuilder = HttpUrl.parse(baseUrl + relativeUrl).newBuilder();
        this.parameterHandlers = (ParameterHandler<Object>[]) parameterHandlers;
    }

    void addQueryParam(String key, String value){
        httpUrlBuilder.addEncodedQueryParameter(key, value);
    }

    Request build(){
        if (args != null){
            int count = args.length;
            for (int i = 0; i< count; i++){
                parameterHandlers[i].apply(this, args[i]);
            }
        }


        return new Request.Builder().url(httpUrlBuilder.build()).get().build();
    }
}
