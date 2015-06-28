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

package com.zzisoo.toylibrary.activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.zzisoo.toylibrary.Config;
import com.zzisoo.toylibrary.R;
import com.zzisoo.toylibrary.utils.Typefaces;

import me.grantland.widget.AutofitHelper;

public abstract class BaseActivity<S extends Scrollable> extends AppCompatActivity {

    public Toolbar mTbHiddenable;
    public S mScrollableChildView;

    public int getShortWidth(){
        Context c = this.getApplicationContext();
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int x = display.getWidth();
        int y = display.getHeight();
        int nwidth = x;
        if (Config.isLandscape(c)) {
            nwidth = x < y ? x : y;
        } else {
            nwidth = x < y ? x : y;
        }


        Rect rectangle= new Rect();
        Window window= getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight= rectangle.top;
        int contentViewTop=
                window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight= contentViewTop - statusBarHeight;

        return  nwidth;
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

    protected void setToolBar(String strTitle, String strFontFileNameAtAssetsFolder_NULLABLE) {
        if(strFontFileNameAtAssetsFolder_NULLABLE == null){
            strFontFileNameAtAssetsFolder_NULLABLE = "Satisfy-Regular.ttf";
        }
        mTbHiddenable = (Toolbar) findViewById(R.id.tb_activity_mainlist_hidden_toolbar);
        setSupportActionBar(mTbHiddenable);
        TextView tvActivityMainlistHiddenToolbarTitle = (TextView) mTbHiddenable.findViewById(R.id.tv_activity_mainlist_hidden_toolbar_title);

        TextView tvActivityMainlistHiddenToolbarFavorite = (TextView) mTbHiddenable.findViewById(R.id.tv_activity_mainlist_hidden_toolbar_favorite);
        tvActivityMainlistHiddenToolbarTitle.setText(strTitle);
        AutofitHelper.create(tvActivityMainlistHiddenToolbarTitle);
        AutofitHelper.create(tvActivityMainlistHiddenToolbarFavorite);

        tvActivityMainlistHiddenToolbarTitle.setTypeface(Typefaces.get(this, strFontFileNameAtAssetsFolder_NULLABLE));
        tvActivityMainlistHiddenToolbarFavorite.setTypeface(Typefaces.get(this, strFontFileNameAtAssetsFolder_NULLABLE));

    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }

    public boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mTbHiddenable) == 0;
    }

    public boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mTbHiddenable) == -mTbHiddenable.getHeight();
    }

    public void showToolbar() {
        moveToolbar(0);
    }

    public void hideToolbar() {
        moveToolbar(-mTbHiddenable.getHeight());
    }

    public void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(mTbHiddenable) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mTbHiddenable), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(mTbHiddenable, translationY);
                ViewHelper.setTranslationY((View) mScrollableChildView, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ((View) mScrollableChildView).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                ((View) mScrollableChildView).requestLayout();
            }
        });
        animator.start();
    }
}
