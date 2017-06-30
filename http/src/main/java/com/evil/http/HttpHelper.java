package com.evil.http;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * @项目名： SuperHttp
 * @包名： com.evil.http
 * @创建者: Noah.冯
 * @时间: 19:39
 * @描述： TODO
 */

public class HttpHelper {
    public static final int    BYTE_SIZE = 8 * 1024;
    public final static String sContentType      = "Content-Type";
    public static final String ContentTypeDefault = "application/x-www-form-urlencoded";
    public static final String BOUNDARY = "-------------------------evil1234567890";
    public static final String TAG      = "com.evil.superhttp";

    /**
     * 失败回调
     *
     * @param callback
     * @param error
     */
    static void Failure(HttpRequestCallback callback, String error) {
        if (callback != null) {
            callback.failure(error);
        } else {
            LOGE(error);
        }
    }

    /**
     * 失败回调
     *
     * @param callback
     * @param startTime
     */
    static void Finish(HttpURLConnection conn, HttpRequestCallback callback, long startTime) {
        if (conn != null) {
            conn.disconnect();
        }
        long entTime = System.currentTimeMillis();
        if (callback != null) {
            long allTime = entTime - startTime;
            callback.finish(allTime);
        }
    }

    //解析http请求响应
    static void response(HttpURLConnection conn, HttpMap httpMap, HttpRequestCallback callback) throws Exception {
        int responseCode = conn.getResponseCode();
        //读取URLConnection的响应
        if (callback != null) {
            if (callback.response(responseCode)) {
                InputStream inputStream = conn.getInputStream();
                String      charsetName = HttpCharset.UTF_8;
                if (httpMap != null) {
                    charsetName = httpMap.getCharsetName();
                }
                String string = HttpHelper.parse(inputStream, charsetName);
                callback.response(responseCode, string);
            }
        }
    }

    static String parseUrl(String url, HttpMap httpMap) {
        if (httpMap != null) {
            return httpMap.setGetParams(url);
        } else {
            return url;
        }
    }

    //解析http请求响应的字符串
    static String parse(InputStream inputStream, String charsetName) throws Exception {
        byte[]                buf          = new byte[BYTE_SIZE];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int                   lenght;
        while ((lenght = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, lenght);
            outputStream.flush();
        }
        //获取服务器返回的字符串
        String string = outputStream.toString(charsetName);
        //关流
        close(outputStream);
        close(inputStream);

        return string;
    }

    /**
     * 获取httpURL连接
     *
     * @param requestUrl
     *
     * @return
     *
     * @throws Exception
     */
    public static HttpURLConnection getHttpURL(String requestUrl) throws Exception {
        URL               url         = new URL(requestUrl);
        HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
        HttpConfig        config      = HttpConfig.getConfig();
        httpUrlConn.setConnectTimeout(config.getConnectTimeout());
        httpUrlConn.setReadTimeout(config.getReadTimeout());
        httpUrlConn.setUseCaches(config.isUseCaches());
        httpUrlConn.setDoOutput(config.isDooutput());
        httpUrlConn.setDoInput(config.isDoinput());
        HashMap<String, String> headers = config.getHeaders();
        for (String key : headers.keySet()) {
            httpUrlConn.setRequestProperty(key, headers.get(key));
        }
        return httpUrlConn;
    }

    static void updateFile(HttpURLConnection conn, HttpMap httpMap) throws IOException {
        OutputStream out = conn.getOutputStream();
        //                    if (httpMap != null) {
        //                        StringBuffer                  strBuf    = new StringBuffer();
        //                        LinkedHashMap<String, Object> paramsMap = httpMap.getParamsMap();
        //                        for (String key : paramsMap.keySet()) {
        //                            strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
        //                            strBuf.append(
        //                                    "Content-Disposition: form-data; name=\"" +
        //                                    key + "\"\r\n\r\n");
        //                            strBuf.append(paramsMap.get(key));
        //                        }
        //                        out.write(strBuf.toString().getBytes());
        //                    }
        if (httpMap != null) {
            HashMap<String, String> files = httpMap.getFiles();
            if (files != null) {
                for (String key : files.keySet()) {
                    //读取文件上传到服务器
                    File file = new File(files.get(key));
                    if (file != null && file.exists()) {
                        String       filename    = file.getName();
                        String       contentType = "image/gif";
                        StringBuffer strBuf      = new StringBuffer();
                        strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                        strBuf.append(
                                "Content-Disposition: form-data; name=\"" +
                                key + "\"; filename=\"" + filename + "\"\r\n");
                        strBuf.append(
                                "Content-Type:" + contentType + "\r\n\r\n");
                        out.write(strBuf.toString().getBytes());
                        DataInputStream in        = new DataInputStream(new FileInputStream(file));
                        int             bytes;
                        byte[]          bufferOut = new byte[1024];
                        while ((bytes = in.read(bufferOut)) != -1) {
                            out.write(bufferOut, 0, bytes);
                            out.flush();
                        }
                        in.close();
                    }
                }
                byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
                out.write(endData);
            }
        }
        out.flush();
        out.close();
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                LOGE(e.getMessage());
            }
        }
    }

    public static void LOGE(Object msg) {
        if (msg != null) {
            Log.e(TAG, String.valueOf(msg));
        } else {
            Log.e(TAG, "Super http message is null!");
        }
    }
}
