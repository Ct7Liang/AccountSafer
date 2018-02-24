package com.ct7liang.accountsafer.activity;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.ct7liang.accountsafer.BaseActivity;
import com.ct7liang.accountsafer.BaseApp;
import com.ct7liang.accountsafer.R;
import com.ct7liang.accountsafer.bean.Query;
import com.ct7liang.accountsafer.utils.Base64Utils;
import com.ct7liang.accountsafer.utils.SnackBarUtils;

import java.util.List;

import cn.ct7liang.greendao.QueryDao;

public class UpdateQueryPwActivity extends BaseActivity {

    private boolean isRead;
    private PatternLockView patternLockView;
    private QueryDao queryDao;
    private Query query;
    private String pswd;

    @Override
    public int setLayout() {
        return R.layout.activity_update_query_pw;
    }

    @Override
    public void findView() {
        ((TextView)findViewById(R.id.title)).setText("修改查询密码");
        findViewById(R.id.back).setOnClickListener(this);
        patternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {}
            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {}
            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String s = PatternLockUtils.patternToString(patternLockView, pattern);
                if (!isRead){
                    if (!s.equals(Base64Utils.Base64ToString(query.getQueryPassword()))){
                        patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                        SnackBarUtils.show(findViewById(R.id.snack), "密码错误", "#DD4E41", new Snackbar.Callback(){
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                patternLockView.clearPattern();
                            }
                        });
                    }else{
                        isRead = !isRead;
                        patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                        SnackBarUtils.show(findViewById(R.id.snack), "请输入新密码", "#82BF23", new Snackbar.Callback(){
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                patternLockView.clearPattern();
                            }
                        });
                    }
                }else{
                    if (pswd == null){
                        patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                        pswd = s;
                        SnackBarUtils.show(findViewById(R.id.snack), "请确认密码", "#82BF23", new Snackbar.Callback(){
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                patternLockView.clearPattern();
                            }
                        });
                    }else{
                        if (!s.equals(pswd)){
                            pswd = null;
                            patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                            SnackBarUtils.show(findViewById(R.id.snack), "两次密码不一致, 请重新输入", "#DD4E41", new Snackbar.Callback(){
                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    patternLockView.clearPattern();
                                }
                            });
                        }else{
                            patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                            query.setQueryPassword(Base64Utils.StringToBase64(s));
                            queryDao.update(query);
                            SnackBarUtils.show(findViewById(R.id.snack), "查询密码修改成功", "#82BF23", new Snackbar.Callback(){
                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    finish();
                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onCleared() {}
        });
    }

    @Override
    public void initData() {
        queryDao = BaseApp.getDaoSession().getQueryDao();
        query = queryDao.loadAll().get(0);
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