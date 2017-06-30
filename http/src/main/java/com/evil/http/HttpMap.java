package com.evil.http;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.evil.http.HttpHelper.ContentTypeDefault;
import static com.evil.http.HttpRequest.sContentType;

/**
 * @项目名： SuperHttp
 * @包名： com.evil.http
 * @创建者: Noah.冯
 * @时间: 18:35
 * @描述： TODO
 */

public class HttpMap {
    private LinkedHashMap<String, Object> mParamsMap;
    private LinkedHashMap<String, String> mHeadMap;
    private LinkedHashMap<String, Object> mSuffixHashMap;
    private HashMap<String, String>         mFileHashMap;
    private boolean isHasFile = false;
    private String charsetName = HttpCharset.UTF_8;

    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    public HttpMap() {
        mParamsMap = new LinkedHashMap<>();
    }

    public static HttpMap get() {
        return new HttpMap();
    }

    public HttpMap add(String key, String value) {
        mParamsMap.put(key, value);
        return this;
    }

    public HttpMap add(String key, int value) {
        mParamsMap.put(key, value);
        return this;
    }

    public HttpMap add(String key, long value) {
        mParamsMap.put(key, value);
        return this;
    }

    public HttpMap add(String key, boolean value) {
        mParamsMap.put(key, value);
        return this;
    }

    public HttpMap add(String key, double value) {
        mParamsMap.put(key, value);
        return this;
    }

    public HttpMap add(String key, float value) {
        mParamsMap.put(key, value);
        return this;
    }

    public HttpMap add(String key, File value) {
        if (mFileHashMap == null) {
            mFileHashMap = new HashMap<>();
            addHeader(sContentType, ContentTypeDefault);
        }
        isHasFile = true;
        mFileHashMap.put(key, value.getAbsolutePath());
        return this;
    }

    public HttpMap addFile(String key, String value) {
        if (value == null){
            return this;
        }
        if (mFileHashMap == null) {
            mFileHashMap = new HashMap<>();
            addHeader(sContentType, ContentTypeDefault);
        }
        isHasFile = true;
        mFileHashMap.put(key, value);
        return this;
    }

    public HttpMap add(String key, File value, String ContentType) {
        if (value == null){
            return this;
        }
        if (value == null){
            return this;
        }
        if (mFileHashMap == null) {
            mFileHashMap = new HashMap<>();
        }
        addHeader(sContentType, ContentType);
        isHasFile = true;
        mFileHashMap.put(key, value.getAbsolutePath());
        return this;
    }

    public HttpMap addFile(String key, String fileValue, String ContentType) {
        if (fileValue == null){
            return this;
        }
        if (mFileHashMap == null) {
            mFileHashMap = new HashMap<>();
        }
        addHeader(sContentType, ContentType);
        isHasFile = true;
        mFileHashMap.put(key, fileValue);
        return this;
    }


    boolean isHasFile() {
        return isHasFile;
    }

    public HttpMap addSuffix(String key, String value) {
        if (mSuffixHashMap == null) {
            mSuffixHashMap = new LinkedHashMap<>();
        }
        mSuffixHashMap.put(key, value);
        return this;
    }

    public HttpMap addSuffix(String key, int value) {
        if (mSuffixHashMap == null) {
            mSuffixHashMap = new LinkedHashMap<>();
        }
        mSuffixHashMap.put(key, value);
        return this;
    }

    public HttpMap addSuffix(String key, long value) {
        if (mSuffixHashMap == null) {
            mSuffixHashMap = new LinkedHashMap<>();
        }
        mSuffixHashMap.put(key, value);
        return this;
    }

    public HttpMap addSuffix(String key, boolean value) {
        if (mSuffixHashMap == null) {
            mSuffixHashMap = new LinkedHashMap<>();
        }
        mSuffixHashMap.put(key, value);
        return this;
    }

    public HttpMap addSuffix(String key, double value) {
        if (mSuffixHashMap == null) {
            mSuffixHashMap = new LinkedHashMap<>();
        }
        mSuffixHashMap.put(key, value);
        return this;
    }

    public HttpMap addSuffix(String key, float value) {
        if (mSuffixHashMap == null) {
            mSuffixHashMap = new LinkedHashMap<>();
        }
        mSuffixHashMap.put(key, value);
        return this;
    }

    public HttpMap addHeader(String key, String value) {
        if (mHeadMap == null) {
            mHeadMap = new LinkedHashMap<>();
        }
        mHeadMap.put(key, value);
        return this;
    }


    public String getParams() {
        return setGetParams("");
    }

    String setGetParams(String url) {
        if (mParamsMap.size() == 0 && mSuffixHashMap.size() == 0) {
            return url;
        }
        StringBuffer buffer = new StringBuffer(url);
        buffer.append("?");
        if (mSuffixHashMap != null && mSuffixHashMap.size() > 0) {
            for (String key : mSuffixHashMap.keySet()) {
                buffer.append(key);
                buffer.append("=");
                Object o = mSuffixHashMap.get(key);
                buffer.append(o);
                buffer.append("&");
            }
        }
        if (mParamsMap.size() > 0) {
            for (String key : mParamsMap.keySet()) {
                buffer.append(key);
                buffer.append("=");
                Object o = mParamsMap.get(key);
                buffer.append(o);
                buffer.append("&");
            }
        }
        buffer.delete(buffer.length() - 1, buffer.length());
        return buffer.toString();
    }

    void setHeaders(HttpURLConnection httpURL) {
        if (mHeadMap == null || mHeadMap.size() == 0) {
            return;
        }
        for (String key : mHeadMap.keySet()) {
            httpURL.setRequestProperty(key, mHeadMap.get(key));
        }
    }

    void setPostParams(HttpURLConnection httpURL) {
        if (mParamsMap.size() == 0) {
            return;
        }
        for (String key : mParamsMap.keySet()) {
            httpURL.setRequestProperty(key, String.valueOf(mParamsMap.get(key)));
        }
    }

    public LinkedHashMap<String, Object> getParamsMap() {
        return mParamsMap;
    }

    HashMap<String, String> getFiles() {
        return mFileHashMap;
    }

    public void clear() {
        mParamsMap.clear();
        if (mSuffixHashMap != null) {
            mSuffixHashMap.clear();
            mSuffixHashMap = null;
        }
        if (mFileHashMap != null) {
            mFileHashMap.clear();
            mFileHashMap = null;
        }
    }
}
