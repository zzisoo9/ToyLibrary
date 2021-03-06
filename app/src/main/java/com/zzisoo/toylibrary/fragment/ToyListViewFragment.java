/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.zzisoo.toylibrary.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.zzisoo.toylibrary.Config;
import com.zzisoo.toylibrary.R;
import com.zzisoo.toylibrary.SharedPref;
import com.zzisoo.toylibrary.activity.BaseActivity;
import com.zzisoo.toylibrary.adapter.ToyListAdapter;
import com.zzisoo.toylibrary.popup.IntroProgressPopupDialog;
import com.zzisoo.toylibrary.vo.Toy;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

/**
 * Demonstrates the use of {@link RecyclerView} with a {@link LinearLayoutManager} and a
 * {@link GridLayoutManager}.
 */
public class ToyListViewFragment extends Fragment  {
    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";

    public static final int MSG_FINISH = 1;
    public static final int MSG_OBJ = 2;
    private static final String BUNDLE_RECYCLER_LAYOUT = "BUNDLE_RECYCLER_LAYOUT";
    private Handler mActivityHandler;

    private static final String TAG = "ToyListView";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 1;
    private LayoutManagerType mLayoutType;
    private SharedPref mPref;
    private Bundle mArguments;
    private Context mContext;

    private int lastScrollPosition = 0;

    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER,
        STAGGEREDGRID_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected ObservableRecyclerView mToyListView;
    protected ToyListAdapter mAdapter;
    protected ObservableRecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPref = new SharedPref(getActivity());
        mContext = (Context) getActivity();
    }


    @Override
    public void onResume() {
        super.onResume();
        mToyListView.setAdapter(mAdapter);
        if (mAdapter != null) {
            final int lastClickedPostion = mAdapter.getClickedPostion();
            final int lastClickedPrePostion = lastClickedPostion - 1 < 0 ? 0 : lastClickedPostion - 1;
            Log.e(TAG, "mClickedPostion:" + lastClickedPostion);
            mToyListView.scrollToPosition(lastClickedPrePostion);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mToyListView.smoothScrollToPosition(lastClickedPostion);
                }
            }, 100);
        }
    }


    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mLayoutManager = new GridLayoutManager(getActivity(), Config.getSpans(getActivity()));
        mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        mToyListView.setLayoutManager(mLayoutManager);
        Log.e(TAG, "lastScrollPosition:" + lastScrollPosition);
        mToyListView.getAdapter().notifyItemRangeChanged(0,lastScrollPosition);
        mToyListView.scrollToPosition(lastScrollPosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.toy_list_view_frag, container, false);
        mToyListView = (ObservableRecyclerView) rootView.findViewById(R.id.toyListRecyclerView);

        ((BaseActivity)mContext).mScrollableChildView = mToyListView;

        mToyListView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                lastScrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                Log.e(TAG, visibleItemCount + "/" + totalItemCount + "/" + lastScrollPosition);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        if(Config.IS_HIDDENABLE_TOOLBAR) {
            mToyListView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
                @Override
                public void onScrollChanged(int i, boolean b, boolean b1) {

                }

                @Override
                public void onDownMotionEvent() {

                }

                @Override
                public void onUpOrCancelMotionEvent(ScrollState scrollState) {
                    BaseActivity act = (BaseActivity) mContext;
                    Log.e("DEBUG", "onUpOrCancelMotionEvent: " + scrollState);
                    if (scrollState == ScrollState.UP) {
                        if (act.toolbarIsShown()) {
                            act.hideToolbar();
                        }
                    } else if (scrollState == ScrollState.DOWN) {
                        if (act.toolbarIsHidden()) {
                            act.showToolbar();
                        }
                    }
                }
            });
        }
        rootView.setTag(TAG);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mActivityHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.arg1 == MSG_FINISH) {
                    getActivity().finish();
                } else {
                    Toy[] list = (Toy[]) msg.obj;
                    Gson gson = new Gson();
                    String strList = gson.toJson(list);

                    mAdapter = new ToyListAdapter(list);
                    mToyListView.setAdapter(mAdapter);
                }
            }
        };

        String oldData = mPref.getStringPref(SharedPref.PREF_TOYS_LIST);
        if (oldData.equals(SharedPref.NODATA_STRING)) {
            getData();
        } else {
            dataLoad(oldData);
        }

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mLayoutType = (LayoutManagerType) savedInstanceState.getSerializable(KEY_LAYOUT_MANAGER);
        } else {
            mLayoutType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        }

        setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        scrollPosition = getScrollInitPosition();

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), Config.getSpans(getActivity()));
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            case STAGGEREDGRID_LAYOUT_MANAGER:
                mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
                mCurrentLayoutManagerType = LayoutManagerType.STAGGEREDGRID_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
        }


        mToyListView.setLayoutManager(mLayoutManager);
        mToyListView.scrollToPosition(scrollPosition);
    }

    private int getScrollInitPosition() {
        int scrollPosition = 0;
        if (mToyListView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mToyListView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            Log.e(TAG , "scrollPosition:"+scrollPosition);
        }
        return scrollPosition;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mLayoutType);
        super.onSaveInstanceState(savedInstanceState);

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mToyListView.getLayoutManager().onSaveInstanceState());

    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mToyListView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }

    }

    private void getData() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.get(getActivity(), Config.URL_BOOKS_LIST, new AsyncHttpResponseHandler() {
            private IntroProgressPopupDialog mIntroProgressPopup;

            @Override
            public void onStart() {
                super.onStart();
                mIntroProgressPopup = new IntroProgressPopupDialog(getActivity());
                mIntroProgressPopup.setProgressBarVisible(false);
                mIntroProgressPopup.show();
            }

            @Override
            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                super.onPreProcessResponse(instance, response);
            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                mIntroProgressPopup.setProgressBarVisible(true);

                super.onProgress(bytesWritten, totalSize);
                Log.v(TAG, bytesWritten + "/" + totalSize);
                mIntroProgressPopup.setProgress((int) (100 * bytesWritten / totalSize));
            }


            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.d(TAG, "Http Get Success");
                String responseStr = new String(bytes);
                Log.d(TAG, "[" + i + "] Received Msg:" + responseStr);
                mIntroProgressPopup.dismiss();

                mPref.setUpdateMode();
                mPref.setStringPref(SharedPref.PREF_TOYS_LIST, responseStr);
                mPref.updateFinish();
                dataLoad(responseStr);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.d(TAG, "Http Get Failure");
                Log.d(TAG, "Code [" + i + "] ");
                mIntroProgressPopup.setProgressBarVisible(false);
                mIntroProgressPopup.setText("ERROR Code: " + i);

                String oldData = mPref.getStringPref(SharedPref.PREF_TOYS_LIST);
                if (oldData.equals(SharedPref.NODATA_STRING)) {
                    delayedFinish();
                } else {
                    delayLoadOldData(oldData);
                }
            }

            private void delayedFinish() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIntroProgressPopup.setText("Retry. Check Network.");
                    }
                }, 1000);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIntroProgressPopup.setText("Will Finish.");
                    }
                }, 2000);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.arg1 = MSG_FINISH;
                        mActivityHandler.dispatchMessage(msg);
                    }
                }, 5000);
            }

            private void delayLoadOldData(final String oldData) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIntroProgressPopup.setText("ERROR But Found OldData.");
                    }
                }, 1000);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIntroProgressPopup.dismiss();
                        dataLoad(oldData);
                    }
                }, 2000);


            }


        });

    }

    public void dataLoad(String strData) {
        Gson gson = new Gson();
        Toy[] arrToy = gson.fromJson(strData, Toy[].class);
        Message msg = new Message();
        msg.arg1 = MSG_OBJ;
        msg.obj = arrToy;
        mActivityHandler.dispatchMessage(msg);
    }

}
