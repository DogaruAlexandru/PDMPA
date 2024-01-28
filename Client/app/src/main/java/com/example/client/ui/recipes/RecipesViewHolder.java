package com.example.client.ui.recipes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;

public class RecipesViewHolder extends RecyclerView.ViewHolder {

    TextView recipeName, ingredient1, ingredient2, ingredient3;
    public RecipesViewHolder(@NonNull View itemView) {
        super(itemView);
        recipeName = itemView.findViewById(R.id.recipeitemname);
        ingredient1 = itemView.findViewById(R.id.recipeingredient1);
        ingredient2 = itemView.findViewById(R.id.recipeingredient2);
        ingredient3 = itemView.findViewById(R.id.recipeingredient3);
    }
}
