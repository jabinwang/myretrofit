package com.jabin.jnretrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.jabin.jnretrofit.http.GET;
import com.jabin.jnretrofit.http.Query;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;
import okhttp3.ResponseBody;

import static android.content.ContentValues.TAG;

public final class ServiceMethod<T> {
    final JnRetrofit jnRetrofit;
    final Method method;
    String httpMethod;
    String relativeUrl;
    final okhttp3.Call.Factory callFactory;
    final ParameterHandler<?>[] parameterHandlers;

    ServiceMethod(Builder<T> builder){
        this.jnRetrofit = builder.jnRetrofit;
        this.callFactory = builder.jnRetrofit.callFactory();
        this.method = builder.method;

        this.httpMethod = builder.httpMethod;
        this.relativeUrl = builder.relativeUrl;
        this.parameterHandlers = builder.parameterHandlers;
    }

    public okhttp3.Call.Factory callFactory(){
        return this.callFactory;
    }

    public Request toRequest(Object[] args) {
        RequestBuilder requestBuilder =
                new RequestBuilder(jnRetrofit.baseUrl(), relativeUrl, httpMethod, parameterHandlers,args);
        return requestBuilder.build();
    }

    public  T toResponse(ResponseBody responseBody) {
        Type returnType = method.getGenericReturnType();
        Class<T> dataClass= (Class<T>) ((ParameterizedType)returnType).getActualTypeArguments()[0];
        Gson gson = new Gson();
//        try {
//            String content = responseBody.string();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        T body = gson.fromJson(responseBody.charStream(), dataClass);
        return body;
    }


    static final class Builder<T> {
        final JnRetrofit jnRetrofit;
        final Method method;
        final Annotation[] methodAnnotation;
        final Annotation[][] parameterAnnotationArray;
        String httpMethod;
        String relativeUrl;
        ParameterHandler<?>[] parameterHandlers;

        public Builder(JnRetrofit jnRetrofit, Method method){
            this.jnRetrofit = jnRetrofit;
            this.method = method;
            this.methodAnnotation = method.getAnnotations();
            this.parameterAnnotationArray = method.getParameterAnnotations();
            parameterHandlers = new ParameterHandler[parameterAnnotationArray.length];
        }

        public ServiceMethod<T> build(){

            for (Annotation annotation : methodAnnotation) {
                parseMethodAnnotation(annotation);
            }
            int count = parameterAnnotationArray.length;
            Log.e(TAG, "build: "+ count );
            for(int i = 0; i< count; i++){
                Annotation parameter = parameterAnnotationArray[i][0];
                if (parameter instanceof Query){
                    parameterHandlers[i] = new ParameterHandler.Query<>(((Query) parameter).value());
                }
            }
            return new ServiceMethod<>(this);
        }

        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof GET){
                parseHttpMethodAndPath("GET", ((GET) annotation).value());
            }
        }

        private void parseHttpMethodAndPath(String httpMethod, String value){
            this.httpMethod = httpMethod;
            this.relativeUrl = value;
        }
    }
}
