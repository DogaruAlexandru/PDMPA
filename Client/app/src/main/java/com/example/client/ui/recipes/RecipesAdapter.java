package com.example.client.ui.recipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.data.RecyclerViewInterface;
import com.example.client.data.model.Recipe;
import com.example.client.ui.containers.ContainersAdapter;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<Recipe> recipes;

    public RecipesAdapter(Context context, List<Recipe> recipes, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.recipes = recipes;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipesViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe_item_view, parent, false), recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        holder.getRecipeName().setText(recipes.get(position).name());
        List<String> ingredients = recipes.get(position).ingredients();
        if (ingredients.size()>=3){
            holder.getIngredient1().setText(ingredients.get(0));
            holder.getIngredient2().setText(ingredients.get(1));
            holder.getIngredient3().setText(ingredients.get(2));
        } else if (ingredients.size() == 2) {
            holder.getIngredient1().setText(ingredients.get(0));
            holder.getIngredient2().setText(ingredients.get(1));
            holder.getIngredient3().setText(null);
        } else if (ingredients.size() == 1) {
            holder.getIngredient1().setText(ingredients.get(0));
            holder.getIngredient2().setText(null);
            holder.getIngredient3().setText(null);
        } else {
            holder.getIngredient1().setText(null);
            holder.getIngredient2().setText(null);
            holder.getIngredient3().setText(null);
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
        }
    }
}
