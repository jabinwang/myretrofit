package com.jabin.jnretrofit;

public interface ParameterHandler<T> {
    public void apply(RequestBuilder requestBuilder, T value);

    class Query<T> implements ParameterHandler<T>{
        private String key;
        public Query(String key){
            this.key = key;
        }
        @Override
        public void apply(RequestBuilder requestBuilder, T value) {
            requestBuilder.addQueryParam(key, value.toString());
        }
    }
}
