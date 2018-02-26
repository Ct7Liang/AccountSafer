package com.ct7liang.accountsafer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.ct7liang.accountsafer.BaseApp;
import com.ct7liang.accountsafer.R;
import com.ct7liang.accountsafer.bean.User;
import com.ct7liang.accountsafer.utils.Base64Utils;
import com.ct7liang.accountsafer.utils.FileUtils;
import com.ct7liang.tangyuan.AppFolder;
import com.ct7liang.tangyuan.utils.GlideHelper;
import com.ct7liang.tangyuan.utils.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EntryActivity extends TakePhotoActivity implements View.OnClickListener {

    private String passwordstr;
    private ImageView userImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        StatusBarUtil.setColor(this, Color.parseColor("#5BB0CD"), 0);
        findViewById(R.id.title_bar).setBackgroundColor(Color.parseColor("#5BB0CD"));

        findViewById(R.id.back).setOnClickListener(this);
        ((TextView)findViewById(R.id.title)).setText("登录");
        userImage = (ImageView) findViewById(R.id.user_icon);
        userImage.setOnClickListener(this);

        File file = new File(AppFolder.get(), "/user.txt");
        File file1 = new File(AppFolder.get(), "user.jpg");
        if (file.exists()){
            String read = FileUtils.read(file);
            if (file1.exists()){
                file1.delete();
            }
            File file2 = Base64Utils.base64ToFile(read, file1);
            Glide.with(this).load(file2).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .transform(new BitmapTransformation[]{GlideHelper.getInstance().getCircleTransform(this)}).into(userImage);
        }

        EditText password = (EditText) findViewById(R.id.password);

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String pswd = s.toString().trim();
                if (passwordstr.equals(pswd)){
                    startActivity(new Intent(EntryActivity.this, MainActivity.class));
                    finish();
                }
            }
        });

        List<User> users = BaseApp.getDaoSession().getUserDao().loadAll();
        if (users.size()>0){
            passwordstr = Base64Utils.Base64ToString(users.get(0).getPassword());
        }else{
            startActivity(new Intent(EntryActivity.this, CreateEntryPwActivity.class));
            finish();
        }
    }

    private long lastTime;
    public void exitApp() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastTime < 2000L) {
            lastTime = currentTime;
            Intent intent = new Intent();
            intent.setAction("com.ct7liang.tangyuan.receiver");
            sendBroadcast(intent);
            finish();
        } else {
            ToastUtils.showStatic(this, "再按一次退出");
            lastTime = currentTime;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                exitApp();
                break;
            case R.id.user_icon:
                CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(false).create();
                File file = new File(AppFolder.get(), "/user.jpg");
                if (file.exists()){
                    file.delete();
                }else{
                    try {
                        file.createNewFile();
                        Uri uri = Uri.fromFile(file);
                        getTakePhoto().onPickFromGalleryWithCrop(uri, cropOptions);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    @Override
    public void takeSuccess(TResult result) {
        String originalPath = result.getImage().getOriginalPath();
        File file = new File(originalPath);
        String s = Base64Utils.fileToBase64(file);
        File imageStr = new File(AppFolder.get(), "/user.txt");
        if (imageStr.exists()){
            imageStr.delete();
        }
        try {
            imageStr.createNewFile();
            FileUtils.write(imageStr.getPath(), s);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Glide.with(this).load(file).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(new BitmapTransformation[]{GlideHelper.getInstance().getCircleTransform(this)}).into(userImage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        File file = new File(AppFolder.get(), "/user.jpg");
        if (file.exists()){
            file.delete();
        }
    }
}