package com.example.client.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.R;
import com.example.client.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //todo add expiration dates in calendar
        bindTextToView(homeViewModel);

        bindItemsToView(homeViewModel, root);

        return root;
    }

    private void bindTextToView(HomeViewModel homeViewModel) {
        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
    }

    private void bindItemsToView(HomeViewModel homeViewModel, View root) {
        RecyclerView recyclerView = binding.comingExpirationsRecyclerView;
//        RecyclerView recyclerView = root.findViewById(R.id.coming_expirations_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        homeViewModel.getItems().observe(getViewLifecycleOwner(), expirationTodayModels -> {
            recyclerView.setAdapter(new ExpirationsTodayAdaptor(root.getContext(), expirationTodayModels));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}