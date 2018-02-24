package com.ct7liang.accountsafer.activity;

import android.content.Intent;
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

import cn.ct7liang.greendao.AccountDao;

public class EditAccountActivity extends BaseActivity {

    private EditText eT;
    private EditText eA;
    private EditText eP;
    private EditText eR;
    private Account account;
    private AccountDao accountDao;

    @Override
    public int setLayout() {
        return R.layout.activity_edit_account;
    }

    @Override
    public void findView() {
        ((TextView)findViewById(R.id.title)).setText("修改账号信息");
        findViewById(R.id.back).setOnClickListener(this);
        eT = (EditText) findViewById(R.id.tag);
        eA = (EditText) findViewById(R.id.account);
        eP = (EditText) findViewById(R.id.password);
        eR = (EditText) findViewById(R.id.remark);
        findViewById(R.id.confirm).setOnClickListener(this);
    }

    @Override
    public void initData() {
        account = (Account) getIntent().getSerializableExtra("bean");
        accountDao = BaseApp.getDaoSession().getAccountDao();
    }

    @Override
    public void initView() {
        eT.setText(Base64Utils.Base64ToString(account.getTag()));
        eA.setText(Base64Utils.Base64ToString(account.getAccount()));
        eP.setText(Base64Utils.Base64ToString(account.getPassword()));
        String remark = account.getRemark();
        if (remark!=null){
            eR.setText(Base64Utils.Base64ToString(account.getRemark()));
        }
    }

    @Override
    public void initFinish() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                Intent ii = new Intent();
                setResult(333, ii);
                finish();
                break;
            case R.id.confirm:
                String t = eT.getText().toString().trim();
                String a = eA.getText().toString().trim();
                String p = eP.getText().toString().trim();
                String r = eR.getText().toString().trim();
                if (TextUtils.isEmpty(t)||TextUtils.isEmpty(a)||TextUtils.isEmpty(p)){
                    SnackBarUtils.show(findViewById(R.id.snack), "请将内容输入完整", "#DD4E41");
                    return;
                }
                if (!TextUtils.isEmpty(r)){
                    account.setRemark(Base64Utils.StringToBase64(r));
                }else{
                    account.setRemark(Base64Utils.StringToBase64(""));
                }
                account.setTag(Base64Utils.StringToBase64(t));
                account.setAccount(Base64Utils.StringToBase64(a));
                account.setPassword(Base64Utils.StringToBase64(p));
                accountDao.update(account);
                Intent i = new Intent();
                i.putExtra("bean", account);
                setResult(222, i);
                finish();
                break;
        }
    }
}