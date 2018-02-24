package com.ct7liang.accountsafer;

import android.graphics.Color;

import com.ct7liang.tangyuan.BasisActivity;
import com.jaeger.library.StatusBarUtil;

public abstract class BaseActivity extends BasisActivity {

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, Color.parseColor("#DD4E43"), 0);
    }
}