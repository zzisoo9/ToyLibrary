package com.zzisoo.toylibrary.vo;

/**
 * Created by yangjisoo on 15. 6. 20..
 *
 */
public class Toy {
    /******************************
     "title": "래프앤런 어소트 진공청소기",
     "link": "2929",
     "image": "../upload/product2/150515_1355.jpg",
     "category": "신체",
     "goods": "본체1개",
     "recommendAge": "12개월 이상",
     "manufacturer": "피셔프라이스",
     "description": "\r\n\t\t\t\t\t30여개의 다양한 노래와, 문구들로 학습능력을 키울 수 있습니다. 밀고 당기고 버튼을 눌러보고 배우면서 다양한 놀이를 할 수 있습니다.\t\t\t\t",
     "products": []
     ******************************/
    String title;

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
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
        return description.replace("\r","").replace("\n","").replace("\t","").replace("  ","");
    }

    public Product[] getProducts() {
        return products;
    }

    String link;
    String image;
    String category;
    String goods;
    String recommendAge;
    String manufacturer;
    String description;
    Product[] products;
}
