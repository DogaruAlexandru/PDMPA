package com.example.client.ui.produces;

import android.content.Context;
import android.icu.text.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.data.model.Product;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {
    private final static DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.MEDIUM);
    private final RecyclerViewInterface recyclerViewInterface;

    private Context context;
    private List<Product> items;

    public ProductsAdapter(Context context, List<Product> items, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.items = items;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.produce_container_layout, parent, false), recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.getName().setText(items.get(position).name());
        String storage = "Storage: " + items.get(position).container();
        holder.getContainer().setText(storage);
        String date = "Expires: " + DATE_FORMAT.format(items.get(position).expirationDate());
        holder.getExpirationDate().setText(date);
        String quantity = "Quantity: " + items.get(position).quantity();
        holder.getQuantity().setText(quantity);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, container, expirationDate, quantity;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            name = itemView.findViewById(R.id.produce_name);
            container = itemView.findViewById(R.id.container_name);
            expirationDate = itemView.findViewById(R.id.expiration_date);
            quantity = itemView.findViewById(R.id.quantity);

            itemView.setOnClickListener(v -> {
                if (recyclerViewInterface != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClick(position);
                    }
                }
            });
        }

        public TextView getName() {
            return name;
        }

        public TextView getContainer() {
            return container;
        }

        public TextView getExpirationDate() {
            return expirationDate;
        }

        public TextView getQuantity() {
            return quantity;
        }
    }
}

