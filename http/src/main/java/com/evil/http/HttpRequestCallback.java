package com.evil.http;

public interface HttpRequestCallback {

    void response(int responseCode,String respone);

    boolean response(int responseCode);//返回false则不执行response(int responseCode,String respone)方法

    void failure(String error);

    void finish(long elapsedTime);
}
