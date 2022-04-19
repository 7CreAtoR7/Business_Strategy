package ru.examp.businessproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Stock> stocks;

    StockAdapter(Context context, List<Stock> stocks) {
        this.stocks = stocks;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StockAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Stock stock = stocks.get(position);
        holder.imgView.setImageResource(stock.getImgResource());
        holder.nameView.setText(stock.getName());
        holder.priceView.setText(stock.getPrice());
        holder.figiView.setText(stock.getFigi());
        holder.itemView.setOnClickListener(v -> {
            //Toast.makeText(v.getContext(), "Нажатие " + position, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgView;
        final TextView nameView, priceView, figiView;

        ViewHolder(View view) {
            super(view);
            imgView = view.findViewById(R.id.photo);
            nameView = view.findViewById(R.id.name);
            priceView = view.findViewById(R.id.price);
            figiView = view.findViewById(R.id.figi);
        }
    }
}