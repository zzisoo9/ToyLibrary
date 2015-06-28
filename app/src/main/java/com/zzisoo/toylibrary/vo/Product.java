package com.zzisoo.toylibrary.vo;

/**
 * Created by yangjisoo on 15. 6. 20..
 */
public class Product {

    /*******************************
     [{"pid":2,"title":"휠리버그-밀크카우",
     "image":"../upload/product2/150515_1486.jpg",
     "category":"신체",
     "goods":"본체1개",
     "recommendAge":"12개월 이상",
     "manufacturer":"휠리버그",
     "description":"\r\n\t\t\t\t\t휠리버그의 섬세한 디자인은 아이들로 하여금 친구로 여기기에 충분합니다. 아이들 누구든 한번보면 사랑에 빠질 수 밖에 없는 앙증맞은 디자인 입니다. 휠리버그가 인기 있는 가장 큰 이유 입니다.\t\t\t\t"
     }]    ******************************/
    String pid;
    String image;
    String category;
    String goods;
    String recommendAge;
    String manufacturer;
    String description;

    public String getPid() {
        return pid;
    }

    public String getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public String getGoods() {
        return goods;
    }

    public String getRecommendAge() {
        return recommendAge;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getDescription() {
        return description.replace("\r\n","").replace("\t","");
    }

}
