/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zzisoo.toylibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zzisoo.toylibrary.Config;
import com.zzisoo.toylibrary.R;
import com.zzisoo.toylibrary.activity.BaseActivity;
import com.zzisoo.toylibrary.vo.Product;

import java.util.ArrayList;

public class SimpleHeaderRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private static final String TAG = "SHRA" ;

    private LayoutInflater mInflater;
    private ArrayList<Product> mItems;
    private View mHeaderView;

    public SimpleHeaderRecyclerAdapter(Context context, ArrayList<Product> items, View headerView) {
        mInflater = LayoutInflater.from(context);
        mItems = items;
        mHeaderView = headerView;
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null) {
            return mItems.size();
        } else {
            return mItems.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return new HeaderViewHolder(mHeaderView);
        } else {
            return new ItemViewHolder(mInflater.inflate(R.layout.product_item_view, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            position--;
            ((ItemViewHolder) viewHolder).getTvDescription().setText(mItems.get(position).getDescription());
            ((ItemViewHolder) viewHolder).getTvCategory().setText(mItems.get(position).getCategory());
            ((ItemViewHolder) viewHolder).getTvGoods().setText(mItems.get(position).getGoods());
            ((ItemViewHolder) viewHolder).getTvManufacturer().setText(mItems.get(position).getManufacturer());
            ((ItemViewHolder) viewHolder).getTvRecommendAge().setText(mItems.get(position).getRecommendAge());
            ((ItemViewHolder) viewHolder).getTvPid().setText(mItems.get(position).getPid());
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvPid;
        TextView tvCategory;
        TextView tvGoods;
        TextView tvRecommendAge;
        TextView tvManufacturer;

        TextView tvDescription;

        public ItemViewHolder(View view) {
            super(view);
            tvPid = (TextView) view.findViewById(R.id.tvPid);
            tvCategory = (TextView) view.findViewById(R.id.tvCategory);
            tvGoods = (TextView) view.findViewById(R.id.tvGoods);
            tvRecommendAge = (TextView) view.findViewById(R.id.tvRecommendAge);
            tvManufacturer = (TextView) view.findViewById(R.id.tvManufacturer);
            tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        }

        public TextView getTvDescription() {
            return tvDescription;
        }

        public TextView getTvPid() {
            return tvPid;
        }

        public TextView getTvCategory() {
            return tvCategory;
        }

        public TextView getTvGoods() {
            return tvGoods;
        }

        public TextView getTvRecommendAge() {
            return tvRecommendAge;
        }

        public TextView getTvManufacturer() {
            return tvManufacturer;
        }

    }
    private void setItemSize(View v) {
        if (Config.isLandscape(v.getContext())) {
            ((TextView) v.findViewById(R.id.tvToyTitle)).setMaxLines(Config.TITLE_MAX_LINE_LANDSCAPE);
        } else {
            ((TextView) v.findViewById(R.id.tvToyTitle)).setMaxLines(Config.TITLE_MAX_LINE_PORTRAIT);
        }
        int nwidth = ((BaseActivity)v.getContext()).getShortWidth();
        v.findViewById(R.id.imageview_Background).setLayoutParams(
                new LinearLayout.LayoutParams(nwidth, nwidth));
    }


}
