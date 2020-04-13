package com.example.myapplication.mvp.model;


import android.text.TextUtils;

import com.example.myapplication.app.App;
import com.example.myapplication.callback.IDataCallBack;
import com.example.myapplication.di.component.DaggerRxOperateComponent;
import com.example.myapplication.mvp.model.api.ApiService;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;

/**
 * RxJava和OkHttp关联的封装类
 */
public class RxOperateImpl {
    @Inject
    ApiService mApiService;
    private RequestBody body;
    private MultipartBody.Part filePart;

    public RxOperateImpl() {
        //将apiService注入到RxOperateImpl里面

        DaggerRxOperateComponent.builder().
                appComponent(App.daggerAppComponent()).build().
                inject(this);
    }

    /**
     * @param url          get请求的URL地址
     * @param dataCallBack 结果回调(实际上是接口回调)
     * @param <T>          接口回调获取的值
     */
    public <T> void requestData(String url, IDataCallBack<T> dataCallBack) {
        RxSchedulersOperator.retryWhenOperator(mApiService.requestData(url)).
                subscribe(getObserver(dataCallBack));
    }

    //get请求的url
    public <T> void requestData(String url, Map<String, T> map, IDataCallBack<T> dataCallBack) {
        if (map != null || map.size() == 0) {
            requestData(url, dataCallBack);
        } else {
            RxSchedulersOperator.retryWhenOperator(mApiService.requestData(url, map))
                    .subscribe(getObserver(dataCallBack));
        }
    }

    //没有参数的post请求
    public <T> void requestFormData(String url, IDataCallBack<T> dataCallBack) {
        RxSchedulersOperator.retryWhenOperator(mApiService.requestFormData(url))
                .subscribe(getObserver(dataCallBack));
    }

    //有参数的post请求
    public <T> void requestFormData(String url, Map<String, T> map, IDataCallBack<T> dataCallBack) {
        if (map != null || map.size() == 0) {
            requestFormData(url, dataCallBack);
        } else
            RxSchedulersOperator.retryWhenOperator(mApiService.requestFormData(url, map))
                    .subscribe(getObserver(dataCallBack));
    }


    //文件下载
    public <T> void DownLoad(String Url, IDataCallBack<T> dataCallBack, boolean isEnre, String fileName) {
        RxSchedulersOperator.retryWhenOperator(mApiService.downloadFile(Url))
                .subscribe(getObserver(dataCallBack));
    }

    //封装单个文件上传
    public <T> void uploading(String url, File file, IDataCallBack<T> dataCallBack) {
        /*File file = new File(url);*/
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("headimg", file.getName(), requestFile);
        RxSchedulersOperator.retryWhenOperator(mApiService.uploadSingleFile(url, filePart))
                .subscribe(getObserver(dataCallBack));
    }

    //既有请求头又有参数的post请求
    public <T> void requestFormData(String url, Map<String, T> headers, Map<String, T> params, IDataCallBack<T> dataCallBack) {
        if (headers == null || headers.size() == 0)  //请求头为空 但是参数不为空
            requestFormData(url, params, dataCallBack);
        else if (params == null || params.size() == 0)  //TODO参数为空但是请求头不为空
            requestFormData(url, headers, dataCallBack);
        else if ((headers == null || headers.size() == 0) && // 请求头和参数都为空
                (params == null || params.size() == 0))
            requestFormData(url, dataCallBack);
        else
            //请求头和参数都不为空
            RxSchedulersOperator.retryWhenOperator(mApiService.requestFormData(url, headers, params)).
                    subscribe(getObserver(dataCallBack));
    }

    //带有Json串的没有请求头的Post请求

    public <T> void requestFormData(String url, String strJson, IDataCallBack<T> dataCallBack) {
        if (strJson.isEmpty()) {
            return;
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset = utf - 8"), strJson);
        RxSchedulersOperator
                .retryWhenOperator(mApiService.requestFormData(url, body))
                .subscribe(getObserver(dataCallBack));
    }

    //带请求头的上传json串的post请求
    public <T> void requestFormData(String url, Map<String, T> haders, String jsonStr, IDataCallBack<T> dataCallBack) {
        if (haders.size() == 0 || haders == null) {
            requestFormData(url, jsonStr, dataCallBack);
        } else {
            if (jsonStr.isEmpty())
                return;
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset = utf - 8"), jsonStr);
            RxSchedulersOperator
                    .retryWhenOperator(mApiService.requestFormData(url, haders, body))
                    .subscribe(getObserver(dataCallBack));
        }
    }

    //多个文件上传1
    public <T> void uploadMultipleFiles(IDataCallBack<T> dataCallBack, String url, File... files) {
        MultipartBody.Part[] parts = new MultipartBody.Part[files.length];
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                parts[i] = part;
            }
        }
        RxSchedulersOperator.retryWhenOperator(mApiService.uploadMultipleFiles(url, parts)).
                subscribe(getObserver(dataCallBack));
    }

    //多个文件上传2
    public <T> void uploadMultipleFiles(String url, IDataCallBack<T> dataCallBack, File... files) {
        if (files != null && files.length > 0) {
            HashMap<String, RequestBody> hashMap = new HashMap<>();
            for (File file : files) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                hashMap.put("file;filename=" + file.getName(), requestBody);
            }
            RxSchedulersOperator.retryWhenOperator(mApiService.uploadMultipleFiles(url, hashMap))
                    .subscribe(getObserver(dataCallBack));
        }
    }

    //多文件+多个键值对上传
    public <T> void uploadStrFiles(String url, Map<String, String> stringTMap, IDataCallBack<T> dataCallBack, File... files) {
        if (files != null && files.length > 0) {
            if (stringTMap.size() == 0 || stringTMap == null) {
                uploadMultipleFiles(url, dataCallBack, files);
            } else {
                FormBody.Builder builder = new FormBody.Builder();
                for (int i = 0; i < stringTMap.size(); i++) {
                    Set<String> strings = stringTMap.keySet();
                    for (String str : strings) {
                        String st = stringTMap.get(str);
                        builder.add(str, st);
                    }
                }
                Map<String, RequestBody> hashMap = new HashMap<String, RequestBody>();
                for (File file : files) {
                    RequestBody re = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    //上传多个文件
                    hashMap.put("file;fileName=" + file.getName(), re);
                }
                RxSchedulersOperator.retryWhenOperator(mApiService.uploadStrFiles(url, builder.build(), hashMap))
                        .subscribe(getObserver(dataCallBack));
            }
        }
    }

    //多文件+多个键值对上传2
    public <T> void uploadStrFiles(String url, String s, IDataCallBack<T> dataCallBack, File... files) {
        if (files != null && files.length > 0) {
            uploadMultipleFiles(url, dataCallBack, files);
        } else {
            for (File file : files) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                filePart = MultipartBody.Part.createFormData("headimg", file.getName(), requestFile);
                body = RequestBody.create(MediaType.parse("multipart/form-data"), s);
            }
            RxSchedulersOperator.retryWhenOperator(mApiService.uploadStrFiles(url, body, filePart))
                    .subscribe(getObserver(dataCallBack));
        }
    }


    //封装单个文件携带参数上传
    public <T> void upLoadingData(String url, File file, String str, IDataCallBack<T> dataCallBack) {
        if (TextUtils.isEmpty(str)) {
            uploading(url, file, dataCallBack);
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("headimg", file.getName(), requestFile);
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), str);
        RxSchedulersOperator.retryWhenOperator(mApiService.uploadStrFile(url, body, filePart))
                .subscribe(getObserver(dataCallBack));
    }

    //封装回调的方法
    private <T> RxObserver<? extends T> getObserver(IDataCallBack<T> iDataCallBack) {
        return new RxObserver<T>(iDataCallBack) {
            @Override
            public void onSubscribe(Disposable d) {
                if (iDataCallBack != null) {
                    iDataCallBack.onResponseDisposable(d);
                }
            }

            @Override
            public void onNext(T t) {
                if (iDataCallBack != null) {
                    iDataCallBack.onStateSucess(t);
                }
            }
        };
    }
}
