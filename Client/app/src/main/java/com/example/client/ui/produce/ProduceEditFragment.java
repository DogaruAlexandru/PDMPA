package com.example.client.ui.produce;

import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.icu.text.DateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.client.R;
import com.example.client.data.model.ProductFull;
import com.example.client.ui.produces.ProductsAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class ProduceEditFragment extends Fragment {

    public static ProduceEditFragment newInstance() {
        return new ProduceEditFragment();
    }

    private ProduceEditViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProduceEditViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_produce_edit, container, false);

        assert getArguments() != null;
        mViewModel.setProductId(getArguments().getLong("productId", -1));

        setButtonsAction(rootView);
        setProductContainerValues(rootView);

        setFieldsValue(rootView);

        return rootView;
    }

    private void setButtonsAction(View rootView) {
        Button btnEdit = rootView.findViewById(R.id.btnEdit);
        Button btnBack = rootView.findViewById(R.id.btnBack);
        Button btnRemove = rootView.findViewById(R.id.btnRemove);

        btnEdit.setOnClickListener(view -> {
            mViewModel.updateProduct();
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        btnBack.setOnClickListener(view -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        btnRemove.setOnClickListener(view -> {
            mViewModel.deleteProduct();
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
    }

    public void setProductContainerValues(View rootView) {
        Spinner spinnerProductContainer = rootView.findViewById(R.id.spinnerProductContainer);

        spinnerProductContainer.setBackground(Objects.requireNonNull(((TextInputLayout) rootView
                .findViewById(R.id.tilProductName)).getEditText()).getBackground());

        mViewModel.getContainerNamesMutableLiveData().observe(getViewLifecycleOwner(), containers -> {
            containers.add(0, "Chose storage");
            CustomAdapter adapter = new CustomAdapter(requireContext(),
                    android.R.layout.simple_spinner_item, containers, 0);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerProductContainer.setAdapter(adapter);
        });
    }

    private void setFieldsValue(View rootView) {
        mViewModel.getProductFullMutableLiveData().observe(getViewLifecycleOwner(), productFull -> {
            setFieldString(rootView, R.id.tilProductName, productFull.productName());
            setFieldFloat(rootView, R.id.tilProductName, productFull.quantity());
            setFieldDate(rootView, R.id.tilExpirationDate, productFull.expirationDate());
            setFieldDate(rootView, R.id.tilAddedDate, productFull.addedDate());
            setContainerValueSelected(rootView, R.id.spinnerProductContainer, productFull.productContainer());

            setFieldFloat(rootView, R.id.tilEnergyValue, productFull.energyValue());
            setFieldFloat(rootView, R.id.tilFatValue, productFull.fatValue());
            setFieldFloat(rootView, R.id.tilCarbohydrateValue, productFull.carbohydrateValue());
            setFieldFloat(rootView, R.id.tilSodium, productFull.sodium());
            setFieldFloat(rootView, R.id.tilCalcium, productFull.calcium());
            setFieldFloat(rootView, R.id.tilProtein, productFull.protein());
            setFieldFloat(rootView, R.id.tilVitamin, productFull.vitamin());
            setFieldString(rootView, R.id.tilVitaminType, productFull.vitaminType());
            setFieldString(rootView, R.id.tilAllergens, productFull.allergens());
        });
    }

    private void setContainerValueSelected(View rootView, int id, String productContainer) {
        Spinner spinner = rootView.findViewById(id);
        mViewModel.getContainerNamesMutableLiveData().observe(getViewLifecycleOwner(), containers -> {
            spinner.setSelection(containers.indexOf(productContainer));
        });
    }

    private void setFieldString(View rootView, int id, String value) {
        if (value == null) {
            return;
        }
        Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id)).getEditText()).setText(value);
    }

    private void setFieldFloat(View rootView, int id, Float value) {
        if (value == null) {
            return;
        }
        var v = value.toString();
        Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id)).getEditText()).setText(v);

    }

    private void setFieldDate(View rootView, int id, Date value) {
        if (value == null) {
            return;
        }
        Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id)).getEditText())
                .setText(value.toString());
    }
}