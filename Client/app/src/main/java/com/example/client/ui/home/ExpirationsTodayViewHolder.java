package com.example.client.ui.home;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;

public class ExpirationsTodayViewHolder extends RecyclerView.ViewHolder {

    private final TextView produceName, containerName;

    public ExpirationsTodayViewHolder(@NonNull View itemView) {
        super(itemView);
        produceName = itemView.findViewById(R.id.produce_name);
        containerName = itemView.findViewById(R.id.container_name);
    }

    public TextView getProduceName() {
        return produceName;
    }

    public TextView getContainerName() {
        return containerName;
    }
}
