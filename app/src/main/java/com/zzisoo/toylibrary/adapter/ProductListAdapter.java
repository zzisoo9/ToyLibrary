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

package com.zzisoo.toylibrary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzisoo.toylibrary.App;
import com.zzisoo.toylibrary.Config;
import com.zzisoo.toylibrary.R;
import com.zzisoo.toylibrary.vo.Product;

import java.util.Random;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private static final String TAG = "ToyListAdapter";

    private Product[] mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView getmTvProductPid() {
            return mTvProductPid;
        }

        public TextView getmTvProductTitle() {
            return mTvProductTitle;
        }

        public ImageView getmIvProductImageView() {
            return mIvProductImageView;
        }

        private final ImageView mIvProductImageView;
        private final TextView mTvProductPid;
        private final TextView mTvProductTitle;
        private final View vh;

        public ViewHolder(View v) {
            super(v);
            mIvProductImageView = null;

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "All Element " + getPosition() + " clicked.");
                }
            });
            vh = v;

            mTvProductPid = (TextView) v.findViewById(R.id.tvDescription);
            mTvProductTitle = (TextView) v.findViewById(R.id.tvCategory);
        }

        public View getVh() {
            return vh;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public ProductListAdapter(Product[] dataSet) {
        mDataSet = dataSet;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_item_view, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        View v = viewHolder.getVh();
        ImageView ivProductImageView = viewHolder.getmIvProductImageView();
        TextView tvProductPid = viewHolder.getmTvProductPid();
        TextView tvProductDescription = viewHolder.getmTvProductTitle();

        Product product = mDataSet[position];
        String bgImage = Config.HOST_SERVER_URL + product.getImage().replace("..", "");

        App.getImageLoader(v.getContext()).displayImage(bgImage, ivProductImageView);
        tvProductPid.setText(product.getPid());
        tvProductDescription.setText(product.getDescription());

        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        int R = r.nextInt(125)+ 125;
        int G = r.nextInt(125)+ 125;
        int B = r.nextInt(125)+ 125;

        int strRBG = Color.rgb(R, G, B);
        v.setBackgroundColor(strRBG);
        setItemSize(v);

    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }


    private void setItemSize(View v) {
        Context c = v.getContext();
        WindowManager wm = (WindowManager) v.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int x = display.getWidth();
        int y = display.getHeight();
        int nwidth = x < y ? x : y;
        if (Config.isLandscape(c)) {

        } else {
        }


       // v.findViewById(R.id.flToyImageProductBackground).setLayoutParams(new LinearLayout.LayoutParams(nwidth, nwidth));
    }

}
