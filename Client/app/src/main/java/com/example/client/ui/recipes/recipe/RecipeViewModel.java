package com.example.client.ui.recipes.recipe;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.client.R;
import com.example.client.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeViewModel extends ViewModel{

    private long recipeId;

    private Recipe recipe;

    private MutableLiveData<Recipe> recipeMutableLiveData;

    private RecipeViewModel(){
        setRecipe();
    }

    private void setRecipe() {
        getFromDB();

        recipeMutableLiveData = new MutableLiveData<>();
        recipeMutableLiveData.setValue(recipe);
    }

    private void getFromDB() {
        //TODO: DB Connection

        getRecipeInfo();
    }

    private void getRecipeInfo() {
        List<String> ingredients = new ArrayList<String>();
        ingredients.add("ing1");
        ingredients.add("ing2");
        recipe = new Recipe(
                1L,
                "Test Recipe",
                ingredients,
                "This is the recipe description"
        );
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public LiveData<Recipe> getRecipeMutableLiveData() {
        return recipeMutableLiveData;
    }
}
