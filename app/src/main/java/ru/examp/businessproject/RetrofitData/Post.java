package ru.examp.businessproject.RetrofitData;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {

    private double close_price_last_day;
    private String figi;
    private String last_date;
    private List<Double> list_prices_last_100_day;
    private String name;
    private int volume;

    public double getClosePriceLastDay() {
        return close_price_last_day;
    }

    public String getFigi() {
        return figi;
    }

    public String getLast_date() {
        return last_date;
    }

    public List<Double> getListPricesLast100Day() {
        return list_prices_last_100_day;
    }

    public String getName() {
        return name;
    }

    public int getVolume() {
        return volume;
    }

}