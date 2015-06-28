package com.zzisoo.toylibrary.activity;


import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.zzisoo.toylibrary.Config;
import com.zzisoo.toylibrary.R;
import com.zzisoo.toylibrary.SharedPref;
import com.zzisoo.toylibrary.adapter.ToyListAdapter;
import com.zzisoo.toylibrary.fragment.ToyListViewFragment;
import com.zzisoo.toylibrary.vo.Toy;

import java.util.ArrayList;


public class MainListActivity extends BaseActivity {

    private static final String TAG = "MainListActivity";
    private Context mContext;
    ListView mLvNavDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_mainlist);

        mLvNavDrawer = (ListView) findViewById(R.id.lv_activity_mainlist_nav_drawer);
        DrawerLayout dlActivityMainlistRoot = (DrawerLayout) findViewById(R.id.dl_activity_mainlist_root);
        FrameLayout flActivityMainlistContentRoot = (FrameLayout) findViewById(R.id.fl_activity_mainlist_content_root);

        setToolBar("ToyToI", null);
        if (Config.IS_PUSH_DRAWER) {
            setDrawerAnimation(flActivityMainlistContentRoot, dlActivityMainlistRoot);
        }

        String[] navItems = {"1", "2", "3", "4", "5"};

        mLvNavDrawer.setAdapter(
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navItems));
        mLvNavDrawer.setOnItemClickListener(new DrawerItemClickListener());

        setConfig();


        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ToyListViewFragment fragment = new ToyListViewFragment();
            transaction.replace(R.id.fl_activity_mainlist_content_fragment, fragment);
            transaction.addToBackStack(getClass().getSimpleName());
            transaction.commit();
        }
    }


    private void setDrawerAnimation(final FrameLayout mainView, final DrawerLayout drawerLayout) {

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mTbHiddenable, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mainView.setTranslationX(slideOffset * drawerView.getWidth());
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
            }
        };

        drawerLayout.setDrawerListener(mDrawerToggle);

        mTbHiddenable.setNavigationIcon(R.drawable.ic_drawer);

        final TextView tvActivityMainlistHiddenToolbarFavorite = (TextView) mTbHiddenable.findViewById(R.id.tv_activity_mainlist_hidden_toolbar_favorite);
        tvActivityMainlistHiddenToolbarFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObservableRecyclerView or = (ObservableRecyclerView) (findViewById(R.id.toyListRecyclerView));
                ToyListAdapter adapter = (ToyListAdapter) or.getAdapter();

                CharSequence text = tvActivityMainlistHiddenToolbarFavorite.getText();
                if (Config.IS_FAVORITE_VIEW) {

                    adapter.mDataSet = Config.backupDataSet;
                    adapter.notifyItemRangeChanged(0, adapter.mDataSet.size());
                    tvActivityMainlistHiddenToolbarFavorite.setText(getString(R.string.star));
                    Config.IS_FAVORITE_VIEW = false;
                } else {
                    Config.backupDataSet = adapter.mDataSet;
                    ArrayList<Toy> newData = new ArrayList<Toy>();
                    SharedPref pref = new SharedPref(getApplicationContext());
                    ArrayList<String> tmpList = pref.getStringArrayList(SharedPref.PREF_FAVORITE_LIST);
                    for (Toy toy : adapter.mDataSet) {
                        if (tmpList.contains(toy.getStrPid())) {
                            Log.e(TAG, ">>>>" + toy.getStrPid());
                            newData.add(toy);
                        }
                    }
                    adapter.mDataSet = newData;
                    adapter.notifyItemRangeChanged(0, adapter.mDataSet.size());
                    tvActivityMainlistHiddenToolbarFavorite.setText(getString(R.string.x));
                    Config.IS_FAVORITE_VIEW = true;

                }
            }
        });


    }

    private void setConfig() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        Config.DisplayWidth = display.getWidth();
        Config.DisplayToolbarHeight = getActionBarSize();
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


    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position,
                                long id) {


            int num = position + 1;

            Context context = view.getContext();
            GridLayoutManager mLayoutManager = new GridLayoutManager(context, num);
            Config.setSpans(context, num);
            ObservableRecyclerView mToyListView = (ObservableRecyclerView) findViewById(R.id.toyListRecyclerView);
            mToyListView.setLayoutManager(mLayoutManager);
            mToyListView.getAdapter().notifyItemRangeChanged(0, mToyListView.getAdapter().getItemCount());

        }
    }
}
