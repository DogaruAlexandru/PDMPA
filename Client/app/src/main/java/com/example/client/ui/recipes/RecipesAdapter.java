package com.example.client.ui.recipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.data.model.Recipe;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesViewHolder> {

    Context context;
    List<Recipe> recipes;

    public RecipesAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }
    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipesViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        holder.recipeName.setText(recipes.get(position).name());
        List<String> ingredients = recipes.get(position).ingredients();
        if (ingredients.size()>=3){
            holder.ingredient1.setText(ingredients.get(0));
            holder.ingredient2.setText(ingredients.get(1));
            holder.ingredient3.setText(ingredients.get(2));
        } else if (ingredients.size() == 2) {
            holder.ingredient1.setText(ingredients.get(0));
            holder.ingredient2.setText(ingredients.get(1));
            holder.ingredient3.setText(null);
        } else if (ingredients.size() == 1) {
            holder.ingredient1.setText(ingredients.get(0));
            holder.ingredient2.setText(null);
            holder.ingredient3.setText(null);
        } else {
            holder.ingredient1.setText(null);
            holder.ingredient2.setText(null);
            holder.ingredient3.setText(null);
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
