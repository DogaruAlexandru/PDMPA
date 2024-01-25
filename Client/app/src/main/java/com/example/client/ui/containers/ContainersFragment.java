package com.example.client.ui.containers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.client.databinding.FragmentContainersBinding;

public class ContainersFragment extends Fragment {

    private FragmentContainersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ContainersViewModel containersViewModel =
                new ViewModelProvider(this).get(ContainersViewModel.class);

        binding = FragmentContainersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textContainers;
        containersViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;//todo
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}