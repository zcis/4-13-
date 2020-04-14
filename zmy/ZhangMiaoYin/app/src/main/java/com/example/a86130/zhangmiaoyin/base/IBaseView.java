package com.example.a86130.zhangmiaoyin.base;

public interface IBaseView<T> {
    void stateSuess(T t);
    void stateError(String msg);
}
