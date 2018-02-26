package com.ct7liang.accountsafer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ct7liang.accountsafer.BaseActivity;
import com.ct7liang.accountsafer.BaseApp;
import com.ct7liang.accountsafer.R;
import com.ct7liang.accountsafer.bean.User;
import com.ct7liang.accountsafer.utils.Base64Utils;
import com.ct7liang.accountsafer.utils.SnackBarUtils;
import com.jaeger.library.StatusBarUtil;

public class CreateEntryPwActivity extends BaseActivity {

    private EditText editText01;
    private EditText editText02;

    @Override
    public int setLayout() {
        return R.layout.activity_entry_password_setting;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, Color.parseColor("#787772"), 0);
        findViewById(R.id.title_bar).setBackgroundColor(Color.parseColor("#787772"));
    }

    @Override
    public void findView() {
        ((TextView)findViewById(R.id.title)).setText("设置登录密码");
        editText01 = (EditText) findViewById(R.id.password);
        editText02 = (EditText) findViewById(R.id.password_confirm);
//        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.confirm).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
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
                exitApp();
                break;
//            case R.id.cancel:
//                exitApp();
//                break;
            case R.id.confirm:
                String pw01 = editText01.getText().toString().trim();
                String pw02 = editText02.getText().toString().trim();
                if (TextUtils.isEmpty(pw01)||TextUtils.isEmpty(pw02)){
                    SnackBarUtils.show(findViewById(R.id.snack), "密码不能为空", "#DD4E41");
                    return;
                }
                if (!pw01.equals(pw02)){
                    SnackBarUtils.show(findViewById(R.id.snack), "密码不一致", "#DD4E41");
                    return;
                }
                BaseApp.getDaoSession().getUserDao().deleteAll();
                User user = new User(null, Base64Utils.StringToBase64(pw01));
                BaseApp.getDaoSession().insert(user);
                startActivity(new Intent(this, CreateQueryPwActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }
}