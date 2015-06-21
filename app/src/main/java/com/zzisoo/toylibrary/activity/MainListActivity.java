package com.zzisoo.toylibrary.activity;


import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.Window;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.zzisoo.toylibrary.Config;
import com.zzisoo.toylibrary.R;
import com.zzisoo.toylibrary.fragment.ToyListViewFragment;


public class MainListActivity extends FragmentActivity {


    private static final String TAG = "MainListActivity";

    private SlidingUpPanelLayout mLayout;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);
        setConfig();

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ToyListViewFragment fragment = new ToyListViewFragment();
            transaction.replace(R.id.main_list_fragment, fragment);
            transaction.addToBackStack(getClass().getSimpleName());
            transaction.commit();
        }
    }

    private void setConfig() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        Config.DISPLAY_WIDTH = display.getWidth();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 1) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
            removeCurrentFragment();
        }
    }

    public void removeCurrentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.main_list_fragment);


        String fragName = "NONE";

        if (currentFrag != null)
            fragName = currentFrag.getClass().getSimpleName();


        if (currentFrag != null) {
            transaction.remove(currentFrag);
        }

        transaction.commit();

    }
}
