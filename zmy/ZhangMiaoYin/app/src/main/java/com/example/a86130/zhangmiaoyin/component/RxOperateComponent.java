package com.example.a86130.zhangmiaoyin.component;


import com.example.a86130.zhangmiaoyin.annotation.PerSinglelton;
import com.example.a86130.zhangmiaoyin.model.RxOperateImpl;

import dagger.Component;

@PerSinglelton
@Component(dependencies = AppComponent.class)
public interface RxOperateComponent {
    void inject(RxOperateImpl rxOperate);
}
