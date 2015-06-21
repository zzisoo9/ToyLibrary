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
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.zzisoo.toylibrary.R;
import com.zzisoo.toylibrary.adapter.ProductListAdapter;
import com.zzisoo.toylibrary.vo.Product;

/**
 * Demonstrates the use of {@link RecyclerView} with a {@link LinearLayoutManager} and a
 * {@link GridLayoutManager}.
 */
public class ProductListViewFragment extends Fragment {
    public static final int MSG_FINISH = 1;
    public static final int MSG_OBJ = 2;
    private Handler mActivityHandler;

    private static final String TAG = "ToyListViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 1;

    protected RecyclerView mProductListView;
    protected ProductListAdapter mAdapter;
    protected Product[] mProducts;
    protected RecyclerView.LayoutManager mLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_list_view_frag, container, false);
        rootView.setTag(TAG);
        String strProducts = getArguments().getString("Products");
        Gson gson = new Gson();
        mProducts = gson.fromJson(strProducts, Product[].class);

        mAdapter = new ProductListAdapter(mProducts);

        mProductListView = (RecyclerView) rootView.findViewById(R.id.productListView);
        mProductListView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity());
        int scrollPosition = 0;

        if (mProductListView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mProductListView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }
        mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        mProductListView.setLayoutManager(mLayoutManager);
        mProductListView.scrollToPosition(scrollPosition);

        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }



}
