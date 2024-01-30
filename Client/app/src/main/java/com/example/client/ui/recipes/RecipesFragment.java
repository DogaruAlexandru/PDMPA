package com.example.client.ui.recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.data.RecyclerViewInterface;
import com.example.client.data.model.Recipe;
import com.example.client.databinding.FragmentContainersBinding;
import com.example.client.databinding.FragmentRecipesBinding;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;

public class RecipesFragment extends Fragment implements RecyclerViewInterface {

    private FragmentRecipesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView recyclerView = root.findViewById(R.id.recipesview);

        List<Recipe> recipes= new ArrayList<Recipe>();
        List<String> ingredients = new ArrayList<String>();
        List<String> ingredients2 = new ArrayList<String>();
        ingredients.add("ing1");
        ingredients.add("ing2");
        ingredients2.add("ingredient1");
        ingredients2.add("ingredient2");
        recipes.add(new Recipe(2L, "Tort", ingredients, "Tort"));
        recipes.add(new Recipe(2L, "Salam de biscuiti", ingredients2, ""));
        recipes.add(new Recipe(2L, "Supa", ingredients, ""));
        ingredients.add("new");
        recipes.add(new Recipe(2L, "Omleta", ingredients2, ""));

        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setAdapter(new RecipesAdapter(getContext(), recipes));
        return root;
    }

    @Override
    public void onItemClick(int pos) {

    }
}
