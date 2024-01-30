package com.example.client.ui.recipes.recipe;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.example.client.R;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        String type = getIntent().getStringExtra("type");
        Long recipeId = getIntent().getLongExtra("recipe_id", -1);
        Fragment fragment;

        switch (type) {
            case "view" -> {
                fragment = RecipeViewFragment.newInstance();

                Bundle args = new Bundle();
                args.putLong("recipe_id", recipeId);
                fragment.setArguments(args);
            }
            case "add" -> {
                fragment = RecipeAddFragment.newInstance();
            }
            default -> {
                return;
            }
        }
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.recipe, fragment).commitNow();
            }
    }
}