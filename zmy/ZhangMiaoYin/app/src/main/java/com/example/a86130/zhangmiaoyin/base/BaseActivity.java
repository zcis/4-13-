package com.example.a86130.zhangmiaoyin.base;

import android.os.Bundle;

import com.example.a86130.zhangmiaoyin.App;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<T> extends AppCompatActivity implements IBaseView<T> {
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayout();
        if (layoutId != 0)
            setContentView(layoutId);
        App.getInstance().addActivity(this);
        mUnbinder = ButterKnife.bind(this);
        onViewCreated();
        initListenner();

    }

    protected abstract int getLayout();

    protected abstract void initListenner();

    protected abstract void onViewCreated();


    @Override
    public void stateSuess(T t) {

    }

    @Override
    public void stateError(String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
        App.getInstance().removeActivity();
    }
}
