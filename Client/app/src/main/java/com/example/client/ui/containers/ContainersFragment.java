package com.example.client.ui.containers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.data.RecyclerViewInterface;
import com.example.client.data.model.Container;
import com.example.client.databinding.FragmentContainersBinding;
import com.example.client.ui.containers.container.ContainerActivity;

import java.util.List;

public class ContainersFragment extends Fragment implements RecyclerViewInterface {

    private FragmentContainersBinding binding;
    private ContainersViewModel containersViewModel;
    private List<Container> containerList;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        containersViewModel = new ViewModelProvider(this).get(ContainersViewModel.class);

        binding = FragmentContainersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bindContainersToView(root);

        return root;
    }

    private void bindContainersToView(View root) {
        final RecyclerView recyclerView = binding.containersRv;

        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        setAdaptorData(root, recyclerView);
    }

    private void setAdaptorData(View root, RecyclerView recyclerView) {
        containersViewModel.getContainers().observe(getViewLifecycleOwner(), containers -> {
            containerList = containers;
            ContainersAdapter adaptor = new ContainersAdapter(root.getContext(), containers, this);
            recyclerView.setAdapter(adaptor);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        containersViewModel.setData();
        setAdaptorData(binding.getRoot(), binding.containersRv);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int pos) {
        Container clickedContainer = containerList.get(pos);
        Intent intent = new Intent(requireContext(), ContainerActivity.class);
        intent.putExtra("containerId", clickedContainer.id());
        intent.putExtra("type", "edit");
        startActivity(intent);
    }
}