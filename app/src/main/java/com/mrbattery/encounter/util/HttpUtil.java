package com.mrbattery.encounter.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.mrbattery.encounter.entity.MatchedUser;
import com.mrbattery.encounter.entity.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.makeramen.roundedimageview.RoundedDrawable.TAG;
import static java.security.AccessController.getContext;

public class HttpUtil {

    private  String responseData;

    public  String getResponseData() {
        return responseData;
    }

    private static Bitmap responseBmp;


    public static Bitmap getResponseBmp() {
        return responseBmp;
    }

    private String url;

/*    public static String getDataSync(String address) {
        url = address;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS).build();//创建OkHttpClient对象
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
                    responseData = "network_error";
                }
            }
        }).start();
        return responseData;

    }*/

    public void getDataAsync(String address, final Context context, final Runnable runnable) {
        url = address;
        Log.i(TAG, "getDataAsync: 开始创建OkHttpClient对象");
        OkHttpClient client = new OkHttpClient.Builder()//创建OkHttpClient对象
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();
        Log.i(TAG, "getDataAsync: 创建OkHttpClient对象成功");
        Log.i(TAG, "getDataAsync: 开始请求接口");
        final Request request = new Request.Builder()
                .url(url)//请求接口。如果需要传参拼接到接口后面。
                .build();//创建Request 对象
        Log.i(TAG, "getDataAsync: 请求接口成功");
        Log.i(TAG, "getDataAsync: 开始回调函数");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "getDataAsync: 回调函数失败");
                responseData = "network_error";
                Log.i(TAG, "getDataAsync: 回调函数runOnUiThread开始");
                ((Activity) context).runOnUiThread(runnable);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "getDataAsync: 回调函数响应");
                responseData = response.body().string();
                Log.i(TAG, "getDataAsync: 回调函数runOnUiThread开始");
                ((Activity) context).runOnUiThread(runnable);

            }
        });
    }

/*    public static void getPictureAsync(String address, final Context context, final Runnable runnable) {
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
                //response的body是图片的byte字节
                byte[] bytes = response.body().bytes();
                //把byte字节组装成图片
                responseBmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ((Activity) context).runOnUiThread(runnable);

            }
        });
    }*/

/*    public static User parseJSONWithGSON(String responseData) {
        //使用轻量级的Gson解析得到的json
        Gson gson = new Gson();
        if (responseData.equals("network_error")) {
            return null;
        } else {
            User u = gson.fromJson(responseData, new TypeToken<User>() {
            }.getType());

            //控制台输出结果，便于查看
            Log.i(TAG, "parseJSONWithGSON: " + u.toString());

            return u;
        }

    }*/

/*    public static ArrayList<MatchedUser> parseJSONListWithGSON() {
        Gson gson = new Gson();
        if (responseData.equals("network_error")) {
            return null;
        } else {
            ArrayList<MatchedUser> matchedUsers = gson.fromJson(responseData, new TypeToken<ArrayList<MatchedUser>>() {
            }.getType());
            for (MatchedUser matchedUser : matchedUsers) {
                Log.i(TAG, "parseJSONListWithGSON: " + matchedUser.toString());
            }
            return matchedUsers;
        }
    }*/

}
