package com.example.client.ui.recipes.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.client.R;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeViewFragment extends Fragment {

    private long recipeId;
    private RecipeViewModel recipeViewModel;
    public static Fragment newInstance() {
        return new RecipeViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_item_view, container, false);

        assert getArguments() != null;
        recipeId = getArguments().getLong("recipe_id", -1);
        recipeViewModel.setRecipeId(recipeId);

        setValues(rootView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
    }

    private void setValues(View rootView) {
        recipeViewModel.getRecipeMutableLiveData().observe(getViewLifecycleOwner(), recipe -> {
            setViewString(rootView, R.id.recipe_title, recipe.name());
            setViewString(rootView, R.id.recipe_description, recipe.description());
            setIngredientList(rootView, R.id.recipe_ingredients_list, recipe.ingredients());
        });
    }

    private void setViewString(View rootView, int itemId, String string) {
        ((TextView)rootView.findViewById(itemId)).setText(string);
    }

    private void setIngredientList(View rootView, int itemId, List<String> list){
        String ingredientString = list.stream()
                .collect(Collectors.joining("; ", "", ""));
        ((TextView)rootView.findViewById(itemId)).setText(ingredientString);
    }
}
