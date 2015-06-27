package com.zzisoo.toylibrary;

import android.content.res.Configuration;

/**
 * Created by yangjisoo on 15. 6. 18..
 */
public class Config {
    public static int DISPLAY_WIDTH = 0;


    public static boolean TOGGLE_FULL_SCREEN = false;
    public static boolean TOGGLE_FULL_SCREEN_AUTO_HIDE = false;
    public static boolean TOGGLE_ON_CLICK = true;
    public static int AUTO_HIDE_DELAY_MILLIS = 3000;




    public static final String HOST_SERVER_URL = "http://www.yicare.or.kr/";
    public static final String DATA_SREVER_URL = "http://toylib.cafe24app.com";
    public static final String URL_BOOKS_LIST = DATA_SREVER_URL + "/products";
    public static final String URL_DETAIL =  DATA_SREVER_URL + "/productDetail?id=";

    public static int getSpans(Configuration newConfig) {
        int cols = 1;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            cols = 3;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            cols = 1;
        }
        return cols;
    }
}
