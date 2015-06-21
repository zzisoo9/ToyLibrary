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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.zzisoo.toylibrary.Config;
import com.zzisoo.toylibrary.R;
import com.zzisoo.toylibrary.adapter.ToyListAdapter;
import com.zzisoo.toylibrary.popup.IntroProgressPopupDialog;
import com.zzisoo.toylibrary.vo.Product;
import com.zzisoo.toylibrary.vo.Toy;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

/**
 * Demonstrates the use of {@link RecyclerView} with a {@link LinearLayoutManager} and a
 * {@link GridLayoutManager}.
 */
public class ListViewFragment extends Fragment {
    public static final int MSG_FINISH = 1;
    public static final int MSG_OBJ = 2;
    private Handler mActivityHandler;

    private static final String TAG = "ListViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 1;
    private static final int DATASET_COUNT = 60;
    private LayoutManagerType mLayoutType;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER,
        STAGGEREDGRID_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mToyListView;
    protected ToyListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.toy_list_view_frag, container, false);
        rootView.setTag(TAG);
        /**
         * AsyncClient에서 결과를 넘겨줄때사용.
         * MSG_FINISH : Error 또는 BackPress 받아서 앱종료할때 사용
         */
        mActivityHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1 == MSG_FINISH){
                    getActivity().finish();
                }else{
                    Toy[] list = (Toy[])msg.obj;

                    for(Toy toy : list){
                        Log.v(TAG, toy.getTitle());
                        Product[] tmp = toy.getProducts();
                        for(Product product : tmp){
                            Log.v(TAG,product.getPid() +"/"+product.getIsLent());
                        }
                    }

                    mAdapter = new ToyListAdapter(list);
                    // Set ToyListAdapter as the adapter for RecyclerView.
                    mToyListView.setAdapter(mAdapter);
                }
            }
        };

        // BEGIN_INCLUDE(initializeRecyclerView)
        mToyListView = (RecyclerView) rootView.findViewById(R.id.toyListView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());


        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mLayoutType = (LayoutManagerType) savedInstanceState.getSerializable(KEY_LAYOUT_MANAGER);
        }else{
            mLayoutType = LayoutManagerType.STAGGEREDGRID_LAYOUT_MANAGER;
        }

        setRecyclerViewLayoutManager(LayoutManagerType.STAGGEREDGRID_LAYOUT_MANAGER);
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
        if (mToyListView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mToyListView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            case STAGGEREDGRID_LAYOUT_MANAGER:
                mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT,StaggeredGridLayoutManager.VERTICAL);
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mLayoutType);
        super.onSaveInstanceState(savedInstanceState);
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
                Message msg = new Message();
                msg.arg1 = MSG_OBJ;


                Gson gson = new Gson();
                Toy[] arrToy = gson.fromJson(responseStr, Toy[].class);


                msg.obj = arrToy;

                mActivityHandler.dispatchMessage(msg);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.d(TAG, "Http Get Failure");
                Log.d(TAG, "Code [" + i + "] ");
                mIntroProgressPopup.setProgressBarVisible(false);
                mIntroProgressPopup.setText("ERROR Code: " + i);
                Runnable mRunnable = new Runnable() {
                    @Override
                    public void run() {

                    }
                };
                delayedFinish();
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
        });
    }
}
