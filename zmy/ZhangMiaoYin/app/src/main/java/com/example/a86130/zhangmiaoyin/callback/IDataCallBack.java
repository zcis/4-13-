package com.example.a86130.zhangmiaoyin.callback;

import io.reactivex.disposables.Disposable;

public interface IDataCallBack<T> {
    void onStateSucess(T t);
    void onStateError(String msg);
    void onResponseDisposable(Disposable disposable);
}
