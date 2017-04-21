package com.jarry.jayvoice.activity.main.core;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarry.jayvoice.R;
import com.jarry.jayvoice.activity.LoginActivity;
import com.jarry.jayvoice.activity.MeInfoActivity;
import com.jarry.jayvoice.activity.main.interf.MainInterf;
import com.jarry.jayvoice.bean.User;
import com.jarry.jayvoice.core.UserManager;
import com.jarry.jayvoice.util.ImageUtil;

/**
 * Created by mlfdev1 on 2017/4/20.
 */

public class MainNavView extends NavigationView implements MainInterf.NavViewManageInterface,View.OnClickListener{

    ImageView nav_HeadImgView;
    View nav_headView;
    TextView nav_titleTv;
    TextView nav_subtitleTv;
    Context context;

    public MainNavView(Context context) {
        super(context);
        initView(context);
    }

    public MainNavView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MainNavView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        nav_headView = this.getHeaderView(0);
        nav_HeadImgView = (ImageView) nav_headView.findViewById(R.id.nav_header_imageView);
        nav_titleTv = (TextView) nav_headView.findViewById(R.id.nav_header_titleTv);
        nav_subtitleTv = (TextView) nav_headView.findViewById(R.id.nav_header_subtitleTv);
        nav_headView.setOnClickListener(this);
    }

    @Override
    public void setData(User user) {
        if (user != null) {
            nav_titleTv.setText(user.nickName);
            nav_subtitleTv.setText(user.gender == 0 ? "男" : "女");
            ImageUtil.setImg(context,nav_HeadImgView,user.imgUrl,R.drawable.userpic);
        }else {
            nav_titleTv.setText("点击登录");
            nav_subtitleTv.setText("");
            ImageUtil.setImg(context,nav_HeadImgView,null,R.drawable.userpic);
        }

    }

    @Override
    public void onClick(View v) {
        UserManager userManager = UserManager.getInstance(getContext());
        if (userManager.isLogin()) {
            gotoActvity(MeInfoActivity.class);
        }else  {
            gotoActvity(LoginActivity.class);
        }
    }

    private void gotoActvity(Class activity){
        Intent intent = new Intent();
        intent.setClass(getContext(), activity);
        getContext().startActivity(intent);
    }
}
