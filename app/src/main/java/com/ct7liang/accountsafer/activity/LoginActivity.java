package com.ct7liang.accountsafer.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ct7liang.accountsafer.BaseActivity;
import com.ct7liang.accountsafer.BaseApp;
import com.ct7liang.accountsafer.R;
import com.ct7liang.accountsafer.bean.User;
import com.ct7liang.accountsafer.utils.Base64Utils;

import java.util.List;

public class LoginActivity extends BaseActivity {

    private String passwordstr;

    @Override
    public int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void findView() {
        ((TextView)findViewById(R.id.title)).setText("登录");
        final EditText password = (EditText) findViewById(R.id.password);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String pswd = s.toString().trim();
                if (passwordstr.equals(pswd)){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
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
        List<User> users = BaseApp.getDaoSession().getUserDao().loadAll();
        if (users.size()>0){
            passwordstr = Base64Utils.Base64ToString(users.get(0).getPassword());
        }else{
            startActivity(new Intent(LoginActivity.this, SettingEntryPasswordActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                exitApp();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }
}