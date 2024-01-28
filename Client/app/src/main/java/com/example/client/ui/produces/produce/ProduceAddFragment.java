package com.example.client.ui.produces.produce;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

public class ProduceAddFragment extends Fragment {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    public static ProduceAddFragment newInstance() {
        return new ProduceAddFragment();
    }

    private ProduceAddViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProduceAddViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_produce_add, container, false);

        mViewModel.setUserId(getUserId());

        setButtonsAction(rootView);
        setProductContainerValues(rootView);

        setFieldDate(rootView, R.id.tilAddedDate, new Date());

        return rootView;
    }

    private void setButtonsAction(View rootView) {
        Button btnAdd = rootView.findViewById(R.id.btnAdd);
        Button btnBack = rootView.findViewById(R.id.btnBack);

        setDatePickerAction(rootView);

        btnAdd.setOnClickListener(view -> {
            var values = getValues(rootView);
            if (!validValues(rootView, values)) {
                return;
            }
            mViewModel.addProduct(values);
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        btnBack.setOnClickListener(view -> {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private long getUserId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        return sharedPreferences.getLong("userId", -1);
    }

    private boolean validValues(View rootView, ProductFull values) {
        boolean isValid = true;

        isValid &= notEmptyString(values.productName(), rootView.findViewById(R.id.tilProductName), "Product name");
        isValid &= positiveNotEmptyFloat(values.quantity(), rootView.findViewById(R.id.tilQuantity), "Quantity");
        isValid &= notEmptyDate(values.expirationDate(), rootView.findViewById(R.id.tilExpirationDate), "Expiration date");

        isValid &= validSpinnerSelection(rootView.findViewById(R.id.spinnerProductContainer), rootView);

        isValid &= positiveEmptyFloat(values.energyValue(), rootView.findViewById(R.id.tilEnergyValue), "Energy value");
        isValid &= positiveEmptyFloat(values.fatValue(), rootView.findViewById(R.id.tilFatValue), "Fat value");
        isValid &= positiveEmptyFloat(values.carbohydrateValue(), rootView.findViewById(R.id.tilCarbohydrateValue), "Carbohydrate value");
        isValid &= positiveEmptyFloat(values.sodium(), rootView.findViewById(R.id.tilSodium), "Sodium");
        isValid &= positiveEmptyFloat(values.calcium(), rootView.findViewById(R.id.tilCalcium), "Calcium");
        isValid &= positiveEmptyFloat(values.protein(), rootView.findViewById(R.id.tilProtein), "Protein");
        isValid &= positiveEmptyFloat(values.vitamin(), rootView.findViewById(R.id.tilVitamin), "Vitamin");

        return isValid;
    }

    private boolean validSpinnerSelection(Spinner spinner, View rootView) {
        if (spinner.getSelectedItemPosition() <= 0) {
            spinner.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red));
            return false;
        }
        spinner.setBackground(Objects.requireNonNull(((TextInputLayout) rootView
                .findViewById(R.id.tilProductName)).getEditText()).getBackground());
        return true;
    }

    private boolean notEmptyDate(Date value, TextInputLayout till, String fieldName) {
        if (value == null) {
            till.setError(fieldName + " cannot be empty");
            return false;
        }
        return true;
    }

    private boolean notEmptyString(String value, TextInputLayout till, String fieldName) {
        if (value.isEmpty()) {
            till.setError(fieldName + " cannot be empty");
            return false;
        }
        return true;
    }

    private boolean positiveNotEmptyFloat(Float value, TextInputLayout till, String fieldName) {
        if (value == null) {
            till.setError(fieldName + " cannot be empty");
            return false;
        }
        return positiveFloat(value, till, fieldName);
    }

    private boolean positiveEmptyFloat(Float value, TextInputLayout till, String fieldName) {
        return value == null || positiveFloat(value, till, fieldName);
    }

    private boolean positiveFloat(Float value, TextInputLayout till, String fieldName) {
        if (value < 0) {
            till.setError(fieldName + " cannot be less then 0");
            return false;
        }
        return true;
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

    private ProductFull getValues(View rootView) {
        return new ProductFull(
                null,
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

    private void setFieldDate(View rootView, int id, Date value) {
        if (value == null) {
            return;
        }
        Objects.requireNonNull(((TextInputLayout) rootView.findViewById(id)).getEditText())
                .setText(dateFormat.format(value));
    }

    private String getStringFromSpinner(View rootView, int id) {
        return ((Spinner) rootView.findViewById(id)).getSelectedItem().toString();
    }

}