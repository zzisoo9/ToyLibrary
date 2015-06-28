package com.zzisoo.toylibrary.vo;

/**
 * Created by yangjisoo on 15. 6. 20..
 *
 */
public class Toy {
    /******************************
     [ {"pid":1,"title":"휠리버그-피그","image":"../upload/product2/150515_1495.jpg"},
     {"pid":2,"title":"휠리버그-밀크카우","image":"../upload/product2/150515_1486.jpg"},
     {"pid":3,"title":"휠리버그-비","image":"../upload/product2/150515_1489.jpg"}, ...]
     ******************************/
    public int getPid() {
        return pid;
    }public String getStrPid() {
        return String.valueOf(pid);
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    int pid;
    String title;
    String image;
}
