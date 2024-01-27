package com.example.client.ui.containers.container;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.client.R;

import java.util.Objects;

public class ContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        String type = getIntent().getStringExtra("type");
        long containerId = getIntent().getLongExtra("containerId", -1);
        Fragment fragment;

        switch (Objects.requireNonNull(type)) {
            case "edit" -> {
                fragment = ContainerEditFragment.newInstance();

                Bundle args = new Bundle();
                args.putLong("containerId", containerId);
                fragment.setArguments(args);
                setTitle("Edit Container");
            }
            case "add" -> {
                fragment = ContainerAddFragment.newInstance();
                setTitle("Add Container");
            }
            default -> throw new IllegalArgumentException("Type does not exist");
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commitNow();
        }
    }
}