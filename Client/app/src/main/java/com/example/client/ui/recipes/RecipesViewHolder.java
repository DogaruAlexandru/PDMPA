package com.example.client.ui.recipes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.data.RecyclerViewInterface;

public class RecipesViewHolder extends RecyclerView.ViewHolder {

    private TextView recipeName, ingredient1, ingredient2, ingredient3;
    public RecipesViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
        super(itemView);
        recipeName = itemView.findViewById(R.id.recipeitemname);
        ingredient1 = itemView.findViewById(R.id.recipeingredient1);
        ingredient2 = itemView.findViewById(R.id.recipeingredient2);
        ingredient3 = itemView.findViewById(R.id.recipeingredient3);

        itemView.setOnClickListener(v -> {
            if (recyclerViewInterface != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    recyclerViewInterface.onItemClick(position);
                }
            }
        });
    }

    public TextView getRecipeName() {
        return recipeName;
    }

    public TextView getIngredient1() {
        return ingredient1;
    }

    public TextView getIngredient2() {
        return ingredient2;
    }

    public TextView getIngredient3() {
        return ingredient3;
    }
}
