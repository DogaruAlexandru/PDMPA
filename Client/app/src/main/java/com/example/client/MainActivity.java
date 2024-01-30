package com.example.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.client.ui.containers.container.ContainerActivity;
import com.example.client.ui.produces.produce.ProduceActivity;
import com.example.client.ui.recipes.recipe.RecipeActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.client.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;

    private boolean clicked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        setFABListeners();
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_produces, R.id.nav_containers, R.id.nav_recipes)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void setFABListeners() {
        binding.appBarMain.fab.setOnClickListener(view -> {
            setVisibility();
            setAnimation();
            clicked = !clicked;
        });

        binding.appBarMain.fabProduct.setOnClickListener(view -> {
            Intent intent = new Intent(this, ProduceActivity.class);
            intent.putExtra("type", "add");
            startActivity(intent);
        });

        binding.appBarMain.fabReceipt.setOnClickListener(view -> {
            Snackbar.make(view, "Receipt", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

        binding.appBarMain.fabContainer.setOnClickListener(view -> {
            Intent intent = new Intent(this, ContainerActivity.class);
            intent.putExtra("type", "add");
            startActivity(intent);
        });

        binding.appBarMain.fabRecipes.setOnClickListener(view -> {
            Intent intent = new Intent(this, RecipeActivity.class);
        });
    }

    private void setAnimation() {
        if (!clicked) {
            binding.appBarMain.fabProduct.startAnimation(fromBottom);
            binding.appBarMain.fabReceipt.startAnimation(fromBottom);
            binding.appBarMain.fabContainer.startAnimation(fromBottom);
            binding.appBarMain.fabRecipes.startAnimation(fromBottom);
            binding.appBarMain.fab.startAnimation(rotateClose);
        } else {
            binding.appBarMain.fabProduct.startAnimation(toBottom);
            binding.appBarMain.fabReceipt.startAnimation(toBottom);
            binding.appBarMain.fabContainer.startAnimation(toBottom);
            binding.appBarMain.fabRecipes.startAnimation(toBottom);
            binding.appBarMain.fab.startAnimation(rotateOpen);
        }
    }

    private void setVisibility() {
        if (!clicked) {
            binding.appBarMain.fabProduct.setVisibility(View.VISIBLE);
            binding.appBarMain.fabReceipt.setVisibility(View.VISIBLE);
            binding.appBarMain.fabContainer.setVisibility(View.VISIBLE);
            binding.appBarMain.fabRecipes.setVisibility(View.VISIBLE);
        } else {
            binding.appBarMain.fabProduct.setVisibility(View.INVISIBLE);
            binding.appBarMain.fabReceipt.setVisibility(View.INVISIBLE);
            binding.appBarMain.fabContainer.setVisibility(View.INVISIBLE);
            binding.appBarMain.fabRecipes.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}