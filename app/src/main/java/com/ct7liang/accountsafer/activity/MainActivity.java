package com.ct7liang.accountsafer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.ct7liang.accountsafer.BaseActivity;
import com.ct7liang.accountsafer.BaseApp;
import com.ct7liang.accountsafer.R;
import com.ct7liang.accountsafer.bean.Account;
import com.ct7liang.accountsafer.utils.Base64Utils;
import com.ct7liang.tangyuan.utils.ToastUtils;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnMenuItemClickListener {

    private TagFlowLayout tagFlowLayout;
    private List<Account> list;
    private MyTagAdapter adapter;
    private ContextMenuDialogFragment fragment;

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void findView() {
        tagFlowLayout = (TagFlowLayout) findViewById(R.id.flowLayout);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.image_right).setVisibility(View.VISIBLE);
        findViewById(R.id.right).setOnClickListener(this);
    }

    @Override
    public void initData() {
        list = BaseApp.getDaoSession().getAccountDao().loadAll();
        adapter = new MyTagAdapter(list);
        tagFlowLayout.setAdapter(adapter);
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Intent i = new Intent(mAct, AccountDetailActivity.class);
                i.putExtra("bean", list.get(position));
                startActivity(i);
                return true;
            }
        });
    }

    @Override
    public void initView() {
        ArrayList<MenuObject> menuObjects = new ArrayList<>();

        MenuObject menuObject = new MenuObject("收起");
        menuObject.setBgColor(Color.parseColor("#FF0000"));
        menuObject.setDividerColor(R.color.a);
        menuObjects.add(menuObject);

        MenuObject menuObject1 = new MenuObject("添加账号");
        menuObject1.setBgColor(Color.parseColor("#FFAA00"));
        menuObject1.setDividerColor(R.color.b);
        menuObjects.add(menuObject1);

        MenuObject menuObject2 = new MenuObject("修改登录密码");
        menuObject2.setBgColor(Color.parseColor("#FFFF00"));
        menuObject2.setDividerColor(R.color.c);
        menuObjects.add(menuObject2);

        MenuObject menuObject3 = new MenuObject("修改查询密码");
        menuObject3.setBgColor(Color.parseColor("#84D945"));
        menuObject3.setDividerColor(R.color.d);
        menuObjects.add(menuObject3);

        MenuObject menuObject4 = new MenuObject("数据备份");
        menuObject4.setBgColor(Color.parseColor("#FFAA00"));
        menuObject4.setDividerColor(R.color.b);
        menuObjects.add(menuObject4);

        MenuObject menuObject5 = new MenuObject("软件说明");
        menuObject5.setBgColor(Color.parseColor("#FFFF00"));
        menuObject5.setDividerColor(R.color.c);
        menuObjects.add(menuObject5);

        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        fragment = ContextMenuDialogFragment.newInstance(actionBarHeight, menuObjects);
        fragment.setItemClickListener(this);
    }

    @Override
    public void initFinish() {}

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter!=null){
            list = BaseApp.getDaoSession().getAccountDao().loadAll();
            if (adapter.getCount()!=list.size()){
                adapter = new MyTagAdapter(list);
                tagFlowLayout.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                exitApp();
                break;
            case R.id.right:
                fragment.show(getSupportFragmentManager(), "aaaaaaaaaaaaa");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        switch (position){
            case 1:
                //添加账号
                startActivity(new Intent(this, AddAccountActivity.class));
                break;
            case 2:
                //修改登录密码
                startActivity(new Intent(this, UpdateLoginPwActivity.class));
                break;
            case 3:
                //修改查询密码
                startActivity(new Intent(this, UpdateQueryPwActivity.class));
                break;
            case 4:
                //数据备份
                startActivity(new Intent(this, DataBackUpActivity.class));
                break;
            case 5:
                //软件说明
                ToastUtils.showStatic(mAct, "该功能尚未开发");
                break;
        }
    }

    private class MyTagAdapter extends TagAdapter<Account>{
        public MyTagAdapter(List<Account> datas) {
            super(datas);
        }
        @Override
        public View getView(FlowLayout parent, int position, Account account) {
            TextView tv = (TextView) View.inflate(mAct, R.layout.tag_text_view, null);
            tv.setText(Base64Utils.Base64ToString(list.get(position).getTag()));
            return tv;
        }
    }
}