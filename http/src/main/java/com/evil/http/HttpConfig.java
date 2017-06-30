package com.evil.http;

import java.util.HashMap;

import static com.evil.http.HttpSimulator.ANDROID_WEB_SIMULATOR;

/**
 * @项目名： SuperHttp
 * @包名： com.evil.http
 * @创建者: Noah.冯
 * @时间: 9:37
 * @描述： TODO
 */

public final class HttpConfig{

    //最大读取超时时间
    public static final int MAX_READ_TIMEOUT = 5000;
    //最大连接超时时间
    public static final int MAX_CONNECT_TIMEOUT = 5000;

    private static HttpConfig sHttpConfig;

    private int mConnectTimeout = MAX_CONNECT_TIMEOUT;

    private int mReadTimeout = MAX_READ_TIMEOUT;
    //是否使用缓存
    private boolean useCaches = false;
    //是否要获取连接输入流
    private boolean doinput = true;
    //是否要获取连接输出流
    private boolean dooutput = true;
    //请求模拟器标识
    private String RequestSimulator = ANDROID_WEB_SIMULATOR;

    private HashMap<String,String> mHeaderMap;


    public int getConnectTimeout(){
        return mConnectTimeout;
    }

    public int getReadTimeout(){
        return mReadTimeout;
    }

    public boolean isUseCaches(){
        return useCaches;
    }

    public boolean isDoinput(){
        return doinput;
    }

    public boolean isDooutput(){
        return dooutput;
    }

    public String getRequestSimulator(){
        return RequestSimulator;
    }

    public HttpConfig setUA(@HttpSimulator.TYPE String ua){
        RequestSimulator = ua; return sHttpConfig;
    }

    public void setConnectTimeout(int connectTimeout){
        mConnectTimeout = connectTimeout;
    }

    public void setReadTimeout(int readTimeout){
        mReadTimeout = readTimeout;
    }

    public HttpConfig setUseCaches(boolean useCaches){
        this.useCaches = useCaches; return sHttpConfig;
    }


    public HttpConfig setDoinput(boolean doinput){
        this.doinput = doinput; return sHttpConfig;
    }

    public HttpConfig setDooutput(boolean dooutput){
        this.dooutput = dooutput; return sHttpConfig;
    }

    private HttpConfig(){
        mHeaderMap = new HashMap<>();
    }

    public void addHeader(String key,String header){
        mHeaderMap.put(key,header);
    }

    public HashMap<String,String> getHeaders(){
        return mHeaderMap;
    }

    public static HttpConfig getConfig(){
        synchronized(HttpConfig.class){
            if(sHttpConfig == null){
                synchronized(HttpConfig.class){
                    sHttpConfig = new HttpConfig();
                    sHttpConfig.addHeader("Connection","Keep-Alive");
                    sHttpConfig.addHeader("User-Agent",ANDROID_WEB_SIMULATOR);
                }
            }
        } return sHttpConfig;
    }
}
