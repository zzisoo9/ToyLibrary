package com.zzisoo.toylibrary;

import android.content.Context;
import android.content.res.Configuration;

import com.zzisoo.toylibrary.vo.Toy;

import java.util.ArrayList;

/**
 * Created by yangjisoo on 15. 6. 18..
 */
public class Config {

    public static final int DEFAULT_SPAN_LANDSCAPE = 3;
    public static final int DEFAULT_SPAN_PORTRAIT = 1;


    public static final String HOST_SERVER_URL = "http://www.yicare.or.kr/";
    public static final String DATA_SREVER_URL = "http://toylib.cafe24app.com";
    public static final String URL_BOOKS_LIST = DATA_SREVER_URL + "/products";
    public static final String URL_DETAIL = DATA_SREVER_URL + "/productDetail?id=";
    public static final int TITLE_MAX_LINE_LANDSCAPE = 3;
    public static final int TITLE_MAX_LINE_PORTRAIT = 2;
    public static final boolean IS_HIDDENABLE_TOOLBAR = true;
    public static boolean IS_FAVORITE_VIEW = false ;

    public static int DisplayToolbarHeight = 0;
    public static int DisplayWidth = 0;
    public static int SpanPortrait = DEFAULT_SPAN_PORTRAIT;
    public static int SpanLandscape = DEFAULT_SPAN_LANDSCAPE;
    public static boolean IS_PUSH_DRAWER = true;
    public static ArrayList<Toy> backupDataSet;


    public static int getSpans(Context c) {
        if (isLandscape(c)) {
            return SpanLandscape;
        } else {
            return SpanPortrait;
        }
    }

    public static void setSpans(Context c, int span) {
        if (isLandscape(c)) {
            SpanLandscape = span;
        } else {
            SpanPortrait = span;
        }
    }

    public static boolean isLandscape(Context c) {
        Configuration newConfig = c.getResources().getConfiguration();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else {
            return false;

        }
    }
}
