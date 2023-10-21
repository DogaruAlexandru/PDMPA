package com.example.client.ui.produces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.client.databinding.FragmentProducesBinding;

public class ProducesFragment extends Fragment {

    private FragmentProducesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProducesViewModel producesViewModel =
                new ViewModelProvider(this).get(ProducesViewModel.class);

        binding = FragmentProducesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProduces;
        producesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}