package com.ct7liang.accountsafer;

import android.app.Application;

import com.ct7liang.tangyuan.AppFolder;
import com.ct7liang.tangyuan.utils.LogUtils;
import com.ct7liang.tangyuan.utils.SpUtils;
import com.ct7liang.tangyuan.utils.ToastUtils;

import org.greenrobot.greendao.database.Database;

import java.io.File;

import cn.ct7liang.greendao.DaoMaster;
import cn.ct7liang.greendao.DaoSession;

/**
 * Created by Administrator on 2018-02-22.
 *
 */
public class BaseApp extends Application {

    private static DaoSession dao;

    @Override
    public void onCreate() {
        super.onCreate();

        SpUtils.init(this);
        ToastUtils.setIsShowTestEnable(true);
        LogUtils.setLogEnable(true);
        LogUtils.setShowLocationEnable(false);
        LogUtils.setTag("Account");
        AppFolder.createAppFolder("Account_Safer");
        createSplashFolder();

        initDataBase();
    }

    private void initDataBase() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "account_safer");
        Database db = devOpenHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        dao = daoMaster.newSession();
    }

    public static DaoSession getDaoSession(){
        return dao;
    }

    private void createSplashFolder(){
        File dir = AppFolder.get();
        if (dir.exists()){
            File dis_s = new File(dir, "/splash");
            if (!dis_s.exists()){
                dis_s.mkdirs();
            }
        }
    }

    public static File getSplashFolder(){
        File dir = AppFolder.get();
        if (!dir.exists()){
            return null;
        }else{
            return new File(dir, "/splash");
        }
    }
}
