package com.example.a86130.zhangmiaoyin.component;

import android.content.SharedPreferences;

import com.example.a86130.zhangmiaoyin.model.OkManagerModule;
import com.example.a86130.zhangmiaoyin.model.api.ApiService;
import com.example.a86130.zhangmiaoyin.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

//专门对外提供单例(SP,OK,OkHttClient.Builder ,Retrofit和RetrofitBuidelr ApiService)的桥梁
@Singleton
@Component(modules = {AppModule.class, OkManagerModule.class})
public interface AppComponent {
    SharedPreferences provideSp();
    OkHttpClient provideOk();
    ApiService provideApiService();
}
