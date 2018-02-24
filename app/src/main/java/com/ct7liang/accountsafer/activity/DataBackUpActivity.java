package com.ct7liang.accountsafer.activity;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;
import com.ct7liang.accountsafer.BaseActivity;
import com.ct7liang.accountsafer.BaseApp;
import com.ct7liang.accountsafer.R;
import com.ct7liang.accountsafer.bean.Account;
import com.ct7liang.accountsafer.bean.BackUp;
import com.ct7liang.accountsafer.utils.Base64Utils;
import com.ct7liang.accountsafer.utils.FileUtils;
import com.ct7liang.accountsafer.utils.SnackBarUtils;
import com.ct7liang.tangyuan.AppFolder;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;
import cn.ct7liang.greendao.AccountDao;
import cn.ct7liang.greendao.QueryDao;
import cn.ct7liang.greendao.UserDao;


public class DataBackUpActivity extends BaseActivity {

    private QueryDao queryDao;
    private UserDao userDao;
    private AccountDao accountDao;
    private BackUp backUp;
    private List<Account> accounts;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    cancelProgress();
                    SnackBarUtils.show(findViewById(R.id.snack), "数据备份成功", "#82BF23");
                    break;
                case 1:
                    cancelProgress();
                    SnackBarUtils.show(findViewById(R.id.snack), "数据导入成功", "#82BF23");
                    break;
            }
        }
    };

    @Override
    public int setLayout() {
        return R.layout.activity_data_back_up;
    }

    @Override
    public void findView() {
        findViewById(R.id.back).setOnClickListener(this);
        ((TextView)findViewById(R.id.title)).setText("数据备份");
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.into).setOnClickListener(this);
    }

    @Override
    public void initData() {
        File file = AppFolder.get();
        if (file==null){
            SnackBarUtils.show(findViewById(R.id.snack), "本地数据操作权限关闭,操作无法进行");
        }else{
            File dir = new File(file, "/backups");
            if (!dir.exists()){
                dir.mkdirs();
            }
            File dirs = new File(file, "/fromBackups");
            if (!dirs.exists()){
                dirs.mkdirs();
            }
        }
        queryDao = BaseApp.getDaoSession().getQueryDao();
        userDao = BaseApp.getDaoSession().getUserDao();
        accountDao = BaseApp.getDaoSession().getAccountDao();
        backUp = new BackUp();
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
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.start:
                backup();
                break;
            case R.id.into:
                fromBackup();
                break;
        }
    }

    private void backup() {
        if (AppFolder.get()==null){
            SnackBarUtils.show(findViewById(R.id.snack), "本地数据操作权限关闭,操作无法进行");
            return;
        }
        if (accounts.size()==0){
            SnackBarUtils.show(findViewById(R.id.snack), "暂无数据可以备份");
            return;
        }
        showProgressDialog();
        new Thread(){
            @Override
            public void run() {
                backUp.entryPswd = userDao.loadAll().get(0).getPassword();
                backUp.queryPswd = queryDao.loadAll().get(0).getQueryPassword();
                backUp.list = accounts;
                String s = Base64Utils.StringToBase64(new Gson().toJson(backUp));
                File dir = new File(AppFolder.get(), "/backups");
                if (dir.listFiles().length>0){
                    deleteFiles(dir);
                }
                File file = new File(dir, System.currentTimeMillis()+".txt");
                try {
                    file.createNewFile();
                    FileUtils.write(file.getPath(), s);
                    file.renameTo(new File(dir, System.currentTimeMillis()+".7"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SystemClock.sleep(600);
                handler.sendEmptyMessage(0);
            }
        }.start();
    }


    private void fromBackup() {
        if (AppFolder.get()==null){
            SnackBarUtils.show(findViewById(R.id.snack), "本地数据操作权限关闭,操作无法进行");
            return;
        }
        final File dir = new File(AppFolder.get(), "/fromBackups");
        if (dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getPath().endsWith(".7")){
                    return true;
                }
                return false;
            }
        }).length==0){
            SnackBarUtils.show(findViewById(R.id.snack), "目标文件夹无相关数据", "#DD4E41");
            return;
        }else{
            // TODO: 2018-02-24 验证密码
            showProgressDialog();
            new Thread(){
                @Override
                public void run() {
                    File[] files = dir.listFiles();
                    File dis = null;
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].getPath().endsWith(".7")){
                            dis = files[i];
                            break;
                        }
                    }
                    if (dis==null){
                        return;
                    }
                    String content = FileUtils.read(dis);
                    String s = Base64Utils.Base64ToString(content);
                    final BackUp backUp = new Gson().fromJson(s, BackUp.class);
                    Account account;
                    for (int i = 0; i < backUp.list.size(); i++) {
                        account = new Account();
                        account.setTag(backUp.list.get(i).getTag());
                        account.setAccount(backUp.list.get(i).getAccount());
                        account.setPassword(backUp.list.get(i).getPassword());
                        String remark = backUp.list.get(i).getRemark();
                        if (remark!=null){
                            account.setRemark(remark);
                        }
                        accountDao.insert(account);
                    }
                    dis.delete();
                    SystemClock.sleep(600);
                    handler.sendEmptyMessage(1);
                }
            }.start();
        }
    }


    private void deleteFiles(File dir) {
        File[] files;
        while(true){
            files = dir.listFiles();
            if (files.length!=0){
                files[0].delete();
            }else{
                break;
            }
        }
    }

    private void showCheckEntryPswd(){

    }

    private void showCheckQueryPswd(){

    }
}