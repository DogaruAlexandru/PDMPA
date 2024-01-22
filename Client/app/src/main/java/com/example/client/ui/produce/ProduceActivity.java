package com.example.client.ui.produce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.client.R;

import java.util.Objects;

public class ProduceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produce);

        String type = getIntent().getStringExtra("type");
        long productId = getIntent().getLongExtra("productId", -1);
        Fragment fragment;

        switch (Objects.requireNonNull(type)) {
            case "edit" -> {
                fragment = ProduceEditFragment.newInstance();

                Bundle args = new Bundle();
                args.putLong("productId", productId);
                fragment.setArguments(args);
                setTitle("Edit Produce");

            }
            case "add" -> {
                fragment = ProduceAddFragment.newInstance();
                setTitle("Add Produce");
            }
            default -> throw new IllegalArgumentException("Type does not exist");
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commitNow();
        }
    }
}