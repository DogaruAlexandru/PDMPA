package com.example.client.ui.produces.produce;

import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.example.client.R;
import com.example.client.data.model.ProductFull;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class ProduceEditFragment extends Fragment {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private long productId;

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
        productId = getArguments().getLong("productId", -1);
        mViewModel.setProductId(productId);

        setButtonsAction(rootView);
        setProductContainerValues(rootView);

        setFieldsValue(rootView);

        return rootView;
    }

    private void setButtonsAction(View rootView) {
        Button btnEdit = rootView.findViewById(R.id.btnEdit);
        Button btnBack = rootView.findViewById(R.id.btnBack);
        Button btnRemove = rootView.findViewById(R.id.btnRemove);

        setDatePickerAction(rootView);

        btnEdit.setOnClickListener(view -> {
            mViewModel.updateProduct(getEditedValues(rootView));
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

    private void setDatePickerAction(View rootView) {
        var dateEdt = ((TextInputLayout) rootView.findViewById(R.id.tilExpirationDate)).getEditText();
        assert dateEdt != null;
        dateEdt.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        c.set(year1, monthOfYear, dayOfMonth);
                        var formattedDate = dateFormat.format(c.getTime());
                        dateEdt.setText(formattedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

    }

    public void setProductContainerValues(View rootView) {
        Spinner spinnerProductContainer = rootView.findViewById(R.id.spinnerProductContainer);

        spinnerProductContainer.setBackground(Objects.requireNonNull(((TextInputLayout) rootView
                .findViewById(R.id.tilProductName)).getEditText()).getBackground());

        mViewModel.getContainerNamesMutableLiveData().observe(getViewLifecycleOwner(), containers -> {
            containers.add(0, "Chose storage");
            ProduceAdapter adapter = new ProduceAdapter(requireContext(),
                    android.R.layout.simple_spinner_item, containers, 0);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerProductContainer.setAdapter(adapter);
        });
    }

    private void setFieldsValue(View rootView) {
        mViewModel.getProductFullMutableLiveData().observe(getViewLifecycleOwner(), productFull -> {
            setFieldString(rootView, R.id.tilProductName, productFull.productName());
            setFieldFloat(rootView, R.id.tilQuantity, productFull.quantity());
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

    private ProductFull getEditedValues(View rootView) {
        return new ProductFull(
                productId,
                getFieldString(rootView, R.id.tilProductName),
                getStringFromSpinner(rootView, R.id.spinnerProductContainer),
                getFieldDate(rootView, R.id.tilExpirationDate),
                getFieldFloat(rootView, R.id.tilQuantity),
                getFieldDate(rootView, R.id.tilAddedDate),

                getFieldFloat(rootView, R.id.tilEnergyValue),
                getFieldFloat(rootView, R.id.tilFatValue),
                getFieldFloat(rootView, R.id.tilCarbohydrateValue),
                getFieldFloat(rootView, R.id.tilSodium),
                getFieldFloat(rootView, R.id.tilCalcium),
                getFieldFloat(rootView, R.id.tilProtein),
                getFieldFloat(rootView, R.id.tilVitamin),
                getFieldString(rootView, R.id.tilVitaminType),
                getFieldString(rootView, R.id.tilAllergens)
        );
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
                .setText(dateFormat.format(value));
    }

    private String getFieldString(View rootView, int id) {
        return Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id)).getEditText())
                .getText().toString();
    }

    private Float getFieldFloat(View rootView, int id) {
        String stringValue = Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id))
                .getEditText()).getText().toString();
        return stringValue.isEmpty() ? null : Float.parseFloat(stringValue);
    }

    private Date getFieldDate(View rootView, int id) {
        String dateString = Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id))
                .getEditText()).getText().toString();

        try {
            return dateString.isEmpty() ? null : dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getStringFromSpinner(View rootView, int id) {
        return ((Spinner) rootView.findViewById(id)).getSelectedItem().toString();
    }

}