package com.example.client.ui.recipes.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.client.R;
import com.example.client.data.model.Recipe;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RecipeAddFragment extends Fragment {

    RecipeAddViewModel recipeAddViewModel;
    public static Fragment newInstance() {
        return new RecipeAddFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_recipe_add, container, false);
        setButtonsAction(rootview);
        return rootview;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeAddViewModel = new ViewModelProvider(this).get(RecipeAddViewModel.class);
    }

    private void setButtonsAction(View rootView) {
        Button btnAdd = rootView.findViewById(R.id.recipe_add_button);
        Button btnBack = rootView.findViewById(R.id.recipe_back_button);

        btnAdd.setOnClickListener(view -> {
            if (validateRecipe(rootView)) {
                recipeAddViewModel.addRecipe(getValues(rootView));
                Toast.makeText(getActivity(), getResources().getString(R.string.recipe_success), Toast.LENGTH_LONG).show();
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
            else Toast.makeText(getActivity(), getResources().getString(R.string.recipe_fail), Toast.LENGTH_LONG).show();
        });

        btnBack.setOnClickListener(view -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private Recipe getValues(View rootView) {
        return new Recipe(
                null,
                getFieldString(rootView, R.id.recipe_title_edit),
                getListFromFieldString(rootView, R.id.recipe_ingredients_list),
                getFieldString(rootView, R.id.recipe_description));
    }

    private String getFieldString(View rootView, int id) {
        return Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id)).getEditText())
                .getText().toString();
    }

    private List<String> getListFromFieldString(View rootView, int id){
        String string = Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id)).getEditText()).getText().toString();
        return Arrays.asList(string.split("; "));
    }

    private boolean validateRecipe(View rootView) {
        Recipe potentialRecipe = getValues(rootView);
        if (potentialRecipe.name().isEmpty())
            return false;
        if (potentialRecipe.ingredients().isEmpty())
            return false;
        if(potentialRecipe.description().isEmpty())
            return false;
        return true;
    }
}
