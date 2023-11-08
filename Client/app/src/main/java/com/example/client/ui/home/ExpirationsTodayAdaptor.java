package com.example.client.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;

import java.util.List;

public class ExpirationsTodayAdaptor extends RecyclerView.Adapter<ExpirationsTodayViewHolder> {

    Context context;
    List<ProductModel> items;

    public ExpirationsTodayAdaptor(Context context, List<ProductModel> list) {
        this.context = context;
        this.items = list;
    }

    @NonNull
    @Override
    public ExpirationsTodayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExpirationsTodayViewHolder(LayoutInflater.from(context).inflate(
                R.layout.expirations_today_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExpirationsTodayViewHolder holder, int position) {
        holder.getProduceName().setText(items.get(position).getProduceName());
        holder.getContainerName().setText(items.get(position).getContainerName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
