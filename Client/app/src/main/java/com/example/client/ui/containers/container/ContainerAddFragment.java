package com.example.client.ui.containers.container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.client.R;

public class ContainerAddFragment extends Fragment {

    public static ContainerAddFragment newInstance() {
        return new ContainerAddFragment();
    }

    private ContainerAddViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ContainerAddViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_container_add, container, false);

        Button btnEdit = rootView.findViewById(R.id.btnAdd);
        Button btnBack = rootView.findViewById(R.id.btnBack);

        btnEdit.setOnClickListener(view -> {
        });

        btnBack.setOnClickListener(view -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        return rootView;
    }

}