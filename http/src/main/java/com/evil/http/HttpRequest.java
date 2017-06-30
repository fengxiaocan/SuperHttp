package com.evil.http;

import java.net.HttpURLConnection;

import static com.evil.http.HttpHelper.BOUNDARY;
import static com.evil.http.HttpHelper.Failure;
import static com.evil.http.HttpHelper.Finish;
import static com.evil.http.HttpHelper.getHttpURL;
import static com.evil.http.HttpHelper.parseUrl;
import static com.evil.http.HttpHelper.sContentType;
import static com.evil.http.HttpHelper.updateFile;

/**
 * @创建者: Noah.冯
 * @时间: 19:18 @描述： TODO
 */
public final class HttpRequest {

    private HttpRequest() {}

    public static void GET(final String url, final HttpMap httpMap, final HttpRequestCallback callback) {
        new Thread(new Runnable() {
            public void run() {
                long              startTime = System.currentTimeMillis();
                HttpURLConnection conn      = null;
                try {
                    conn = getHttpURL(parseUrl(url, httpMap));
                    if (httpMap != null) {
                        httpMap.setHeaders(conn);
                    }
                    conn.setRequestMethod(HttpMethod.GET);
                    conn.connect();
                    HttpHelper.response(conn, httpMap, callback);
                } catch (Exception e) {
                    Failure(callback, e.getMessage());
                } finally {
                    Finish(conn, callback, startTime);
                }
            }
        }).start();
    }

    public static void GET(String url, HttpRequestCallback callback) {
        GET(url, null, callback);
    }

    public static void GET(String url, HttpMap httpMap) {
        GET(url, httpMap, null);
    }

    public static void GET(String url) {
        GET(url, null, null);
    }

    public static void POST(final String url, final HttpMap httpMap, final HttpRequestCallback callback) {
        if (httpMap != null) {
            if (httpMap.isHasFile()) {
                POSTFile(url, httpMap, callback);
                return;
            }
        }
        new Thread(new Runnable() {
            public void run() {
                long              startTime = System.currentTimeMillis();
                HttpURLConnection conn      = null;
                try {
                    conn = getHttpURL(parseUrl(url, httpMap));
                    conn.setRequestMethod(HttpMethod.POST);
                    if (httpMap != null) {
                        httpMap.setHeaders(conn);
                        httpMap.setPostParams(conn);
                    }
                    conn.connect();
                    HttpHelper.response(conn, httpMap, callback);
                } catch (Exception e) {
                    Failure(callback, e.getMessage());
                } finally {
                    Finish(conn, callback, startTime);
                }
            }
        }).start();
    }


    public static void POSTFile(final String url, final HttpMap httpMap, final HttpRequestCallback callback) {
        new Thread(new Runnable() {
            public void run() {
                long              startTime = System.currentTimeMillis();
                HttpURLConnection conn      = null;
                try {
                    conn = getHttpURL(parseUrl(url, httpMap));
                    conn.setRequestMethod(HttpMethod.POST);
                    if (httpMap != null) {
                        httpMap.setHeaders(conn);
                        httpMap.setPostParams(conn);
                    }
                    conn.setRequestProperty(sContentType,
                            "multipart/form-data; boundary=" + BOUNDARY);
                    conn.connect();
                    updateFile(conn, httpMap);
                    HttpHelper.response(conn, httpMap, callback);
                } catch (Exception e) {
                    Failure(callback, e.getMessage());
                } finally {
                    Finish(conn, callback, startTime);
                }
            }
        }).start();
    }


    public static void POST(String url, HttpRequestCallback callback) {
        POST(url, null, callback);
    }


    public static void POST(String url, HttpMap httpMap) {
        POST(url, httpMap, null);
    }

    public static void POST(String url) {
        POST(url, null, null);
    }


}
