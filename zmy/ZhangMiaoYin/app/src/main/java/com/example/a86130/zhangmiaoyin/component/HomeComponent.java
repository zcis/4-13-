package com.example.a86130.zhangmiaoyin.component;


import com.example.a86130.zhangmiaoyin.annotation.PerSinglelton;
import com.example.a86130.zhangmiaoyin.presenter.HomePresenter;

import dagger.Component;

@PerSinglelton
@Component(dependencies = AppComponent.class)
public interface HomeComponent {
    void inject(HomePresenter homePresenter);
}
