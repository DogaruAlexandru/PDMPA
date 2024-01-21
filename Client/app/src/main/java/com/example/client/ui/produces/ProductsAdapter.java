package com.example.client.ui.produces;

import android.content.Context;
import android.icu.text.DateFormat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.data.model.Product;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsViewHolder> {
    private final static DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.MEDIUM);

    private Context context;
    private List<Product> items;

    public ProductsAdapter(Context context, List<Product> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductsViewHolder(LayoutInflater.from(context).
                inflate(R.layout.produce_container_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        holder.getName().setText(items.get(position).getName());
        String storage = "Storage: " + items.get(position).getContainer();
        holder.getContainer().setText(storage);
        String date = "Expires: " + DATE_FORMAT.format(items.get(position).getExpirationDate());
        holder.getExpirationDate().setText(date);
        String quantity = "Quantity: " + items.get(position).getQuantity();
        holder.getQuantity().setText(quantity);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

