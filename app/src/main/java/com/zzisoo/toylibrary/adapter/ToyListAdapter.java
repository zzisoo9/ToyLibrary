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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.zzisoo.toylibrary.App;
import com.zzisoo.toylibrary.Config;
import com.zzisoo.toylibrary.R;
import com.zzisoo.toylibrary.SharedPref;
import com.zzisoo.toylibrary.activity.FlexibleSpaceWithImageRecyclerViewActivity;
import com.zzisoo.toylibrary.fragment.ProductListViewFragment;
import com.zzisoo.toylibrary.utils.Typefaces;
import com.zzisoo.toylibrary.vo.Product;
import com.zzisoo.toylibrary.vo.Toy;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Random;

import fr.castorflex.android.flipimageview.library.FlipImageView;
import me.grantland.widget.AutofitHelper;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class ToyListAdapter extends RecyclerView.Adapter<ToyListAdapter.ViewHolder> {
    private static final String TAG = "ToyListAdapter";


    public static ArrayList<Toy> mDataSet;

    private SharedPref mPref;
    private ArrayList<String> mFavList;

    public static int getClickedPostion() {
        return mClickedPostion;
    }

    public static int getClickedOldPostion() {
        return mClickedOldPostion;
    }

    private static int mClickedPostion = 0;
    private static int mClickedOldPostion = 0;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mPref = new SharedPref(recyclerView.getContext());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTvTitle;
        private final ImageView mIvToyImage;
        private final TextView mTvLoading;
        private final FrameLayout mTvLoadingBackground;
        private FlipImageView mFlipImageView;

        private final View vh;
        private long ANIM_DURATION = 1000;

        public ViewHolder(View v) {
            super(v);
            final int position = getPosition();

            Log.e(TAG, "ViewHolder #" + position);

            vh = v;
            mTvTitle = (TextView) v.findViewById(R.id.tvToyTitle);
            mIvToyImage = (ImageView) v.findViewById(R.id.ivToyImage);
            mTvLoading = (TextView) v.findViewById(R.id.tvLoading);
            mTvLoadingBackground = (FrameLayout) v.findViewById(R.id.imageview_Background);
            mFlipImageView = (FlipImageView) v.findViewById(R.id.imageview);
            AutofitHelper.create(mTvTitle);

        }

        public TextView getTvTitle() {
            return mTvTitle;
        }

        public ImageView getIvToyImage() {
            return mIvToyImage;
        }

        public TextView getTvLoading() {
            return mTvLoading;
        }


        public FrameLayout getTvLoadingBackground() {
            return mTvLoadingBackground;
        }

        public View getVh() {
            return vh;
        }


        public FlipImageView getFlipImageView() {
            return mFlipImageView;
        }
    }

    public ToyListAdapter(Toy[] dataSet) {
        mDataSet = new ArrayList<Toy>();
        for (Toy item : dataSet) {
            mDataSet.add(item);
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.toy_item_view, viewGroup, false);

        //setItemSize(v);
        TextView tvLoading = (TextView) (v.findViewById(R.id.tvLoading));
        tvLoading.setTypeface(Typefaces.get(v.getContext(), "Satisfy-Regular.ttf"));

        return new ViewHolder(v);
    }

    private void setItemSize(View v) {
        Context c = v.getContext();
        WindowManager wm = (WindowManager) v.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int x = display.getWidth();
        int y = display.getHeight();
        int nwidth = x;
        if (Config.isLandscape(c)) {
            ((TextView) v.findViewById(R.id.tvToyTitle)).setMaxLines(Config.TITLE_MAX_LINE_LANDSCAPE);
            nwidth = x > y ? x : y;
        } else {
            ((TextView) v.findViewById(R.id.tvToyTitle)).setMaxLines(Config.TITLE_MAX_LINE_PORTRAIT);
            nwidth = x < y ? x : y;
        }
        int nItemPerWidth = nwidth / Config.getSpans(c);
        Log.e(TAG, ">>>" + x + "/" + y + " -->" + nItemPerWidth);

        v.findViewById(R.id.imageview_Background).setLayoutParams(
                new LinearLayout.LayoutParams(nItemPerWidth, nItemPerWidth));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.e(TAG, "onBindViewHolder #" + position);

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        View v = viewHolder.getVh();
        final ImageView ivToyImage = viewHolder.getIvToyImage();
        final TextView textView = viewHolder.getTvTitle();
        final FlipImageView flipImageView = viewHolder.getFlipImageView();

        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strPid = mDataSet.get(position).getStrPid();

                if (mPref.getStringArrayList(SharedPref.PREF_FAVORITE_LIST).contains(strPid)) {
                    mPref.removeStringArrayListItem(SharedPref.PREF_FAVORITE_LIST, strPid);
                } else {
                    mPref.addStringArrayListItem(SharedPref.PREF_FAVORITE_LIST, strPid);
                }
                flipImageView.toggleFlip();
            }
        };
        textView.setOnClickListener(l);
        flipImageView.setOnClickListener(l);

        viewHolder.getTvLoadingBackground().setBackgroundColor(getPastelRBG());
        viewHolder.getTvLoading().setText("ToyToI");
        if (-1 < position && position < mDataSet.size()) {
            setItemSize(v);

            mFavList = mPref.getStringArrayList(SharedPref.PREF_FAVORITE_LIST);
            if (mFavList.contains(mDataSet.get(position).getStrPid())) {
                flipImageView.setFlipped(true);
            } else {
                flipImageView.setFlipped(false);
            }

            final Toy toy = mDataSet.get(position);

            String bgImage = Config.HOST_SERVER_URL + toy.getImage().replace("..", "");
            String strTitle = toy.getTitle();
            textView.setText(strTitle);//

            ImageLoadingListener fadeImageLoadingListener = new ImageLoadingListener() {

                long ANIM_DURATION = 500;

                @Override
                public void onLoadingStarted(String s, View view) {
                    view.clearAnimation();
                    ((ImageView) view).setImageResource(R.drawable.alpha_zero);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    Toast.makeText(view.getContext(), "Network Error : " + failReason.getType().toString(), Toast.LENGTH_LONG).show();
                    ((TextView) ((FrameLayout) view.getParent()).findViewById(R.id.tvLoading)).setText("Error");
                    Log.e(TAG, "onLoadingFailed :" + s);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    ((ImageView) view).setImageBitmap(bitmap);
                    Animation fadeIn = new AlphaAnimation(0, 1);
                    fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
                    fadeIn.setDuration(ANIM_DURATION);
                    view.clearAnimation();
                    view.startAnimation(fadeIn);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    if (view != null) {
                        ((TextView) ((FrameLayout) view.getParent()).findViewById(R.id.tvLoading)).setText("Error");
                        Log.e(TAG, "onLoadingCancelled :" + s);
                    }
                }
            };

            App.getImageLoader(v.getContext()).displayImage(bgImage, ivToyImage, fadeImageLoadingListener);
            ivToyImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

                    asyncHttpClient.get(v.getContext(), Config.URL_DETAIL + mDataSet.get(position).getStrPid(), new AsyncHttpResponseHandler() {


                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            String responseStr = new String(bytes);
                            Context context = v.getContext();

                            Bundle bundle = new Bundle();
                            Gson gson = new Gson();
                            Product[] products = gson.fromJson(responseStr, Product[].class);

                            Intent intent = new Intent(context, FlexibleSpaceWithImageRecyclerViewActivity.class);
                            intent.putExtra("Products", gson.toJson(products));
                            intent.putExtra("title", toy.getTitle());
                            intent.putExtra("image", toy.getImage());
                            intent.putExtra("favorite", mPref.getStringArrayList(SharedPref.PREF_FAVORITE_LIST).contains(toy.getStrPid()));
                            context.startActivity(intent);

                            //changeFragment
                        }

                        private void changeFragment(Bundle bundle) {
                            Fragment fragment = new ProductListViewFragment();
                            fragment.setArguments(bundle);


                            FragmentTransaction transaction = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.popenter, R.anim.popexit);
                            transaction.addToBackStack(getClass().getSimpleName());
                            transaction.replace(R.id.toyListViewWraper, fragment);
                            transaction.commit();
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                        }
                    });


                    Log.d(TAG, "Element " + position + " clicked.");
                    mClickedPostion = position;
//
                }
            });
        }

    }

    private int getPastelRBG() {
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        final int Red = r.nextInt(120) + 100;
        int Green = r.nextInt(120) + 100;
        int Blue = r.nextInt(120) + 100;
        return Color.rgb(Red, Green, Blue);
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}
