package com.jabin.jnretrofit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class JnRetrofit {


    private Map<Method, ServiceMethod> serviceMethodMapCache = new LinkedHashMap<>();
    private final String baseUrl;
    private final Call.Factory callFactory;

    JnRetrofit(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.callFactory = builder.callFactory;
    }

    public String baseUrl(){
        return baseUrl;
    }

    public okhttp3.Call.Factory callFactory(){
        return callFactory;
    }


    public <T> T create(final Class<T> service){
        return (T) Proxy.newProxyInstance(service.getClassLoader(),
                new Class<?>[]{service},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.getDeclaringClass() == Object.class){
                            return method.invoke(this,args);
                        }

                        ServiceMethod serviceMethod = loadServiceMethod(method);
                        OkHttpClientCall okHttpClientCall = new OkHttpClientCall(serviceMethod, args);

                        return okHttpClientCall;
                    }
                });
    }

    private ServiceMethod loadServiceMethod(Method method) {

        ServiceMethod result;
        synchronized (serviceMethodMapCache){
            result = serviceMethodMapCache.get(method);
            if (result == null){
                result = new ServiceMethod.Builder(this, method).build();
                serviceMethodMapCache.put(method, result);
            }

        }
        return result;
    }

    public static class Builder{

        private String baseUrl;
        private Call.Factory callFactory;
        public Builder(){

        }

        public Builder callFactory(okhttp3.Call.Factory factory){
            this.callFactory = factory;
            return this;
        }


        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public JnRetrofit build(){
            if (this.callFactory == null) {
                this.callFactory = new OkHttpClient();
            }
            return new JnRetrofit(this);
        }
    }
}
