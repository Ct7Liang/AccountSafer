package com.ct7liang.accountsafer.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.ct7liang.accountsafer.BaseActivity;
import com.ct7liang.accountsafer.BaseApp;
import com.ct7liang.accountsafer.utils.Constant;
import com.ct7liang.accountsafer.R;
import com.ct7liang.accountsafer.bean.Query;
import com.ct7liang.accountsafer.utils.Base64Utils;
import com.ct7liang.tangyuan.utils.SpUtils;
import java.util.List;

public class SettingQueryPasswordActivity extends BaseActivity {

    private PatternLockView mPatternLockView;
    private String temppassword;
    private TextView tips;

    @Override
    public int setLayout() {
        return R.layout.activity_query_password_setting;
    }

    @Override
    public void findView() {
        ((TextView)findViewById(R.id.title)).setText("设置查询密码");
        findViewById(R.id.back).setOnClickListener(this);
        tips = (TextView) findViewById(R.id.tips);
        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {}
            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {}
            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                if (temppassword==null){
                    temppassword = PatternLockUtils.patternToString(mPatternLockView, pattern);
                    if (temppassword.length()<4){
                        tips.setText("密码长度过短");
                        mPatternLockView.clearPattern();
                        temppassword = null;
                        return;
                    }
                    mPatternLockView.clearPattern();
                    tips.setText("请再次确认密码");
                }else{
                    String s = PatternLockUtils.patternToString(mPatternLockView, pattern);
                    if (s.equals(temppassword)){
                        BaseApp.getDaoSession().getQueryDao().deleteAll();
                        Query query = new Query(null, Base64Utils.StringToBase64(s));
                        BaseApp.getDaoSession().getQueryDao().insert(query);
                        SpUtils.start().saveInt(Constant.IsNewEntry, 1);
                        startActivity(new Intent(mAct, LoginActivity.class));
                    }else{
                        temppassword = null;
                        mPatternLockView.clearPattern();
                        tips.setText("密码设置不一致, 请重新设置");
                    }
                }
            }
            @Override
            public void onCleared() {}
        });
    }

    @Override
    public void initData() {}

    @Override
    public void initView() {}

    @Override
    public void initFinish() {}

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
