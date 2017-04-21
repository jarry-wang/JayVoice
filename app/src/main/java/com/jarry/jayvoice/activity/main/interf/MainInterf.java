package com.jarry.jayvoice.activity.main.interf;

import android.support.v4.app.Fragment;

import com.jarry.jayvoice.activity.main.fragment.BaseFragment;
import com.jarry.jayvoice.bean.User;

/**
 * Created by mlfdev1 on 2017/4/19.
 */

public interface MainInterf {
    interface FragmentManageInterface{
        void initFragment();
        void switchReplaceContent(int toId);
        void switchContent(int toId);
        BaseFragment getFragmentByTag(int tag);
        boolean canGoBack();
        int getCurrentFragmentId();
    }

    interface NavViewManageInterface {
        void setData(User user);
    }

    interface MainView {
        void setPlayMainChild(PlayMainChild playMainChild);
        void setFindChild(FindChild findChild);
        void setSeeChild(SeeChild seeChild);
        void refreshData();
        void stopRefresh();
        void setEnableRefresh(boolean canRefresh);
    }

    interface PlayMainChild {
        void doShare();
        void doRefresh();
    }

    interface FindChild {
        void doRefresh();
    }

    interface SeeChild {
        void doRefresh();
    }
}
