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

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzisoo.toylibrary.App;
import com.zzisoo.toylibrary.Config;
import com.zzisoo.toylibrary.R;
import com.zzisoo.toylibrary.vo.Toy;

import java.util.Random;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class ToyListAdapter extends RecyclerView.Adapter<ToyListAdapter.ViewHolder> {
    private static final String TAG = "ToyListAdapter";

    private Toy[] mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTvTitle;
        private final TextView mTvDesc;
        private final ImageView mIvToyImage;
        private final View vh;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getPosition() + " clicked.");
                }
            });
            vh = v;
            mTvTitle = (TextView) v.findViewById(R.id.tvToyTitle);
            mTvDesc = (TextView) v.findViewById(R.id.tvToyDesc);
            mIvToyImage = (ImageView) v.findViewById(R.id.ivToyImage);;

            ImageView ivToyImage = (ImageView) v.findViewById(R.id.ivToyImage);
            ivToyImage.setImageResource(0);

        }

        public TextView getTvTitle() {
            return mTvTitle;
        }

        public TextView getTvDesc() {
            return mTvDesc;
        }

        public ImageView getIvToyImage() {
            return mIvToyImage;
        }

        public View getVh() {
            return vh;
        }



    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public ToyListAdapter(Toy[] dataSet) {
        mDataSet = dataSet;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.toy_item_view, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        View v = viewHolder.getVh();
        ImageView ivToyImage = viewHolder.getIvToyImage();
        TextView tvToyDesc = viewHolder.getTvDesc();
        TextView tvToyTitle = viewHolder.getTvTitle();

        Toy toy = mDataSet[position];

        tvToyTitle.setText(toy.getTitle());
        tvToyDesc.setText(toy.getDescription());
        String bgImage = Config.URL_SERVER + toy.getImage().replace("..","");

        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        int R = r.nextInt(255);
        int G = r.nextInt(255);
        int B = r.nextInt(255);

        int strRBG = Color.rgb(R, G, B);
        ivToyImage.setBackgroundColor(strRBG);
        Log.e(TAG, ">>>>>" +toy.getDescription());
        ivToyImage.setImageResource(0);
        App.getImageLoader(v.getContext()).displayImage(bgImage, ivToyImage);

    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}
