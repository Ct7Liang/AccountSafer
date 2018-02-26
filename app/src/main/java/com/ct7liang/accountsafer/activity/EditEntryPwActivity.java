package com.ct7liang.accountsafer.activity;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
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

import cn.ct7liang.greendao.UserDao;

public class EditEntryPwActivity extends BaseActivity {

    private EditText ep;
    private EditText epn;
    private EditText epc;
    private UserDao userDao;
    private User user;

    @Override
    public int setLayout() {
        return R.layout.activity_update_login_pw;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, Color.parseColor("#787772"), 0);
        findViewById(R.id.title_bar).setBackgroundColor(Color.parseColor("#787772"));
    }

    @Override
    public void findView() {
        ((TextView)findViewById(R.id.title)).setText("修改登录密码");
        findViewById(R.id.back).setOnClickListener(this);
//        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.confirm).setOnClickListener(this);
        ep = (EditText) findViewById(R.id.password);
        epn = (EditText) findViewById(R.id.password_new);
        epc = (EditText) findViewById(R.id.password_confirm);
    }

    @Override
    public void initData() {
        userDao = BaseApp.getDaoSession().getUserDao();
        user = userDao.loadAll().get(0);
    }

    @Override
    public void initView() {}

    @Override
    public void initFinish() {}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
//            case R.id.cancel:
//                finish();
//                break;
            case R.id.confirm:
                String pswd = ep.getText().toString().trim();
                String pswd1 = epn.getText().toString().trim();
                String pswd2 = epc.getText().toString().trim();
                if (TextUtils.isEmpty(pswd)||TextUtils.isEmpty(pswd1)||TextUtils.isEmpty(pswd2)){
                    SnackBarUtils.show(findViewById(R.id.snack), "密码不能为空", "#DD4E41");
                    return;
                }
                if (!pswd.equals(Base64Utils.Base64ToString(user.getPassword()))){
                    SnackBarUtils.show(findViewById(R.id.snack), "密码错误", "#DD4E41");
                    return;
                }
                if (!pswd1.equals(pswd2)){
                    SnackBarUtils.show(findViewById(R.id.snack), "密码不一致", "#DD4E41");
                    return;
                }
                user.setPassword(Base64Utils.StringToBase64(pswd1));
                userDao.update(user);
                SnackBarUtils.show(findViewById(R.id.snack), "密码修改成功", "#82BF23", new Snackbar.Callback(){
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        finish();
                    }
                });
                break;
        }
    }
}
