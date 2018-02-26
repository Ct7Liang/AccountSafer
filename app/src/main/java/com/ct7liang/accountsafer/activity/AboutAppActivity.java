package com.ct7liang.accountsafer.activity;

import android.view.View;
import android.widget.TextView;

import com.ct7liang.accountsafer.BaseActivity;
import com.ct7liang.accountsafer.R;

public class AboutAppActivity extends BaseActivity {

    private TextView a;
    private TextView b;
    private TextView c;
    private TextView d;
    private TextView e;
    private TextView f;

    @Override
    public int setLayout() {
        return R.layout.activity_about_app;
    }

    @Override
    public void findView() {
        a = (TextView) findViewById(R.id.a);
        b = (TextView) findViewById(R.id.b);
        c = (TextView) findViewById(R.id.c);
        d = (TextView) findViewById(R.id.d);
        e = (TextView) findViewById(R.id.e);
        f = (TextView) findViewById(R.id.f);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initFinish() {

    }

    @Override
    public void onClick(View v) {

    }
}