package com.example.client.ui.produces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.data.model.Product;
import com.example.client.databinding.FragmentProducesBinding;

import java.util.Comparator;
import java.util.List;

public class ProducesFragment extends Fragment implements RecyclerViewInterface {

    private FragmentProducesBinding binding;
    private ProducesViewModel producesViewModel;

    private List<Product> productList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        producesViewModel = new ViewModelProvider(this).get(ProducesViewModel.class);

        binding = FragmentProducesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bindFieldSpinnerOptions();
        bindOrderSpinnerOptions();

        bindProducesToView(root);

        spinnerValueChangeListener(binding.spinnerFieldSelector);
        spinnerValueChangeListener(binding.spinnerOrderSelector);

        return root;
    }

    private void spinnerValueChangeListener(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setAdaptorData(binding.getRoot(), binding.producesRv);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void bindFieldSpinnerOptions() {
        final Spinner spinnerFieldSelector = binding.spinnerFieldSelector;
        String[] fieldNames = producesViewModel.getFieldNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, fieldNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFieldSelector.setAdapter(adapter);
    }

    private void bindOrderSpinnerOptions() {
        final Spinner spinnerFieldSelector = binding.spinnerOrderSelector;
        String[] orders = new String[]{"Ascending", "Descending"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, orders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFieldSelector.setAdapter(adapter);
    }

    private void bindProducesToView(View root) {
        final RecyclerView recyclerView = binding.producesRv;

        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        setAdaptorData(root, recyclerView);
    }

    private void setAdaptorData(View root, RecyclerView recyclerView) {
        producesViewModel.getProduces().observe(getViewLifecycleOwner(), produces -> {
            sortProducts(produces);
            productList = produces;
            ProductsAdapter adaptor = new ProductsAdapter(root.getContext(), produces, this);
            recyclerView.setAdapter(adaptor);
        });
    }

    private void sortProducts(List<Product> produces) {
        String field = binding.spinnerFieldSelector.getSelectedItem().toString();
        String order = binding.spinnerOrderSelector.getSelectedItem().toString();

        Comparator<Product> comparator = getComparatorForField(field);

        if ("Descending".equals(order)) {
            comparator = comparator.reversed();
        }

        produces.sort(comparator);
    }

    private Comparator<Product> getComparatorForField(String field) {
        return switch (field) {
            case "Name" -> Comparator.comparing(Product::name);
            case "Container" -> Comparator.comparing(Product::container);
            case "Expiration Date" -> Comparator.comparing(Product::expirationDate);
            case "Quantity" -> Comparator.comparing(Product::quantity);
            default -> throw new IllegalArgumentException("Invalid field: " + field);
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int pos) {
        Toast.makeText(requireContext(), "Clicked on: " + productList.get(pos).name(), Toast.LENGTH_SHORT).show();

    }
}