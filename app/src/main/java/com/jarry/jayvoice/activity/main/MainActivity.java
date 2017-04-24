package com.jarry.jayvoice.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarry.jayvoice.MyApplication;
import com.jarry.jayvoice.R;
import com.jarry.jayvoice.activity.CollectionActivity;
import com.jarry.jayvoice.activity.InvitationActivity;
import com.jarry.jayvoice.activity.LoginActivity;
import com.jarry.jayvoice.activity.SettingActivity;
import com.jarry.jayvoice.activity.main.core.FragmentManagerImpl;
import com.jarry.jayvoice.activity.main.core.MainNavView;
import com.jarry.jayvoice.activity.main.interf.MainInterf;
import com.jarry.jayvoice.bean.User;
import com.jarry.jayvoice.core.DataBusiness;
import com.jarry.jayvoice.core.GetDataBusiness;
import com.jarry.jayvoice.activity.main.fragment.BaseFragment;
import com.jarry.jayvoice.activity.main.fragment.FindFragment;
import com.jarry.jayvoice.activity.main.fragment.MeFragment;
import com.jarry.jayvoice.activity.main.fragment.PlayMainFragment;
import com.jarry.jayvoice.activity.main.fragment.SeeFragment;
import com.jarry.jayvoice.core.UserManager;
import com.jarry.jayvoice.util.ImageUtil;
import com.jarry.jayvoice.util.Logger;
import com.jarry.jayvoice.util.StringUtils;
import com.umeng.analytics.MobclickAgent;

import a.b.c.DynamicSdkManager;
import cn.join.android.net.imgcache.ImageFetcher;
import cn.join.android.net.imgcache.SharedImageFetcher;
import cn.join.android.ui.widget.SuperRefreshLayout;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,MainInterf.MainView{

    private String[] strings = {"周杰伦","","",""};
    private Toolbar toolbar;
    private ImageView toolbar_leftImg;
    private TextView toolbar_titleTv;
    private DrawerLayout drawer;
    private MainNavView navigationView;
    private BottomNavigationView navigation;
    public ImageFetcher mFetcher;
    private MyApplication application;
    private MainInterf.FragmentManageInterface fragmentManager;
    private UserManager userManager;
    private User user;
    private boolean ifLogin;
    private SuperRefreshLayout mRefreshLayout;
    private MainInterf.PlayMainChild playMainChild;
    private MainInterf.SeeChild seeChild;
    private MainInterf.FindChild findChild;
    public final static String CALL_TYPE = "CALL_TYPE";

    @Override
    public void setPlayMainChild(MainInterf.PlayMainChild playMainChild) {
        this.playMainChild = playMainChild;
    }

    @Override
    public void setFindChild(MainInterf.FindChild findChild) {
        this.findChild = findChild;
    }

    @Override
    public void setSeeChild(MainInterf.SeeChild seeChild) {
        this.seeChild = seeChild;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("MainActivity--onCreate");
        setContentView(R.layout.activity_main);
        application = (MyApplication) this.getApplicationContext();
        userManager = UserManager.getInstance(this);
        checkUpdata();
        initMainView();
        initUtil();
        initFragment();
        controlData();
        getData();
        doMessage();
        ifLogin = userManager.isLogin();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        Logger.d("MainActivity--onNewIntent");
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Logger.d("MainActivity--onResume-ifLogin="+ifLogin+";userManager.isLogin()="+userManager.isLogin());
        MobclickAgent.onResume(this);
        if (ifLogin != userManager.isLogin()) {
            getData();
            ifLogin = userManager.isLogin();
        }else {
            if(ifLogin&&application.isIfUserInfoChange()){
                getData();
                application.setIfUserInfoChange(false);
            }
        }
        if(application.isIfPlayChange()){
            chooseSong();
        }
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Logger.d("MainActivity--onPause");
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }

    @Override
    public void refreshData() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopRefresh() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setEnableRefresh(boolean canRefresh) {
        mRefreshLayout.setEnabled(canRefresh);
    }

    private void doRefreshLayout() {
        if (fragmentManager == null) return;
        switch (fragmentManager.getCurrentFragmentId()) {
            case R.id.navigation_home:
                if (playMainChild != null)
                    playMainChild.doRefresh();
                break;
            case R.id.navigation_st:
                if (seeChild != null)
                    seeChild.doRefresh();
                break;
            case R.id.navigation_find:
                if (findChild != null)
                    findChild.doRefresh();
                break;
        }
    }

    public static enum CallbackType {
        NullSong,
        /**
         * 进入选取歌曲
         */
        SelectSong,
    }
    boolean isSDKLoadComplete = false;
    int checkId;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setToolbarTitle(getString(R.string.app_name));
                    break;
                case R.id.navigation_st:
                    setToolbarTitle(getString(R.string.menu_name3));
                    break;
                case R.id.navigation_find:
                    setToolbarTitle(getString(R.string.menu_name2));
                    break;
            }
            checkId = item.getItemId();
            fragmentManager.switchContent(checkId);
            supportInvalidateOptionsMenu();
            return true;
        }

    };



    private void initUtil() {
        isSDKLoadComplete = DynamicSdkManager.getInstance(this).isDexLoadCompleted();
        DynamicSdkManager.getInstance(this).initNormalAd();
        mFetcher = SharedImageFetcher.getSharedFetcher(this);
        fragmentManager = new FragmentManagerImpl(this,this);
    }


    private void initMainView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_leftImg = (ImageView) findViewById(R.id.toolbar_left_imgview);
        toolbar_titleTv = (TextView) findViewById(R.id.toolbar_title_tv);
        toolbar_leftImg.setOnClickListener(this);

        mRefreshLayout = (SuperRefreshLayout) findViewById(R.id.main_refreshLayout);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        /**
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/

        navigationView = (MainNavView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRefreshLayout.setOnRefreshHandler(new SuperRefreshLayout.OnRefreshHandler() {
            @Override
            public void refresh() {
                doRefreshLayout();
            }
        });

    }

    private void initFragment() {
        checkId = R.id.navigation_home;
        setToolbarTitle(getString(R.string.app_name));
        fragmentManager.initFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left_imgview:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
        }
    }
    MenuItem shareItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d("MainActivity","onCreateOptionsMenu---");
        getMenuInflater().inflate(R.menu.main, menu);
        shareItem = menu.findItem(R.id.action_share);
        MenuItemCompat.setShowAsAction(shareItem,MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        shareItem.setVisible(checkId == R.id.navigation_home);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            if (playMainChild != null) {
                playMainChild.doShare();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_collection) {
            gotoActvity(CollectionActivity.class, true);
        } else if (id == R.id.nav_invitation) {
            gotoActvity(InvitationActivity.class,false);
        } else if (id == R.id.nav_setting) {
            gotoActvity(SettingActivity.class,false);
        }
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void doMessage() {
        // TODO Auto-generated method stub
        String custom = getIntent().getStringExtra("custom");
        if(StringUtils.isNotNull(custom)){
            if(custom.equals("song")){
                navigation.setSelectedItemId(R.id.navigation_home);
            }else if(custom.equals("news")){
                navigation.setSelectedItemId(R.id.navigation_find);
            }
        }
    }

    private boolean checkUpdata() {
        // TODO Auto-generated method stub
        return true;
    }



    private void chooseSong() {
        // TODO Auto-generated method stub
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    private void getData() {
        // TODO Auto-generated method stub
        Logger.d("MainActivity--getdata--user="+user);
        if (userManager.isLogin()) {
            user = userManager.getUser();
            if (user==null || StringUtils.isNull(user.getUsername())) {
                userManager.refreshUserInfo(this, new UserManager.RefreshUserinfoCallback() {
                    @Override
                    public void succuss() {
                        user = userManager.getUser();
                        showUserInfo(user);
                    }
                });
            }else {
                showUserInfo(user);
            }
        }else {
            user = null;
            showUserInfo(user);
        }

    }

    private void showUserInfo(User user){
        ImageUtil.setImg(this,toolbar_leftImg,user == null?null:user.imgUrl,R.drawable.userpic);
        navigationView.setData(user);
    }

    private void controlData() {
//    	mDataBusiness.addSomeAlbum();
//    	mDataBusiness.addSomePic();
    }

    public void setToolbarLeftImg(String imgUrl) {
        ImageUtil.setImg(this,toolbar_leftImg,imgUrl,R.drawable.userpic);
    }

    private void setToolbarTitle(String title) {
        toolbar_titleTv.setText(title);
    }


    public int getCheckId() {
        return checkId;
    }

    private void gotoActvity(Class activity,boolean ifNeedLogin){
        Intent intent = new Intent(this,activity);
        if (ifNeedLogin) {
            if(userManager.isLogin()){
                startActivity(intent);
            }else{
                startActivity(new Intent(this,LoginActivity.class));
            }
        }else {
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(fragmentManager.canGoBack()){
                doBack();
            }
        }
    }


    long mLastBackExit;
    Toast mToast;
    public void doBack() {
        // TODO Auto-generated method stub
        if(SystemClock.elapsedRealtime() - mLastBackExit > 3000){
            if(mToast == null)
                mToast = Toast.makeText(this, getString(R.string.exit_app), Toast.LENGTH_SHORT);
            mToast.show();
            mLastBackExit = SystemClock.elapsedRealtime();
            return;
        }
        this.finish();
    }
}
