package com.ct7liang.accountsafer.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.ct7liang.accountsafer.BaseActivity;
import com.ct7liang.accountsafer.R;
import com.jaeger.library.StatusBarUtil;

public class AboutAppActivity extends BaseActivity {

    @Override
    public int setLayout() {
        return R.layout.activity_about_app;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, Color.parseColor("#4D6BA7"), 0);
        findViewById(R.id.title_bar).setBackgroundColor(Color.parseColor("#4D6BA7"));
    }

    @Override
    public void findView() {
        findViewById(R.id.back).setOnClickListener(this);
        ((TextView)findViewById(R.id.title)).setText("关于软件");
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
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}