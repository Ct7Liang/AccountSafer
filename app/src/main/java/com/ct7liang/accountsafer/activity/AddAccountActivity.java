package com.ct7liang.accountsafer.activity;

import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ct7liang.accountsafer.BaseActivity;
import com.ct7liang.accountsafer.BaseApp;
import com.ct7liang.accountsafer.R;
import com.ct7liang.accountsafer.bean.Account;
import com.ct7liang.accountsafer.utils.Base64Utils;
import com.ct7liang.accountsafer.utils.SnackBarUtils;

import java.util.List;

import cn.ct7liang.greendao.AccountDao;

public class AddAccountActivity extends BaseActivity {

    private EditText eTag;
    private EditText eAccount;
    private EditText ePassword;
    private EditText eDescription;
    private AccountDao accountDao;
    private Account account;
    private List<Account> accounts;

    @Override
    public int setLayout() {
        return R.layout.activity_add_account;
    }

    @Override
    public void findView() {
        findViewById(R.id.back).setOnClickListener(this);
        ((TextView)findViewById(R.id.title)).setText("添加新账号");
        findViewById(R.id.commit).setOnClickListener(this);
        eTag = (EditText) findViewById(R.id.tag);
        eAccount = (EditText) findViewById(R.id.account);
        ePassword = (EditText) findViewById(R.id.password);
        eDescription = (EditText) findViewById(R.id.description);
    }

    @Override
    public void initData() {
        accountDao = BaseApp.getDaoSession().getAccountDao();
        account = new Account();
        accounts = accountDao.loadAll();
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
            case R.id.commit:
                String tag = eTag.getText().toString().trim();
                String accountstr = eAccount.getText().toString().trim();
                String password = ePassword.getText().toString().trim();
                String description = eDescription.getText().toString().trim();
                if (TextUtils.isEmpty(tag)||TextUtils.isEmpty(accountstr)||TextUtils.isEmpty(password)){
                    SnackBarUtils.show(findViewById(R.id.snack), "请将必填项输入完整", "#DD4E41");
                    return;
                }
                for (int i = 0; i < accounts.size(); i++) {
                    if (Base64Utils.StringToBase64(tag).equals(accounts.get(i).getTag())){
                        SnackBarUtils.show(findViewById(R.id.snack), "该标签已存在", "#DD4E41");
                        return;
                    }
                }
                account.setTag(Base64Utils.StringToBase64(tag));
                account.setAccount(Base64Utils.StringToBase64(accountstr));
                account.setPassword(Base64Utils.StringToBase64(password));
                if (!TextUtils.isEmpty(description)){
                    account.setRemark(Base64Utils.StringToBase64(description));
                }else{
                    account.setRemark(Base64Utils.StringToBase64(""));
                }
                accountDao.insert(account);
                SnackBarUtils.show(findViewById(R.id.snack), "数据录入成功", "#82BF23", new Snackbar.Callback(){
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        finish();
                    }
                });
                break;
        }
    }
}
