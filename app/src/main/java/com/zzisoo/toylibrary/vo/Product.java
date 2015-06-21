package com.zzisoo.toylibrary.vo;

/**
 * Created by yangjisoo on 15. 6. 20..
 */
public class Product {
    /*******************************
    {
        "pid": "2201355",
            "isLent": "대여가능"
    },{},{}...
    ******************************/
    String pid;

    public String getIsLent() {
        return isLent;
    }

    String isLent;

    public String getPid() {
        return pid;
    }
}
