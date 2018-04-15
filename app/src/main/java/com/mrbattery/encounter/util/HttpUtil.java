package com.mrbattery.encounter.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.mrbattery.encounter.entity.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class HttpUtil {

    private static String responseData;

    public static String getResponseData() {
        return responseData;
    }

    private static String url;

//    public static String sendSyncOkHttpRequest(String address) throws IOException {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(20, TimeUnit.SECONDS)
//                .readTimeout(20, TimeUnit.SECONDS).build();
//        Request request = new Request.Builder().url(address).build();
//        Response response = client.newCall(request).execute();
//
//        return response.body().string();
//    }

    public static String getDatasync(String address) {
        url = address;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                    Request request = new Request.Builder()
                            .url(url)//请求接口。如果需要传参拼接到接口后面。
                            .build();//创建Request 对象
                    Response response = client.newCall(request).execute();//得到Response 对象
                    if (response.isSuccessful()) {
                        responseData = response.body().string();
                        Log.i(TAG, "run: " + responseData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    responseData = "ERROR";
                }
            }
        }).start();
        return responseData;
    }

    public static void getDataAsync(String address, final Context context, final Runnable runnable) {
        url = address;
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
        Request request = new Request.Builder()
                .url(url)//请求接口。如果需要传参拼接到接口后面。
                .build();//创建Request 对象
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseData = response.body().string();
                ((Activity) context).runOnUiThread(runnable);

            }
        });
    }


    public static User parseJSONWithGSON() {
        //使用轻量级的Gson解析得到的json
        Gson gson = new Gson();
        User u = gson.fromJson(responseData, new TypeToken<User>() {
        }.getType());

        //控制台输出结果，便于查看
        Log.d(RoundedDrawable.TAG, "user_id: " + u.getUserID());
        Log.d(RoundedDrawable.TAG, "user_name: " + u.getUserName());
        Log.d(RoundedDrawable.TAG, "user_gender: " + u.getGender());
        Log.d(RoundedDrawable.TAG, "constellation: " + u.getConstellation());
        Log.d(RoundedDrawable.TAG, "script: " + u.getScript());

        Log.d(RoundedDrawable.TAG, "eScore: " + u.geteScore());
        Log.d(RoundedDrawable.TAG, "nScore: " + u.getnScore());
        Log.d(RoundedDrawable.TAG, "pScore: " + u.getpScore());
        Log.d(RoundedDrawable.TAG, "lScore: " + u.getlScore());

        return u;

    }


}
