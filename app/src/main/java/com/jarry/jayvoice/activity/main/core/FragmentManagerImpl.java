package com.jarry.jayvoice.activity.main.core;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jarry.jayvoice.R;
import com.jarry.jayvoice.activity.main.fragment.BaseFragment;
import com.jarry.jayvoice.activity.main.fragment.FindFragment;
import com.jarry.jayvoice.activity.main.fragment.PlayMainFragment;
import com.jarry.jayvoice.activity.main.fragment.SeeFragment;
import com.jarry.jayvoice.activity.main.interf.MainInterf;

/**
 * Created by mlfdev1 on 2017/4/20.
 */

public class FragmentManagerImpl implements MainInterf.FragmentManageInterface{
    Fragment currentFragment;
    int currentFragmentId;
    PlayMainFragment playMainFragment;
    FindFragment findFragment;
    SeeFragment seeFragment;
    FragmentManager fmManager;
    MainInterf.MainView mainView;
    public FragmentManagerImpl(AppCompatActivity context, MainInterf.MainView mainView) {
        fmManager = context.getSupportFragmentManager();
        this.mainView = mainView;
    }

    @Override
    public void initFragment() {
        playMainFragment = (PlayMainFragment) getFragmentByTag(R.id.navigation_home);
        if(!playMainFragment.isAdded()){
            FragmentTransaction ft = fmManager.beginTransaction();
            ft.add(R.id.fragmentRoot, playMainFragment, String.valueOf(R.id.navigation_home));
            ft.commit();
        }
        currentFragment = playMainFragment;
        currentFragmentId = R.id.navigation_home;
    }

    @Override
    public void switchReplaceContent(int toId) {
        Fragment toFragment = getFragmentByTag(toId);
        if(currentFragment != toFragment) {
            fmManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragmentRoot, toFragment) // 替换Fragment，实现切换
                    .commit();
            currentFragment = toFragment;
            currentFragmentId = toId;
        }
    }

    @Override
    public void switchContent(int toId) {
        Fragment toFragment = getFragmentByTag(toId);
        if (currentFragment != toFragment) {
            FragmentTransaction transaction = fmManager.beginTransaction();
            if (!toFragment.isAdded()) {    // 先判断是否被add过
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).hide(currentFragment).add(R.id.fragmentRoot, toFragment, String.valueOf(toId)).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(currentFragment).show(toFragment).commit(); // 隐藏当前的fragment，显示下一个
            }
            currentFragment = toFragment;
            currentFragmentId = toId;
        }
    }

    @Override
    public BaseFragment getFragmentByTag(int tag) {
        Log.d(this.getClass().getSimpleName(),"getFragmentByTag--tag="+tag);
        BaseFragment f = (BaseFragment) fmManager.findFragmentByTag(String.valueOf(tag));
        Log.d(this.getClass().getSimpleName(),"getFragmentByTag--f="+f);
        switch (tag) {
            case R.id.navigation_home:
                if(f==null)
                    f = new PlayMainFragment(this.mainView);
                break;
            case R.id.navigation_find:
                if(f==null)
                    f = new FindFragment(this.mainView);
                break;
            case R.id.navigation_st:
                if(f==null)
                    f = new SeeFragment(this.mainView);
                break;
        }

        return f;
    }

    @Override
    public boolean canGoBack() {
        if(currentFragment == findFragment){
            findFragment.onBackPressd();
            return false;
        }else {
            return true;
        }
    }

    @Override
    public int getCurrentFragmentId() {
        return currentFragmentId;
    }
}
