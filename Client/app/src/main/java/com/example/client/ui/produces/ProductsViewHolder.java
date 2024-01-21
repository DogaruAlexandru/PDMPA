package com.example.client.ui.produces;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;

public class ProductsViewHolder extends RecyclerView.ViewHolder {
    private final TextView name, container, expirationDate, quantity;

    public ProductsViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.produce_name);
        container = itemView.findViewById(R.id.container_name);
        expirationDate = itemView.findViewById(R.id.expiration_date);
        quantity = itemView.findViewById(R.id.quantity);
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
