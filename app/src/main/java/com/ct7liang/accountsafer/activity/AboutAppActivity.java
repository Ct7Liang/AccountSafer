package com.ct7liang.accountsafer.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import com.ct7liang.accountsafer.BaseActivity;
import com.ct7liang.accountsafer.R;
import com.ct7liang.tangyuan.utils.ScreenInfoUtil;

public class AboutAppActivity extends BaseActivity {

    @Override
    public int setLayout() {
        return R.layout.activity_about_app;
    }

    @Override
    protected void setStatusBar() {
        View title = findViewById(R.id.title_back_ground);
        title.setBackgroundColor(Color.parseColor("#00000000"));
//        title.setBackgroundColor(Color.parseColor("#4D6BA7"));
        title.setPadding(0, ScreenInfoUtil.getStatusHeight(this), 0, 0);
    }

    @Override
    public void findView() {
        initStatusBar();
        findViewById(R.id.left_image).setOnClickListener(this);
        ((TextView)findViewById(R.id.left_text)).setText("关于软件");
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
            case R.id.left_image:
                finish();
                break;
        }
    }
}