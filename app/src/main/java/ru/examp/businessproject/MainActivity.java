package ru.examp.businessproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import ru.examp.businessproject.RetrofitData.Post;

public class MainActivity extends AppCompatActivity {

    ArrayList<Stock> stocks = new ArrayList<>();
    Calendar dateAndTime = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener d;
    private long delta; //разница в днях

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        // начальная инициализация списка
        //setInitialData();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // создаем адаптер
        StockAdapter adapter = new StockAdapter(this, stocks);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        // список информации каждой акции
        List<Post> posts = (List<Post>) intent.getSerializableExtra("POSTS");

        // акция: [кол-во, стоимость портфеля, сумма затрат на покупку этой акции]
        Map<String, List<Double>> hashmap = new HashMap<>();
        // кол-во акций в воображаемом портфеле
        // стоимость виртуального портфеля на данный момент
        // сумма затрат на покупку этой акции


//        for (Post post : posts) {
//            // кол-во акций в воображаемом портфеле
//            // стоимость виртуального портфеля на данный момент
//            // сумма затрат на покупку этой акции
//            List<Double> currentValues = Arrays.asList(0.0, 0.0, 0.0);
//            hashmap.put(post.getName(), currentValues);
//        }


        for (Post post : posts) {
            List<Double> currentValues = Arrays.asList(0.0, 0.0, 0.0);
            hashmap.put(post.getName(), currentValues);

            double close_price_last_day = post.getClosePriceLastDay(); // цена закрытия акции на последних торгах
            String figi = post.getFigi(); // название id figi
            //String last_date = post.getLast_date(); // последний день торгов
            List<Double> list_prices_last_100_day = post.getListPricesLast100Day(); // массив цен закрытия за последние 100 торговых дней
            String name = post.getName(); // название акции
            //Integer volume = post.getVolume(); // кол-во сделок
            //System.out.println("цена закрытия на последних торгах = " + close_price_last_day + "\n" + "название id figi = " + figi + "\n" + "последний день торгов = " + last_date+ "\n" + "название акции = " + name + "\n" + "кол-во сделок = " + volume + "\n");

            try {
                // по умолчанию рассматривается 30 последних товрговых дней
                int days = 10;
                while (days > 5) {
                    // проверка на первое условие:
                    // если цена закрытия за день снижалась хотя бы два дня подряд, а потом
                    // сменилась днем роста, то еще на следующий день покупаем 1 акцию
                    // нужно получить цены закрытия за последние 4 торговых дня
                    double firstPrice = list_prices_last_100_day.get(list_prices_last_100_day.size() - days);
                    double secondPrice = list_prices_last_100_day.get(list_prices_last_100_day.size() - days + 1);
                    double thirdPrice = list_prices_last_100_day.get(list_prices_last_100_day.size() - days + 2);
                    double fourthPrice = list_prices_last_100_day.get(list_prices_last_100_day.size() - days + 3);
                    double isGoodPriceToBuy = list_prices_last_100_day.get(list_prices_last_100_day.size() - days + 4);
                    double isGoodPriceToBuyLast = list_prices_last_100_day.get(list_prices_last_100_day.size() - days + 5);
                    if (firstPrice > secondPrice && secondPrice > thirdPrice && thirdPrice < fourthPrice) {
                        // закупаем 1 акцию на следующий день
                        List<Double> listCurrentStockInfo = hashmap.get(name);
                        if (listCurrentStockInfo != null) {
                            double countStock = listCurrentStockInfo.get(0) + 1; // кол-во акций + 1
                            double sumRubStocks = listCurrentStockInfo.get(1) + isGoodPriceToBuy; // сумма акций +
                            double sumToPay = listCurrentStockInfo.get(2) + isGoodPriceToBuy; // сумма затрат +
                            List<Double> NewData = Arrays.asList(countStock, sumRubStocks, sumToPay);
                            System.out.println("Добавили в словарь акцию " + name + ": " + NewData);
                            hashmap.put(name, NewData);
                            days -= 1;
                        } else {
                            days -= 1;
                        }

                        // после покупки: если акция (цена закрытия за день) растет три и более дней
                        // подряд и потом рост сменяется дневным снижением более чем на 2%, то еще на
                        // следующий день продаем 1 акцию
                    } else if (firstPrice < secondPrice && secondPrice < thirdPrice && thirdPrice < fourthPrice && isGoodPriceToBuy < fourthPrice * 0.98) {
                        // продаем 1 акцию на следующий день (isGoodPriceToBuyLast)
                        List<Double> listCurrentStockInfo = hashmap.get(name);
                        if (listCurrentStockInfo != null && listCurrentStockInfo.get(0) > 0) { //проверка, что акция была куплена ранее
                            double countStock = listCurrentStockInfo.get(0) - 1; // кол-во данной акций - 1 в портфеле
                            double sumRubStocks = listCurrentStockInfo.get(1) - isGoodPriceToBuyLast; // продажа по последней цене закрытия торгов на те дни
                            double sumToPay = listCurrentStockInfo.get(2) - isGoodPriceToBuyLast; // сумма затрат -,т.к. продали, фиксируем прибыль/убыток
                            List<Double> NewData = Arrays.asList(countStock, sumRubStocks, sumToPay);
                            System.out.println("Добавили в словарь акцию " + name + ": " + NewData);
                            hashmap.put(name, NewData);
                            days -= 1;
                        } else {
                            days -= 1;
                        }
                    } else {
                        days -= 1;
                    }
                }

                // кол-во акций в воображаемом портфеле
                // стоимость виртуального портфеля на данный момент
                // сумма затрат на покупку этой акции

                double countBoughtStocksThisType = Objects.requireNonNull(hashmap.get(name)).get(0);
                //double sumStocks = hashmap.get(name).get(1);
                double sumSales = Objects.requireNonNull(hashmap.get(name)).get(2);
                if (countBoughtStocksThisType * close_price_last_day > sumSales) {
                    System.out.println("Условие выгодно");
                    stocks.add(new Stock(name, close_price_last_day + " руб", R.drawable.ic_baseline_trending_up_24, figi));
                } else {
                    System.out.println("Условие не выгодно");
                    stocks.add(new Stock(name, close_price_last_day + " руб", R.drawable.ic_baseline_trending_down_24, figi));
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + Arrays.toString(e.getStackTrace()));
            }
        }

        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);

        findViewById(R.id.set_date).setOnClickListener(v -> {
            new DatePickerDialog(MainActivity.this, d,
                    dateAndTime.get(Calendar.YEAR),
                    dateAndTime.get(Calendar.MONTH),
                    dateAndTime.get(Calendar.DAY_OF_MONTH)).show();
            Toast.makeText(MainActivity.this, "Выберите прошедшую дату в пределах 100 дней", Toast.LENGTH_LONG).show();
        });

        // установка обработчика выбора даты
        d = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateAndTime.set(Calendar.HOUR_OF_DAY, 0);
                dateAndTime.set(Calendar.MINUTE, 0);
                dateAndTime.set(Calendar.SECOND, 0);
                dateAndTime.set(Calendar.MILLISECOND, 0);
                getDaysDelta();
            }

            private void getDaysDelta() {
                Calendar dateAndTime1 = Calendar.getInstance();
                dateAndTime1.set(Calendar.HOUR_OF_DAY, 0);
                dateAndTime1.set(Calendar.MINUTE, 0);
                dateAndTime1.set(Calendar.SECOND, 0);
                dateAndTime1.set(Calendar.MILLISECOND, 0);
                delta = dateAndTime1.getTimeInMillis() - dateAndTime.getTimeInMillis();
                int days = (int) TimeUnit.MILLISECONDS.toDays(delta);
                Toast.makeText(MainActivity.this, "Разница (дни) " + days, Toast.LENGTH_LONG).show();
            }
        };
    }
}