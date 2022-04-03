package ru.examp.businessproject;

public class Stock {

    private String name; // название
    private String price;  // цена
    private int imgResource; // ресурс изображения
    private String figi; // фиджи

    public Stock(String name, String price, int image, String figi){

        this.name=name;
        this.price=price;
        this.imgResource=image;
        this.figi=figi;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImgResource() {
        return this.imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }
    public String getFigi() {
        return this.figi;
    }

    public void setFigi(String figi) {
        this.figi = figi;
    }
}