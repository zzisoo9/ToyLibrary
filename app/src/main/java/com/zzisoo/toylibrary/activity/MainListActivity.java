package com.zzisoo.toylibrary.activity;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.widget.TextView;

import com.zzisoo.toylibrary.Config;
import com.zzisoo.toylibrary.R;
import com.zzisoo.toylibrary.fragment.ToyListViewFragment;
import com.zzisoo.toylibrary.utils.Typefaces;

import me.grantland.widget.AutofitHelper;


public class MainListActivity extends BaseActivity  {

    private static final String TAG = "MainListActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mContext = this;
        setContentView(R.layout.activity_main);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        TextView tv = (TextView) mToolbar.findViewById(R.id.toobarTitle);
        AutofitHelper.create(tv);

        tv.setTypeface(Typefaces.get(mContext, "Satisfy-Regular.ttf"));
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

        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.middle);


        String fragName = "NONE";

        if (currentFrag != null)
            fragName = currentFrag.getClass().getSimpleName();


        if (currentFrag != null) {
            transaction.remove(currentFrag);
        }

        transaction.commit();

    }


    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }



}
